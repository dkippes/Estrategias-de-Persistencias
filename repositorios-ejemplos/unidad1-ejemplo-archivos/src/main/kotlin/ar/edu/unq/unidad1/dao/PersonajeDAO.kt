package ar.edu.unq.unidad1.dao

import ar.edu.unq.unidad1.modelo.Personaje

/**
 * Tiene la responsabilidad de guardar y recuperar personajes desde
 * el medio persistente
 */
interface PersonajeDAO {
    fun guardar(personaje: Personaje)
    fun recuperar(nombre: String): Personaje?
}