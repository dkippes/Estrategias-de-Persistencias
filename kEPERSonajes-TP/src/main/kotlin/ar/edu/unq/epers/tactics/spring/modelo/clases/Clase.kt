package ar.edu.unq.epers.tactics.spring.modelo.clases

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship


@Node
class Clase(val nombre: String) {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Relationship(type = "Habilita")
    var mejora: MutableList<Mejora>? = mutableListOf()

    @Relationship(type = "Requiere")
    var requiere: MutableList<Requiere>? = mutableListOf()

    fun esElMismoNodo(clase: Clase): Boolean {
        return this.nombre == clase.nombre
    }

    fun existeLaRelacionDeMejora(clase: Clase): Boolean {
        return this.mejora!!.any { it.claseAMejorar.nombre == clase.nombre }
    }

    fun existeLaRelacionDeRequerimiento(clase: Clase): Boolean {
        return this.requiere!!.any { it.claseARequerir.nombre == clase.nombre }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Clase

        if (nombre != other.nombre) return false

        return true
    }

    override fun hashCode(): Int {
        return nombre.hashCode()
    }
}
