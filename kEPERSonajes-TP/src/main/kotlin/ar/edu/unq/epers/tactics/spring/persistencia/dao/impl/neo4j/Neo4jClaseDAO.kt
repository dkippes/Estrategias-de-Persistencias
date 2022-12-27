package ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.neo4j

import ar.edu.unq.epers.tactics.spring.modelo.Atributo
import ar.edu.unq.epers.tactics.spring.modelo.clases.Clase
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface Neo4jClaseDAO : Neo4jRepository<Clase, Long> {
    @Query("MATCH(n) DETACH DELETE n")
    fun detachDelete()

    fun findByNombre(nombreDeClase: String): Clase
    fun existsByNombre(nombreDeClase: String): Boolean

    @Query(
        """
        MATCH(clase1:Clase {nombre: ${'$'}n1})-[r:Habilita]->(clase2:Clase {nombre:${'$'}n2})
        RETURN clase1,collect(r),collect(clase2)
    """
    )
    fun findClaseByClaseActualAndClaseAMejorar(n1: String, n2: String): Optional<Clase>

    @Query(
        "MATCH (ant:Clase)-[m:Habilita]->(aMejorar:Clase) WHERE NOT aMejorar.nombre IN ${'$'}clasesDelAventurero " +
                "AND ant.nombre IN ${'$'}clasesDelAventurero OPTIONAL MATCH (aMejorar)-[r:Requiere]->(req:Clase) " +
                "WITH aMejorar, ant.nombre AS claseActual, aMejorar.nombre AS claseAMejorar, collect(req.nombre) AS requerimientos, m AS mejora MATCH (aMejorar) " +
                "WHERE ALL(req IN requerimientos WHERE req IN ${'$'}clasesDelAventurero) " +
                "RETURN collect({ claseAMejorar: claseAMejorar, atributos:mejora.atributos, bonoPorAtributo:mejora.bonoPorAtributo})"
    )
    fun findPosiblesMejoras(clasesDelAventurero: MutableSet<String>): Any

    @Query(
        """ 
            MATCH p=(a:Clase {nombre: ${'$'}claseDestino})<-[r:Habilita*]-(b:Clase {nombre: ${'$'}claseOrigen})
            WHERE ANY(subh IN r WHERE ANY(atributo in subh.atributos WHERE atributo = ${'$'}atributo))
            UNWIND r as mejora
            WITH p, a, mejora, r , sum(CASE ${'$'}atributo in mejora.atributos WHEN true then  mejora.bonoPorAtributo ELSE 0 END) as s
            WITH p, a, mejora, r, max(s) as maxs, nodes(p) as nodos
            ORDER by maxs DESC
            LIMIT 1
            return  [ i in range(0, size(r) - 1) | {claseAMejorar: nodos[i].nombre, bonoPorAtributo: r[i].bonoPorAtributo, atributos: r[i].atributos }]
        """
    )
    fun caminoMasRentable(
        claseOrigen: String,
        claseDestino: String,
        atributo: Atributo,
        proficienciaDelAventurero: Int
    ): Any
}