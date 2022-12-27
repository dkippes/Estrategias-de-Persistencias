package ar.edu.unq.epers.tactics.spring.modelo.randomizador

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RandomTest {
    private lateinit var random: Random

    @Test
    fun numeroRandomDel1Al20() {
        random = Randomizador()
        assertTrue(random.lanzarDados() in 1..20)
    }

    @Test
    fun numeroRandomSimulado() {
        random = RandomizadorSimulado(20)
        assertEquals(random.lanzarDados(), 20)
    }
}