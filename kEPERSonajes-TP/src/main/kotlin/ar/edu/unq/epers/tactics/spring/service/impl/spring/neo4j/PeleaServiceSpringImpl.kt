package ar.edu.unq.epers.tactics.spring.service.impl.spring.neo4j

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.EstadoResultado
import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.modelo.Pelea
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.Habilidad
import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.spring.SpringAventureroDAO
import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.spring.SpringPartyDAO
import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.spring.SpringPeleaDAO
import ar.edu.unq.epers.tactics.spring.service.PeleaService
import ar.edu.unq.epers.tactics.spring.service.PeleasPaginadas
import ar.edu.unq.epers.tactics.spring.service.exception.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PeleaServiceSpringImpl : PeleaService {

    @Autowired
    private lateinit var peleaDao: SpringPeleaDAO

    @Autowired
    private lateinit var partyDao: SpringPartyDAO

    @Autowired
    private lateinit var aventureroDao: SpringAventureroDAO

    override fun iniciarPelea(idDeLaParty: Long, partyEnemiga: String): Pelea {
        val party = this.partyDao.findById(idDeLaParty).orElseThrow { throw PartyNotFoundException(idDeLaParty) }
        party.empezarPelea()
        return peleaDao.save(Pelea(party, partyEnemiga))
    }

    override fun estaEnPelea(partyId: Long): Boolean {
        val party = partyDao.findById(partyId).orElseThrow { throw PartyNotFoundException(partyId) }
        return party.estaPeleando
    }

    override fun resolverTurno(peleaId: Long, aventureroId: Long, enemigos: List<Aventurero>): Habilidad {
        val pelea = peleaDao.findById(peleaId).orElseThrow { throw PeleaNotFoundException(peleaId) }
        if (pelea.estaLaPeleaTerminada()) {
            throw PeleaTerminadaException(peleaId)
        }
        val aventurero =
            aventureroDao.findById(aventureroId).orElseThrow { throw AventureroNotFoundException(aventureroId) }
        if (!pelea.aventureroExisteEnLaPelea(aventurero)) {
            throw AventureroNoExisteEnLaPeleaException(aventureroId, peleaId)
        }
        val habilidad = aventurero.resolverTactica(enemigos)
        pelea.agregarHabilidad(habilidad)
        return habilidad
    }

    override fun recibirHabilidad(aventureroId: Long, habilidad: Habilidad): Aventurero {
        val aventurero =
            aventureroDao.findById(aventureroId).orElseThrow { throw AventureroNotFoundException(aventureroId) }
        if (!aventurero.party!!.estaPeleando) throw AventureroNoPeleandoException(aventurero.id!!)

        habilidad.aventureroReceptor = aventurero
        val pelea = peleaDao.findByPartyAndResultado(aventurero.party!!, EstadoResultado.UNDEFINED)
        pelea.agregarHabilidad(habilidad)
        habilidad.ejecutar()
        return habilidad.aventureroReceptor
    }

    override fun terminarPelea(idPelea: Long): Party {
        val pelea = this.recuperar(idPelea)
        if (pelea.estaLaPeleaTerminada()) {
            throw PeleaTerminadaException(idPelea)
        }
        pelea.dejarDePelear()
        return pelea.party!!
    }

    override fun actualizar(pelea: Pelea): Pelea {
        if (!peleaDao.existsById(pelea.id!!)) throw PeleaNotFoundException(pelea.id!!)
        return peleaDao.save(pelea)
    }

    override fun recuperar(idDeLaPelea: Long): Pelea {
        return this.peleaDao.findById(idDeLaPelea).orElseThrow { throw PeleaNotFoundException(idDeLaPelea) }
    }

    override fun recuperarOrdenadas(partyId: Long, pagina: Int?): PeleasPaginadas {
        val party = partyDao.findById(partyId).orElseThrow { throw PartyNotFoundException(partyId) }
        val pag: Pageable = PageRequest.of(pagina ?: 0, 10, Sort.by("date").descending())
        val peleas = peleaDao.findAllByParty(party, pag).toList()
        return PeleasPaginadas(peleas, peleas.size)
    }

    fun clearAll() {
        partyDao.findAll()
        aventureroDao.findAll()
        peleaDao.findAll()
    }
}