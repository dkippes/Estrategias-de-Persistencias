package ar.edu.unq.epers.tactics.spring.service.impl.spring.neo4j


import ar.edu.unq.epers.tactics.spring.helpers.MapperHelper
import ar.edu.unq.epers.tactics.spring.modelo.Atributo
import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.clases.Clase
import ar.edu.unq.epers.tactics.spring.modelo.clases.Mejora
import ar.edu.unq.epers.tactics.spring.modelo.clases.Requiere
import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.neo4j.Neo4jClaseDAO
import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.spring.SpringAventureroDAO
import ar.edu.unq.epers.tactics.spring.service.ClaseService
import ar.edu.unq.epers.tactics.spring.service.exception.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ClaseServiceSpringImpl(
    @Autowired
    private var claseDao: Neo4jClaseDAO,
    @Autowired
    private var aventureroDao: SpringAventureroDAO
) : ClaseService {

    init {
        if (!claseDao.existsByNombre("Aventurero")) {
            claseDao.save(Clase("Aventurero"))
        }
    }

    override fun crearClase(nombreDeClase: String) {
        if (claseDao.existsByNombre(nombreDeClase)) throw ClaseCannotCreateException(nombreDeClase)
        claseDao.save(Clase(nombreDeClase))
    }

    fun recuperar(nombreDeClase: String): Clase {
        if (!claseDao.existsByNombre(nombreDeClase)) throw ClaseNotFoundException(nombreDeClase)
        return claseDao.findByNombre(nombreDeClase)
    }

    override fun crearMejora(
        nombreDeClase1: String,
        nombreDeClase2: String,
        atributos: List<Atributo>,
        bonoALosAtributos: Int
    ) {
        val clase1 = this.recuperar(nombreDeClase1)
        val clase2 = this.recuperar(nombreDeClase2)
        if (clase1.esElMismoNodo(clase2)) {
            throw RelacionNoPuedeSerElMismoNodoException("mejora", nombreDeClase1)
        }
        clase1.mejora!!.removeIf { it -> it.claseAMejorar.nombre == clase2.nombre }
        clase1.mejora!!.add(Mejora(clase2, bonoALosAtributos, atributos))
        claseDao.save(clase1)
    }

    override fun requerir(nombreDeClase1: String, nombreDeClase2: String) {
        val clase1 = this.recuperar(nombreDeClase1)
        val clase2 = this.recuperar(nombreDeClase2)
        if (clase1.nombre == clase2.nombre) {
            throw RelacionNoPuedeSerElMismoNodoException("requerir", nombreDeClase1)
        }
        if (clase1.existeLaRelacionDeRequerimiento(clase2)) {
            throw YaExisteLaRelacionException("requerir")
        }
        if (clase2.existeLaRelacionDeRequerimiento(clase1)) {
            throw RelacionBidireccionalException("requerir")
        }
        clase1.requiere!!.add(Requiere(clase2))
        claseDao.save(clase1)
    }

    override fun ganarProficiencia(aventureroId: Long, nombreDeClase1: String, nombreDeClase2: String): Aventurero {
        val aventurero = aventureroDao.findById(aventureroId).orElseThrow { AventureroNotFoundException(aventureroId) }

        if(!aventurero.clases.contains(nombreDeClase1)) throw AventureroNoTieneClaseException(aventureroId, nombreDeClase1)
        if (!claseDao.existsByNombre(nombreDeClase2)) throw ClaseNotFoundException(nombreDeClase2)

        //Obtengo la clase con la mejora correspondiente
        val clase = claseDao.findClaseByClaseActualAndClaseAMejorar(nombreDeClase1, nombreDeClase2)
            .orElseThrow { ClaseNotFoundException(nombreDeClase1) }
        val mejora = clase.mejora!![0]

        // Compruebo si puede mejorar
        if (!this.puedeMejorar(aventurero.id!!, mejora)) throw RelacionNoExisteException("Habilita")

        // Hago que el aventurero gane proficiencia
        aventurero.ganarProficiencia(mejora)

        // Updateo y Guardo el aventurero
        aventureroDao.save(aventurero)
        return aventurero
    }

    override fun caminoMasRentable(
        aventureroId: Long,
        atributo: Atributo,
        nombreDeClaseDePartida: String,
        nombreDeClaseDeLlegada: String
    ): List<Mejora> {
        val aventurero = aventureroDao.findById(aventureroId).orElseThrow { AventureroNotFoundException(aventureroId) }

        val mejorasSinMapear = claseDao.caminoMasRentable(
            nombreDeClaseDePartida,
            nombreDeClaseDeLlegada,
            atributo,
            aventurero.proficienciaAGastar
        )
        val caminoRentable = mutableListOf<Mejora>()
        MapperHelper.map(mejorasSinMapear, caminoRentable)
        return caminoRentable
    }

    override fun puedeMejorar(aventureroId: Long, mejora: Mejora): Boolean {
        val mejorasPosibles = this.posiblesMejoras(aventureroId)
        return mejorasPosibles.contains(mejora)
    }

    override fun posiblesMejoras(aventureroId: Long): Set<Mejora> {
        val aventurero: Aventurero =
            aventureroDao.findById(aventureroId).orElseThrow { AventureroNotFoundException(aventureroId) }
        val clasesAMejorar = claseDao.findPosiblesMejoras(aventurero.clases)
        val posiblesMejoras = mutableSetOf<Mejora>()
        MapperHelper.map(clasesAMejorar, posiblesMejoras)
        return posiblesMejoras
    }

    fun deleteAll() {
        claseDao.detachDelete()
    }
}