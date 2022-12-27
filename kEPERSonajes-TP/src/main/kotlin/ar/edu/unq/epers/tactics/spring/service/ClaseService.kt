package ar.edu.unq.epers.tactics.spring.service

import ar.edu.unq.epers.tactics.spring.modelo.Atributo
import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.clases.Mejora



interface ClaseService {

    fun crearClase(nombreDeClase: String);
    fun crearMejora(nombreDeClase1: String, nombreDeClase2: String, atributos: List<Atributo>, bonoALosAtributos: Int)
    fun requerir(nombreDeClase1: String, nombreDeClase2: String)
    fun puedeMejorar(aventureroId: Long, mejora: Mejora): Boolean
    fun ganarProficiencia(aventureroId: Long, nombreDeClase1: String, nombreDeClase2: String): Aventurero
    fun caminoMasRentable(aventureroId: Long, atributo:Atributo, nombreDeClaseDePartida:String, nombreDeClaseDeLlegada:String): List<Mejora>
    fun posiblesMejoras(aventureroId: Long):Set<Mejora>
}