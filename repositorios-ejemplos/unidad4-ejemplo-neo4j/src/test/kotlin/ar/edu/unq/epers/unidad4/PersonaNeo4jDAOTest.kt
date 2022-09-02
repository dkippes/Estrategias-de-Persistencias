package ar.edu.unq.epers.unidad4

import ar.edu.unq.epers.unidad4.dao.PersonaNeo4jDAO
import ar.edu.unq.epers.unidad4.model.Persona
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

// Anotaciones e Imports de Spring: Descomentar para ver los test correr con Spring.
// Los dejamos aca a modo de ejemplo. No hacen falta utilizarlos,
// ya que en el ejemplo estamos usando el driver directamente sin auditoria de Spring en su persistencia.

//import org.junit.jupiter.api.extension.ExtendWith
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.junit.jupiter.SpringExtension

//@ExtendWith(SpringExtension::class)
//@SpringBootTest
@TestInstance(PER_CLASS)
class PersonaNeo4jDAOTest {
    lateinit var dao: PersonaNeo4jDAO

    @BeforeEach
    fun setUp() {
        dao = PersonaNeo4jDAO()
    }

    @AfterEach
    fun after() {
        dao.clear()
    }

    @Test
    fun crearPersona() {
        val jerry = Persona("300000", "Jerry", "Smith")
        val morty = Persona("300001", "Morty", "Smith")

        dao.create(jerry)
        dao.create(morty)

        dao.crearRelacionEsHijoDe(jerry, morty)
    }

    @Test
    fun hijosDe() {
        val jerry = Persona("300000", "Jerry", "Smith")
        val morty = Persona("300001", "Morty", "Smith")
        val summer = Persona("300002", "Summer", "Smith")

        dao.create(jerry)
        dao.create(summer)
        dao.create(morty)

        dao.crearRelacionEsHijoDe(jerry, morty)
        dao.crearRelacionEsHijoDe(jerry, summer)

        val hijos = dao.getHijosDe(jerry)

        Assertions.assertEquals(2, hijos.size.toLong())
        Assertions.assertTrue(hijos.contains(morty))
        Assertions.assertTrue(hijos.contains(summer))
    }
}