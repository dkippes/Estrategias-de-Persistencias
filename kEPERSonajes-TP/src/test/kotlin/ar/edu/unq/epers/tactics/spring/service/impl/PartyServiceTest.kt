package ar.edu.unq.epers.tactics.spring.service.impl

import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.spring.SpringPartyDAO
import ar.edu.unq.epers.tactics.spring.service.PartyService
import helpers.DataDAO
import helpers.Factory
import org.junit.jupiter.api.*
import javax.persistence.EntityNotFoundException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class PartyServiceTest {
    lateinit var partyDAO: PartyDAO
    lateinit var partyService: PartyService

    @Test
    fun recuperarPartyConIdExistente() {
        val partyCreada = partyService.crear(Party("party1", "ejemplo"))
        val partyActual = partyService.recuperar(partyCreada.id!!)

        assertAll(
            { Assertions.assertEquals(partyActual.nombre, "party1") },
            { Assertions.assertEquals(partyActual.id, partyCreada.id!!) },
            { Assertions.assertEquals(partyActual.imagenUrl, "ejemplo") },
            { Assertions.assertEquals(partyActual.numeroDeAventureros, 0) }
        )
    }

    @Test
    fun errorAlNoPoderRecuperarPartyConIdInexistente() {
        partyService.crear(Party("party1", "Ejemplo"))
        assertThrows<EntityNotFoundException> { partyService.recuperar(25000) }
    }

    @Test
    fun recuperaTodasLasPartys() {
        val party1 = partyService.crear(Party("party1", "Ejemplo1"))
        val party2 = partyService.crear(Party("party2", "Ejemplo2"))
        val partys = partyService.recuperarTodas()

        Assertions.assertEquals(listOf(party1, party2), partys)
    }

    @Test
    fun agregarAventureroAUnaParty() {
        val party1 = partyService.crear(Party("party1", "Ejemplo"))
        val aventurero = Factory.crearAventurero("Aventurero", party1)
        val partyCreada = partyService.agregarAventureroAParty(1, aventurero)
        val partyActual = partyService.recuperar(partyCreada.id!!)

        Assertions.assertEquals(1, partyActual.numeroDeAventureros)
    }

    @Test
    fun agregarAventureroAUnaPartyInexistente() {
        val party1 = Party("party1", "Ejemplo")
        val aventurero = Factory.crearAventurero("Aventurero", party1)

        assertThrows<EntityNotFoundException> { partyService.agregarAventureroAParty(100000, aventurero) }
    }

    @Test
    fun actualizarParty() {
        val imagenUrl = "imagen.jpg"
        val party = Party("party1", imagenUrl)
        val nuevoNombre = "partyActualizada"
        val partyPersistida = partyService.crear(party)

        partyPersistida.nombre = nuevoNombre

        val partyActual = partyService.actualizar(partyPersistida)

        assertAll(
            { Assertions.assertEquals(partyActual.nombre, nuevoNombre) },
            { Assertions.assertEquals(partyActual.id, partyPersistida.id!!) },
            { Assertions.assertEquals(partyActual.imagenUrl, imagenUrl) }
        )
    }

    @Test
    fun alActualizarLaPartyElIdEsNull() {
        val partyActual = Party("party1", "Ejemplo.jpg")

        assertThrows<IllegalArgumentException> { partyService.actualizar(partyActual) }
    }

    @Test
    fun alActualizarLaPartyElIdNoExiste() {
        val partyActual = Party("Team Rocket", "Ejemplo")
        partyActual.id = 1000000

        assertThrows<RuntimeException> { partyService.actualizar(partyActual) }
    }

    @AfterEach
    abstract fun cleanup()
}