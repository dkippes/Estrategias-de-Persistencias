package ar.edu.unq.epers.tactics.spring.service

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.modelo.Pelea
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.Habilidad

interface PeleaService {
    fun iniciarPelea(idDeLaParty: Long, partyEnemiga: String): Pelea
    fun estaEnPelea(partyId: Long): Boolean
    fun resolverTurno(peleaId: Long, aventureroId: Long, enemigos: List<Aventurero>): Habilidad
    fun recibirHabilidad(aventureroId: Long, habilidad: Habilidad): Aventurero
    fun terminarPelea(idPelea: Long): Party
    fun actualizar(pelea: Pelea): Pelea
    fun recuperar(idDeLaPelea: Long): Pelea
    fun recuperarOrdenadas(partyId: Long, pagina: Int?): PeleasPaginadas
}

class PeleasPaginadas(var peleas: List<Pelea>, var total: Int)