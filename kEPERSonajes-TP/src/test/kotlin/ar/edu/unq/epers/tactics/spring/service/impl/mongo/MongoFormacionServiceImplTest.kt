package ar.edu.unq.epers.tactics.spring.service.impl.mongo

import ar.edu.unq.epers.tactics.spring.modelo.Atributo
import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.modelo.formacion.AtributoDeFormacion
import ar.edu.unq.epers.tactics.spring.modelo.formacion.Formacion
import ar.edu.unq.epers.tactics.spring.modelo.formacion.Requerimiento
import helpers.Factory
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MongoFormacionServiceImplTest {
    @Autowired
    lateinit var formacionService: MongoFormacionServiceImpl

    var formacion: Formacion? = null
    var formacion2: Formacion? = null
    var formacion3: Formacion? = null

    @BeforeEach
    fun setUp() {
        val requerimientos = listOf(Requerimiento("Aventurero", 4))
        val status = listOf(AtributoDeFormacion(Atributo.FUERZA, 3))
        formacion = Formacion("Los cafres", requerimientos, status)
        formacion2 = Formacion(
            "Los pericos",
            listOf(Requerimiento("Aventurero", 1), Requerimiento("Magico", 1)),
            listOf(AtributoDeFormacion(Atributo.INTELIGENCIA, 5), AtributoDeFormacion(Atributo.FUERZA, 3))
        )
        formacion3 = Formacion(
            "Los Simuladores",
            listOf(Requerimiento("Aventurero", 1), Requerimiento("Magico", 1)),
            listOf(AtributoDeFormacion(Atributo.INTELIGENCIA, 5), AtributoDeFormacion(Atributo.FUERZA, 4))
        )
    }

    @Test
    fun seCreaUnaFormacionNueva() {
        val formacionCreada = formacionService.crearFormacion(
            formacion!!.nombre!!,
            formacion!!.requisitosFormacion!!,
            formacion!!.atributosFormacion!!
        )
        assertAll({
            assertTrue(formacion!!.id != formacionCreada.id)
            assertEquals(formacion!!.nombre, formacionCreada.nombre)
            assertEquals(formacion!!.requisitosFormacion, formacionCreada.requisitosFormacion)
            assertEquals(formacion!!.atributosFormacion, formacionCreada.atributosFormacion)
        })
    }

    @Test
    fun sePuedeRecuperarTodasLasFormaciones() {
        val formacionCreada1 = formacionService.crearFormacion(
            formacion!!.nombre!!,
            formacion!!.requisitosFormacion!!,
            formacion!!.atributosFormacion!!
        )
        val formacionCreada2 = formacionService.crearFormacion(
            formacion2!!.nombre!!,
            formacion2!!.requisitosFormacion!!,
            formacion2!!.atributosFormacion!!
        )

        val formaciones = formacionService.todasLasFormaciones()
        val (form1, form2) = formaciones

        assertEquals(formaciones.size, 2)
        assertEquals(form1.id!!, formacionCreada1.id)
        assertEquals(form2.id!!, formacionCreada2.id)
    }

    @Test
    fun seObtieneLasFormacionesQuePosee() {
        val formacionCreada1 = formacionService.crearFormacion(
            formacion!!.nombre!!,
            formacion!!.requisitosFormacion!!,
            formacion!!.atributosFormacion!!
        )
        val formacionCreada2 = formacionService.crearFormacion(
            formacion2!!.nombre!!,
            formacion2!!.requisitosFormacion!!,
            formacion2!!.atributosFormacion!!
        )

        val formaciones = formacionService.todasLasFormaciones()
        val (form1, form2) = formaciones

        assertEquals(formaciones.size, 2)
        assertEquals(form1.id!!, formacionCreada1.id)
        assertEquals(form2.id!!, formacionCreada2.id)
    }

    @Test
    fun noTieneFormaciones() {
        formacionService.crearFormacion(
            formacion!!.nombre!!,
            formacion!!.requisitosFormacion!!,
            formacion!!.atributosFormacion!!
        )
        formacionService.crearFormacion(
            formacion2!!.nombre!!,
            formacion2!!.requisitosFormacion!!,
            formacion2!!.atributosFormacion!!
        )

        val party = Party("Los pericos", "Los pericos")
        val aventurero1 = Factory.crearAventurero("Rodolfo", party)
        val aventurero2 = Factory.crearAventurero("Alberto", party)
        aventurero1.clases = mutableSetOf("Aventurero")
        aventurero2.clases = mutableSetOf("Aventurero", "Magico")

        party.aventureros.add(aventurero1)
        party.aventureros.add(aventurero2)
        val formaciones = formacionService.formacionesQuePosee(party)

        assertAll({
            assertTrue(formaciones.isNotEmpty())
            assertTrue(formaciones.first().nombre == "Los pericos")
        })
    }

    @Test
    fun atributosDeLaPartyASumar() {
        formacionService.crearFormacion(
            formacion!!.nombre!!,
            formacion!!.requisitosFormacion!!,
            formacion!!.atributosFormacion!!
        )
        formacionService.crearFormacion(
            formacion2!!.nombre!!,
            formacion2!!.requisitosFormacion!!,
            formacion2!!.atributosFormacion!!
        )

        formacionService.crearFormacion(
            formacion3!!.nombre!!,
            formacion3!!.requisitosFormacion!!,
            formacion3!!.atributosFormacion!!
        )

        val party = Party("Los pericos", "Los pericos")
        val aventurero1 = Factory.crearAventurero("Rodolfo", party)
        val aventurero2 = Factory.crearAventurero("Alberto", party)
        aventurero1.clases = mutableSetOf("Aventurero")
        aventurero2.clases = mutableSetOf("Aventurero", "Magico")

        party.aventureros.add(aventurero1)
        party.aventureros.add(aventurero2)

        val atributosASumar = formacionService.atributosQueCorresponden(party)

        assertAll({
            assertTrue(atributosASumar.first().tipo!!.name == "FUERZA")
            assertTrue(atributosASumar.first().bono == 7)
            assertTrue(atributosASumar.last().tipo!!.name == "INTELIGENCIA")
            assertTrue(atributosASumar.last().bono == 10)
        })
    }

    @AfterEach
    fun tearDown() {
        formacionService.deleteAll()
    }
}
