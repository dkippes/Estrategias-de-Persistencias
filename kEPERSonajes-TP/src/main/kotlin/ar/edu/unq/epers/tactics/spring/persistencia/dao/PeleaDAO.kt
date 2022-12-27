package ar.edu.unq.epers.tactics.spring.persistencia.dao

import ar.edu.unq.epers.tactics.spring.modelo.Pelea

interface PeleaDAO {
    fun crear(pelea: Pelea): Pelea
    fun actualizar(pelea: Pelea): Pelea
    fun recuperar(idDeLaPelea: Long): Pelea
    fun encontrarPorParty(partyId: Long): Pelea?
}