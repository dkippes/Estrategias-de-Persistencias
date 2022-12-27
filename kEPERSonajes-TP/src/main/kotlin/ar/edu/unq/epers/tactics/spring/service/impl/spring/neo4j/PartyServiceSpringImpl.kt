package ar.edu.unq.epers.tactics.spring.service.impl.spring.neo4j

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.spring.SpringPartyDAO
import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.spring.SpringPeleaDAO
import ar.edu.unq.epers.tactics.spring.service.Direccion
import ar.edu.unq.epers.tactics.spring.service.Orden
import ar.edu.unq.epers.tactics.spring.service.PartyPaginadas
import ar.edu.unq.epers.tactics.spring.service.PartyService
import ar.edu.unq.epers.tactics.spring.service.exception.PartyNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.JpaSort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PartyServiceSpringImpl(@Autowired val partyDAO: SpringPartyDAO, @Autowired val peleaDAO: SpringPeleaDAO) :
    PartyService {
    override fun crear(party: Party): Party {
        return partyDAO.save(party)
    }

    override fun actualizar(party: Party): Party {
        if (!partyDAO.existsById(party.id!!)) throw PartyNotFoundException(party.id!!)
        return partyDAO.save(party)
    }

    override fun recuperar(idDeLaParty: Long): Party {
        return partyDAO.findById(idDeLaParty).orElseThrow { PartyNotFoundException(idDeLaParty) }
    }

    override fun recuperarTodas(): List<Party> {
        return partyDAO.findAll().toList()
    }

    override fun agregarAventureroAParty(idDeLaParty: Long, aventurero: Aventurero): Aventurero {
        val partyRecuperada = this.recuperar(idDeLaParty)
        partyRecuperada.agregarAventurero(aventurero)
        return aventurero
    }

    override fun recuperarOrdenadas(orden: Orden, direccion: Direccion, pagina: Int?): PartyPaginadas {
        val sortDirection = if (direccion == Direccion.DESCENDENTE) Sort.Direction.DESC else Sort.Direction.ASC

        val pag: Pageable = PageRequest.of(pagina ?: 0, 10, JpaSort.unsafe(sortDirection, "(${orden})"))

        val parties = when(orden) {
            Orden.PODER -> partyDAO.findAllByOrderByPoderDeLaParty(pag);
            Orden.DERROTAS -> partyDAO.findAllByOrderByDerrotas(pag)
            Orden.VICTORIAS -> partyDAO.findAllByOrderByVictorias(pag)
        }

        val partiesCount =  partyDAO.count();

        return PartyPaginadas(parties, partiesCount.toInt())
    }



    fun clearAll() {
        peleaDAO.deleteAll()
        partyDAO.deleteAll()
    }
}