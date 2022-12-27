package ar.edu.unq.epers.tactics.spring.modelo.habilidad

import ar.edu.unq.epers.tactics.spring.modelo.Atributos
import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.Meditacion
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MeditarTest {
    private lateinit var aventurero: Aventurero
    private lateinit var aventurero2: Aventurero
    private lateinit var party: Party
    private lateinit var meditar: Meditacion

    @BeforeEach
    fun setup() {
        party = Party("Team Rocket", "imagen.jpg")
        aventurero = Aventurero("Rodolfo", party, 1, "Imagen.jpg", Atributos(1.0, 1.0, 10.0, 10.0))
        aventurero2 = Aventurero("Carlos", party, 10, "Imagen.jpg", Atributos(1.0, 1.0, 1.0, 10.0))
    }

    @Test
    fun alMeditarUnAventureroIncrementaSuMana() {
        aventurero.atributos?.inteligencia = 5.0
        val manaDelEmisorPreviaALaMeditacion = aventurero.getMana()
        meditar = Meditacion(aventurero, aventurero)
        meditar.ejecutar()

        assertEquals(
            manaDelEmisorPreviaALaMeditacion + meditar.aventureroEmisor!!.nivel,
            meditar.aventureroEmisor!!.getMana()
        )
    }

    @Test
    fun alMeditarUnAventureroIncrementaSuManaSinSuperarElMaximo() {
        meditar = Meditacion(aventurero2, aventurero2)
        val manaDelEmisorPreviaALaMeditacion = meditar.aventureroEmisor!!.getMana()
        meditar.ejecutar()

        assertTrue(aventurero2.manaMaximo < manaDelEmisorPreviaALaMeditacion + meditar.aventureroEmisor!!.nivel)
        assertEquals(aventurero2.manaMaximo, aventurero2.getMana())
    }
}