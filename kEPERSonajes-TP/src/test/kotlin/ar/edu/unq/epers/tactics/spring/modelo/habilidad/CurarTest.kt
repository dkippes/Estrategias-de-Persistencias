package ar.edu.unq.epers.tactics.spring.modelo.habilidad

import ar.edu.unq.epers.tactics.spring.modelo.Atributos
import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.Curacion
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CurarTest {
    private lateinit var aventurero: Aventurero
    private lateinit var aventurero2: Aventurero
    private lateinit var party: Party
    private lateinit var curar: Curacion

    @BeforeEach
    fun setup() {
        party = Party("Team Rocket", "imagen.jpg")
        aventurero = Aventurero("Carlos", party, 1, "Imagen.jpg", Atributos(1.0, 1.0, 1.0, 1.0))
        aventurero2 = Aventurero("Rodolfo", party, 10, "Imagen.jpg", Atributos(1.0, 1.0, 1.0, 1.0))
        party.agregarAventurero(aventurero)
        party.agregarAventurero(aventurero2)
    }

    @Test
    fun alCurarAUnAventureroEsteIncrementaSuVidaSinSuperarElMaximo() {
        curar = Curacion(aventurero2, aventurero, aventurero.getPoderMagico())
        curar.ejecutar()

        assertEquals(aventurero2.vidaMaxima, aventurero2.getVida())
    }

    @Test
    fun alCurarAUnAventureroEsteIncrementaSuVida() {
        curar = Curacion(aventurero2, aventurero, aventurero.getPoderMagico())
        aventurero2.nivel = 2
        val vidaDelReceptorPreviaALaCuracion = aventurero2.getVida()
        curar.ejecutar()

        assertEquals(vidaDelReceptorPreviaALaCuracion + curar.poderMagicoEmisor, aventurero2.getVida())
    }
}
