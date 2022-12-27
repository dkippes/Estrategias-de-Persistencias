package ar.edu.unq.epers.tactics.spring.service

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.Party

interface PartyService {
    fun crear(party: Party): Party
    fun actualizar(party: Party): Party
    fun recuperar(idDeLaParty: Long): Party
    fun recuperarTodas(): List<Party>
    fun agregarAventureroAParty(idDeLaParty: Long, aventurero: Aventurero): Aventurero
    fun recuperarOrdenadas(orden: Orden, direccion: Direccion, pagina: Int?): PartyPaginadas
}

class PartyPaginadas(var parties: List<Party>, var total: Int)
enum class Orden(val value:String) {
    PODER("poder"),
    VICTORIAS("victorias"),
    DERROTAS("derrotas")
}

enum class Direccion {
    ASCENDENTE,
    DESCENDENTE
}
