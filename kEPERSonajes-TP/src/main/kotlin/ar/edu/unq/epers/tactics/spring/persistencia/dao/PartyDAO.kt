package ar.edu.unq.epers.tactics.spring.persistencia.dao

import ar.edu.unq.epers.tactics.spring.modelo.Party

interface PartyDAO {
    fun crear(party: Party): Party
    fun actualizar(party: Party): Party
    fun recuperar(idDeLaParty: Long): Party
    fun recuperarTodas(): List<Party>
}