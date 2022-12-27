package ar.edu.unq.epers.tactics.spring.service.impl.mongo

import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.modelo.formacion.AtributoDeFormacion
import ar.edu.unq.epers.tactics.spring.modelo.formacion.Formacion
import ar.edu.unq.epers.tactics.spring.modelo.formacion.Requerimiento
import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.mongo.FormacionDAO
import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.mongo.MongoFormacionDAO
import ar.edu.unq.epers.tactics.spring.service.FormacionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MongoFormacionServiceImpl(
    @Autowired val formacionDAO: FormacionDAO,
    @Autowired val mongoFormacionDAO: MongoFormacionDAO
) : FormacionService {
    override fun crearFormacion(
        nombreFormacion: String,
        requerimientos: List<Requerimiento>,
        stats: List<AtributoDeFormacion>
    ): Formacion {
        return mongoFormacionDAO.save(Formacion(nombreFormacion, requerimientos, stats))
    }

    override fun todasLasFormaciones(): List<Formacion> {
        return mongoFormacionDAO.findAll();
    }

    override fun atributosQueCorresponden(party: Party): List<AtributoDeFormacion> {
        val formacionesDeLaParty = formacionesQuePosee(party).map { it.nombre }
        return mongoFormacionDAO.atributosDeLaFormacion(formacionesDeLaParty as List<String>)
    }

    override fun formacionesQuePosee(party: Party): List<Formacion> {
        return formacionDAO.formacionesQuePosee(party.requerimientosDeLaParty())
    }

    fun deleteAll() {
        mongoFormacionDAO.deleteAll()
    }
}
