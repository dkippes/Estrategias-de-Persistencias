package ar.edu.unq.epers.tactics.spring.service.impl

import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.service.AventureroService
import ar.edu.unq.epers.tactics.spring.service.PartyService
import ar.edu.unq.epers.tactics.spring.service.exception.AventureroIdNullException
import helpers.DataDAO
import helpers.Factory
import org.junit.jupiter.api.*
import javax.persistence.EntityNotFoundException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AventureroServiceTest {
    open lateinit var aventureroService: AventureroService
    open lateinit var partyService: PartyService

    @Test
    fun recuperarAventureroConIdExistente() {
        val party = Party("Team Rocket", "imagen.jpg")
        val partyCreada = partyService.crear(party)
        val nombreDeAventurero = "Gandalff"
        val aventureroAgregado =
            partyService.agregarAventureroAParty(partyCreada.id!!, Factory.crearAventurero(nombreDeAventurero, party))

        val aventureroActual = aventureroService.recuperar(aventureroAgregado.id!!)

        assertAll(
            { Assertions.assertEquals(nombreDeAventurero, aventureroActual.nombre) },
            { Assertions.assertEquals(aventureroAgregado.id!!, aventureroActual.id) },
            { Assertions.assertEquals(party.id, aventureroActual.party!!.id) }
        )
    }

    @Test
    fun errorAlNoPoderRecuperarAventureroConIdInexistente() {
        val party = Party("Team Rocket", "imagen.jpg")
        val partyPersistida = partyService.crear(party)
        val nombreDeAventurero = "Gandalff"
        partyService.agregarAventureroAParty(partyPersistida.id!!, Factory.crearAventurero(nombreDeAventurero, party))

        assertThrows<EntityNotFoundException> { aventureroService.recuperar(34000) }
    }

    @Test
    fun puedoBorrarUnAventurero() {
        val partyPersistida = partyService.crear(Party("Team Rocket", "imagen.jpg"))
        val aventurero = Factory.crearAventurero("Lord legislador", partyPersistida)
        val aventureroPersitido = partyService.agregarAventureroAParty(partyPersistida.id!!, aventurero)
        aventureroService.eliminar(aventureroPersitido.id!!)
        assertThrows<EntityNotFoundException> { aventureroService.recuperar(aventureroPersitido.id!!) }
    }

    @Test
    fun noPuedoActualizarUnAventureroQueNoFueGuardadoPreviamente() {
        val party = Party("Team Rocket", "imagen.jpg")
        val aventurero = Factory.crearAventurero("Lord legislador", party)

        assertThrows<AventureroIdNullException>("El aventurero tiene el id en null") {
            aventureroService.actualizar(
                aventurero
            )
        }
    }

    @Test
    fun actualizarAventurero() {
        val party = Party("Team Rocket", "imagen.jpg")
        val partyCreada = partyService.crear(party)
        val aventureroCreado =
            partyService.agregarAventureroAParty(partyCreada.id!!, Factory.crearAventurero("Lord legislador"))
        aventureroCreado.nombre = "Gofus"
        val aventureroActual = aventureroService.actualizar(aventureroCreado)

        Assertions.assertEquals("Gofus", aventureroActual.nombre)
    }

    @Test
    fun aventureroAActualizarNoExiste() {
        val party = Party("Team Rocket", "imagen.jpg")
        val partyCreada = partyService.crear(party)
        val aventureroCreado =
            partyService.agregarAventureroAParty(partyCreada.id!!, Factory.crearAventurero("Lord legislador", party))
        aventureroCreado.nombre = "Gofus"
        aventureroCreado.id = 100000000000
        assertThrows<RuntimeException> { aventureroService.actualizar(aventureroCreado) }
    }

    @AfterEach
    abstract fun cleanup()
}