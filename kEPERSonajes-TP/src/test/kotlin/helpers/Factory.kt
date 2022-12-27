package helpers

import ar.edu.unq.epers.tactics.spring.modelo.Atributos
import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.Party

object Factory {
    fun crearAventurero(
        nombre: String = "Alakazam",
        party: Party? = null,
        nivel: Int = 1,
        imagenUrl: String = "Ejemplo.jpg",
        fuerza: Double = 1.0,
        destreza: Double = 1.0,
        inteligencia: Double = 1.0,
        constitucion: Double = 1.0
    ): Aventurero {
        val aventurero = Aventurero(nombre, party, nivel, imagenUrl, Atributos(fuerza, destreza, constitucion, inteligencia))
        return aventurero
    }
}