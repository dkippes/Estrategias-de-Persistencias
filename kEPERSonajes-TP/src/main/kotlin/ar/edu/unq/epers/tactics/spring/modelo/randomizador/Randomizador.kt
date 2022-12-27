package ar.edu.unq.epers.tactics.spring.modelo.randomizador

import kotlin.random.Random.Default.nextInt


class Randomizador : Random {
    override fun lanzarDados(): Int = nextInt(1, 20)
}