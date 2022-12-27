package ar.edu.unq.epers.tactics.spring.modelo.randomizador

import kotlin.random.Random.Default.nextInt


class RandomizadorSimulado(private val dado: Int) : Random {
    override fun lanzarDados(): Int = dado
}