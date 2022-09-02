package ar.edu.unq.unidad1.modelo

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.thoughtworks.xstream.annotations.XStreamAlias
import java.io.Serializable

@XStreamAlias("item")
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.ANY)
class Item private constructor() : Serializable {
    var nombre: String? = null
        private set
    var peso = 0
        private set

    constructor(nombre: String?, peso: Int) : this() {
        this.nombre = nombre
        this.peso = peso
    }

    override fun toString(): String {
        return nombre!!
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}