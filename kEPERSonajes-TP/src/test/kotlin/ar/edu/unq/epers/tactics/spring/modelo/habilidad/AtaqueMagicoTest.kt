package ar.edu.unq.epers.tactics.spring.modelo.habilidad

import ar.edu.unq.epers.tactics.spring.modelo.Atributos
import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.AtaqueMagico
import ar.edu.unq.epers.tactics.spring.modelo.randomizador.RandomizadorSimulado
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AtaqueMagicoTest {

    private lateinit var aventurero: Aventurero
    private lateinit var aventurero2: Aventurero
    private lateinit var party: Party
    private lateinit var party2: Party
    private lateinit var atacar: AtaqueMagico

    @BeforeEach
    fun setup() {
        party = Party("Team Rocket", "imagen.jpg")
        party2 = Party("Team Enemigo", "imagen.jpg")
        aventurero = Aventurero("Carlos", party, 1, "Imagen.jpg", Atributos(1.0, 1.0, 1.0, 1.0))
        aventurero2 = Aventurero("JoseC", party2, 1, "Imagen.jpg", Atributos(1.0, 1.0, 1.0, 1.0))
    }

    @Test
    fun alEjecutarAtaqueConMagiaOfensivaExitosoElReceptorDisminuyeSuVidaYElEmisorPierdeMana() {
        atacar = AtaqueMagico(
            aventurero2,
            aventurero,
            aventurero.nivel,
            aventurero.getPoderMagico(), RandomizadorSimulado(1)
        )
        aventurero.atributos!!.inteligencia = 40.0
        atacar.ejecutar()

        assertEquals(6.0, atacar.aventureroReceptor.getVida())
    }

    @Test
    fun alEjecutarUnAtaqueConMagiaOfensivaNoExitosoElReceptorNoVeAfectadaSuVida() {
        aventurero2.nivel = 100
        atacar = AtaqueMagico(
            aventurero2,
            aventurero,
            aventurero.nivel,
            aventurero.getPoderMagico(), RandomizadorSimulado(0)
        )

        assertEquals(503.0, atacar.aventureroReceptor.getVida())
    }
}
