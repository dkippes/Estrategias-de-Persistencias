package ar.edu.unq.epers.tactics.spring.modelo.habilidad

import ar.edu.unq.epers.tactics.spring.modelo.Atributos
import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.Ataque
import ar.edu.unq.epers.tactics.spring.modelo.randomizador.RandomizadorSimulado
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AtacarFisicamenteTest {

    private lateinit var aventurero: Aventurero
    private lateinit var aventurero2: Aventurero
    private lateinit var party: Party
    private lateinit var party2: Party
    private lateinit var atacarFisicamente: Ataque

    @BeforeEach
    fun setup() {
        party = Party("Team Rocket", "imagen.jpg")
        party2 = Party("Team Enemigo", "imagen.jpg")
        aventurero = Aventurero("Carlos", null, 1, "Imagen.jpg", Atributos(1.0, 1.0, 1.0, 1.0))
        aventurero2 = Aventurero("Rodolfo", null, 1, "Imagen.jpg", Atributos(1.0, 1.0, 1.0, 1.0))
        party.agregarAventurero(aventurero)
        party2.agregarAventurero(aventurero2)
    }

    @Test
    fun alEjecutarAtaqueExitosoElReceptorDisminuyeSuVida() {
        atacarFisicamente = Ataque(
            aventurero2,
            aventurero,
            aventurero.getPrecisionFisica(),
            aventurero.getDanoFisico(),
            RandomizadorSimulado(1)
        )
        val vidaPreviaAlAtaque = aventurero2.getVida()
        atacarFisicamente.ejecutar()

        assertTrue(atacarFisicamente.esAtaqueExitosoSobre(aventurero2))
        assertEquals(
            vidaPreviaAlAtaque - atacarFisicamente.aventureroReceptor.getDanoFisico(),
            aventurero2.getVida()
        )
    }

    @Test
    fun alEjecutarUnAtaqueNoExitosoElReceptorNoVeAfectadaSuVida() {
        aventurero2.atributos?.constitucion = 20.0
        atacarFisicamente = Ataque(
            aventurero2,
            aventurero,
            aventurero.getPrecisionFisica(),
            aventurero.getDanoFisico(),
            RandomizadorSimulado(1)
        )
        val vidaPreviaAlAtaque = aventurero2.getVida()
        atacarFisicamente.ejecutar()

        assertFalse(atacarFisicamente.esAtaqueExitosoSobre(aventurero2))
        assertEquals(vidaPreviaAlAtaque, aventurero2.getVida())
    }

    @Test
    fun unAventureroNoPuedeAtacarseFisicamenteAsiMismo() {
        val aventureroEsperadoSinCambios = aventurero.getVida()
        Ataque(
            aventurero,
            aventurero,
            aventurero.getPrecisionFisica(),
            aventurero.getDanoFisico(),
            RandomizadorSimulado(1)
        ).ejecutar()
        assertEquals(aventurero.getVida(), aventureroEsperadoSinCambios)
    }
}
