package ar.edu.unq.epers.tactics.spring.modelo.clases

import org.springframework.data.neo4j.core.schema.RelationshipId
import org.springframework.data.neo4j.core.schema.RelationshipProperties
import org.springframework.data.neo4j.core.schema.TargetNode

@RelationshipProperties
class Requiere(
    @TargetNode
    var claseARequerir: Clase
) {
    @RelationshipId
    var id: Long? = null
}