package ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.spring

import ar.edu.unq.epers.tactics.spring.modelo.EstadoResultado
import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.modelo.Pelea
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface SpringPeleaDAO : PagingAndSortingRepository<Pelea, Long> {
    fun countPeleasByPartyAndResultado(party: Party, resultado: EstadoResultado): Int
    fun findAllByParty(party: Party, pag: Pageable): List<Pelea>
    fun findByPartyAndResultado(party: Party, undefined: EstadoResultado): Pelea
}