package ar.edu.unq.unidad1.modelo

import ar.edu.unq.unidad1.modelo.exception.MuchoPesoException
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.thoughtworks.xstream.annotations.XStreamAlias
import java.io.Serializable
import java.util.*

@XStreamAlias("personaje")
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.ANY, fieldVisibility = JsonAutoDetect.Visibility.ANY)
class Personaje private constructor() : Serializable {
    lateinit var nombre: String
    var pesoMaximo = 0
    var xp = 0
    var vida = 0
    var inventario: MutableSet<Item> = HashSet()

    constructor(nombre: String) : this() {
        this.nombre = nombre
    }

    fun recoger(item: Item) {
        val pesoActual = pesoActual
        if (pesoActual + item.peso > pesoMaximo) {
            throw MuchoPesoException(this, item)
        }
        inventario.add(item)
    }

    private val pesoActual: Int
         get() {
            var pesoActual = 0
            for (item in inventario) {
                pesoActual += item.peso
            }
            return pesoActual
        }

    override fun toString(): String {
        return nombre
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}