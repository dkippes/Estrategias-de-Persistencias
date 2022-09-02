package ar.edu.unq.epers.unidad4.dao

import ar.edu.unq.epers.unidad4.model.Persona
import org.neo4j.driver.*

/**
 * PersonaNeo4jDAO encapsula el acceso a Neo4j
 */
class PersonaNeo4jDAO {
    private val driver: Driver

    init {
        val env = System.getenv()
        val url = env.getOrDefault("NEO_URL", "bolt://localhost:7687")
        val username = env.getOrDefault("NEO_USER", "neo4j")
        val password = env.getOrDefault("NEO_PASSWORD", "root")

        driver = GraphDatabase.driver(url, AuthTokens.basic(username, password),
            Config.builder().withLogging(Logging.slf4j()).build()
        )
    }

    fun create(persona: Persona) {
        driver.session().use { session ->

            session.writeTransaction {
                val query = "MERGE (n:Persona {dni: ${'$'}elDni, name:  ${'$'}elNombre, surname: ${'$'}elApellido })"
                it.run(query, Values.parameters(
                    "elNombre", persona.nombre,
                    "elDni", persona.dni,
                    "elApellido", persona.apellido
                ))
            }
        }
    }

    fun crearRelacionEsHijoDe(padre: Persona, hijo: Persona) {
        driver.session().use { session ->
            val query = """
                MATCH (padre:Persona {dni: ${'$'}elDniPadre})
                MATCH (hijo:Persona {dni: ${'$'}elDniHijo})
                MERGE (padre)-[:padreDe]->(hijo)
                MERGE (hijo)-[:hijoDe]->(padre)
            """
            session.run(
                query, Values.parameters(
                    "elDniPadre", padre.dni,
                    "elDniHijo", hijo.dni
                )
            )
        }
    }

    fun getHijosDe(padre: Persona): List<Persona> {
        return driver.session().use { session ->
            val query = """
                MATCH (padre:Persona {dni: ${'$'}elDniPadre }) 
                MATCH (hijo)-[:hijoDe]->(padre)
                RETURN hijo
            """
            val result = session.run(query, Values.parameters("elDniPadre", padre.dni))
            result.list { record: Record ->
                val hijo = record[0]
                val dni = hijo["dni"].asString()
                val nombre = hijo["nombre"].asString()
                val apellido = hijo["apellido"].asString()
                Persona(dni, nombre, apellido)
            }
        }
    }

    fun clear() {
        return driver.session().use { session ->
            session.run("MATCH (n) DETACH DELETE n")
        }
    }
}