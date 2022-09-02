package ar.edu.unq.unidad1.modelo.exception

import ar.edu.unq.unidad1.modelo.Item
import ar.edu.unq.unidad1.modelo.Personaje

class MuchoPesoException(private val personaje: Personaje, private val item: Item) :  RuntimeException() {

    override val message = "El personaje [$personaje] no puede recoger [$item] porque cagar mucho peso ya"

}