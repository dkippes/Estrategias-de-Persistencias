package ar.edu.unq.epers.tactics.spring.modelo.clases

import ar.edu.unq.epers.tactics.spring.modelo.Atributo
import org.springframework.data.neo4j.core.schema.RelationshipId
import org.springframework.data.neo4j.core.schema.RelationshipProperties
import org.springframework.data.neo4j.core.schema.TargetNode

@RelationshipProperties
class Mejora(
    @TargetNode
    var claseAMejorar: Clase,
    var bonoPorAtributo: Int,
    var atributos: List<Atributo>
) {
    @RelationshipId
    var id: Long? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Mejora

        if (claseAMejorar != other.claseAMejorar) return false
        if (bonoPorAtributo != other.bonoPorAtributo) return false
        if (atributos != other.atributos) return false

        return true
    }

    override fun hashCode(): Int {
        var result = claseAMejorar.hashCode()
        result = 31 * result + bonoPorAtributo
        result = 31 * result + atributos.hashCode()
        return result
    }
}