package ar.edu.unq.epers.tactics.spring.service.impl.hibernate

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.spring.service.Direccion
import ar.edu.unq.epers.tactics.spring.service.Orden
import ar.edu.unq.epers.tactics.spring.service.PartyPaginadas
import ar.edu.unq.epers.tactics.spring.service.PartyService
import ar.edu.unq.epers.tactics.spring.service.runner.HibernateTransactionRunner.runTrx

class HibernatePartyServiceImpl(private val partyDao: PartyDAO) : PartyService {

    override fun crear(party: Party): Party {
        return runTrx { partyDao.crear(party) }
    }

    override fun actualizar(party: Party): Party {
        if (party.id === null) {
            throw IllegalArgumentException()
        }
        return runTrx {
            partyDao.actualizar(party)
        }
    }

    override fun recuperar(idDeLaParty: Long): Party {
        return runTrx { partyDao.recuperar(idDeLaParty) }
    }

    override fun recuperarTodas(): List<Party> {
        return runTrx { partyDao.recuperarTodas() }
    }

    override fun agregarAventureroAParty(idDeLaParty: Long, aventurero: Aventurero): Aventurero {
        return runTrx {
            val party = partyDao.recuperar(idDeLaParty)
            party.agregarAventurero(aventurero)
            partyDao.actualizar(party)
            aventurero
        }
    }

    override fun recuperarOrdenadas(orden: Orden, direccion: Direccion, pagina: Int?): PartyPaginadas {
        TODO("Not yet implemented")
    }
}