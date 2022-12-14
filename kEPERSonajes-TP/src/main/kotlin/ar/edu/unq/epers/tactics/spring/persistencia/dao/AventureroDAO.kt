package ar.edu.unq.epers.tactics.spring.persistencia.dao

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero

interface AventureroDAO {
    fun actualizar(aventurero: Aventurero): Aventurero
    fun recuperar(idDelAventurero: Long): Aventurero
    fun eliminar(aventurero: Aventurero)
}