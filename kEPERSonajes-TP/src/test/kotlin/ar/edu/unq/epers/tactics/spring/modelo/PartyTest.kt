package ar.edu.unq.epers.tactics.spring.modelo

import ar.edu.unq.epers.tactics.spring.modelo.excepcion.MaximoDeAventurerosEnPartyException
import helpers.Factory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows

class PartyTest {
    private lateinit var aventurero: Aventurero
    private lateinit var party: Party

    @BeforeEach
    fun setup() {
        party = Party("Team Rocket", "imagen.jpg")
        aventurero = Factory.crearAventurero("Rodolfo", party)
    }

    @Test
    fun crearParty() {
        party = Party("Team Rocket", "imagen.jpg")
        assertAll(
            { assertEquals(party.id, null) },
            { assertEquals(party.nombre, "Team Rocket") },
            { assertEquals(party.imagenUrl, "imagen.jpg") }
        )
    }

    @Test
    fun unAventureroPuedeAAgregarseAUnaParty() {
        party.agregarAventurero(aventurero)

        assertEquals(1, party.numeroDeAventureros)
    }

    @Test
    fun noSePuedeAgregarMasDe5AventurerosAUnaParty() {
        party.agregarAventurero(Factory.crearAventurero("Ash"))
        party.agregarAventurero(Factory.crearAventurero("Yamala"))
        party.agregarAventurero(Factory.crearAventurero("GUTS"))
        party.agregarAventurero(Factory.crearAventurero("Carlitos Sainz"))
        party.agregarAventurero(Factory.crearAventurero("Carlitos Menem"))


        assertThrows<MaximoDeAventurerosEnPartyException> {
            party.agregarAventurero(aventurero)
        }
    }
}