package ar.edu.unq.epers.tactics.spring.service

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero

interface AventureroService {
    fun actualizar(aventurero: Aventurero): Aventurero
    fun recuperar(idDelAventurero: Long): Aventurero
    fun eliminar(aventureroId: Long)
}