package ar.edu.unq.epers.tactics.spring.modelo.habilidad

import ar.edu.unq.epers.tactics.spring.modelo.Atributos
import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.modelo.excepcion.NoSePuedeDefenderException
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.Defensa
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DefenderTest {

    private lateinit var aventurero: Aventurero
    private lateinit var aventurero2: Aventurero
    private lateinit var aventurero3: Aventurero
    private lateinit var party: Party
    private lateinit var party2: Party

    @BeforeEach
    fun setup() {
        party = Party("Team Rocket", "imagen.jpg")
        party2 = Party("Team Enemigo", "imagen.jpg")
        aventurero = Aventurero("Carlos", party, 1, "Imagen.jpg", Atributos(1.0, 1.0, 1.0, 1.0))
        aventurero2 = Aventurero("Roberto", party2, 1, "Imagen.jpg", Atributos(1.0, 1.0, 1.0, 1.0))
        aventurero3 = Aventurero("Diego", party2, 1, "Imagen.jpg", Atributos(1.0, 1.0, 1.0, 1.0))
    }

    @Test
    fun alEjecutarUnaDefensaElDefensorEsAsignadoAlReceptor() {
        aventurero.id = 1
        aventurero2.id = 2
        val defender = Defensa(aventurero, aventurero2)
        defender.ejecutar()

        assertTrue(aventurero.defensor == aventurero2)
    }

    @Test
    fun alEjecutarUnaDefensaElDefensorNoPuedeSerAsignadoAlReceptorPorqueNoTieneId() {
        aventurero2.id = null
        aventurero.id = 1
        val defender = Defensa(aventurero, aventurero2)

        assertThrows<NoSePuedeDefenderException> { defender.ejecutar() }
    }

    @Test
    fun alEjecutarUnaDefensaElDefensorNoPuedeSerAsignadoAlReceptorSiSonElMismo() {
        aventurero2.id = 1
        aventurero.id = 1
        val defender = Defensa(aventurero, aventurero2)

        assertThrows<NoSePuedeDefenderException> { defender.ejecutar() }
    }

    @Test
    fun alEjecutarUnaDefensaElDefensorNoPuedeSerAsignadoAlReceptorSiElReceptorYaTieneDefensor() {
        aventurero2.id = 1
        aventurero.id = 2
        aventurero.defensor = aventurero2
        val defender = Defensa(aventurero, aventurero2)

        assertThrows<NoSePuedeDefenderException> { defender.ejecutar() }
    }
}
