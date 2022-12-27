package ar.edu.unq.epers.tactics.spring.modelo

import ar.edu.unq.epers.tactics.spring.modelo.habilidades.*
import ar.edu.unq.epers.tactics.spring.modelo.randomizador.Randomizador
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.Accion
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.Criterio
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.TipoDeEstadistica
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.TipoDeReceptor
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TacticaTest {
    lateinit var aventurero: Aventurero
    lateinit var aventurerosEnemigos: MutableSet<Aventurero>
    lateinit var aventurerosAliados: MutableSet<Aventurero>

    @BeforeEach
    fun setUp() {
        val partyDelAventurero = Party("El imperio final", "Imagen.jpg")
        val partyDelEnemigo = Party("Banda de kelsier", "Image2n.jpg")
        aventurero = Aventurero(
            "Rashek",
            partyDelAventurero, 5, "Rashek.png", Atributos(100.0, 100.0, 40.0, 100.0)
        )

        aventurerosEnemigos = mutableSetOf(
            Aventurero(
                "Kelsier",
                partyDelEnemigo, 14, "Kelsier.png", Atributos(100.0, 100.0, 100.0, 30.0)
            ),
            Aventurero(
                "Vin",
                partyDelEnemigo, 5, "Vin.png", Atributos(100.0, 100.0, 100.0, 100.0)
            ),
            Aventurero(
                "Dockson",
                partyDelEnemigo, 6, "Dockson.png", Atributos(100.0, 100.0, 100.0, 40.0)
            )
        )

        aventurerosAliados = mutableSetOf(
            Aventurero(
                "Kar",
                partyDelAventurero, 50, "Kar.png", Atributos(100.0, 100.0, 100.0, 100.0)
            ),
            Aventurero(
                "Profundidad",
                partyDelAventurero, 5, "Profundidad.png", Atributos(100.0, 100.0, 100.0, 100.0)
            ),
            Aventurero(
                "Kwann",
                partyDelAventurero, 6, "Kwann.png", Atributos(100.0, 100.0, 100.0, 100.0)
            )
        )

        partyDelEnemigo.aventureros = aventurerosEnemigos
        partyDelAventurero.aventureros = aventurerosAliados
        partyDelAventurero.agregarAventurero(aventurero)

    }

    @Test
    fun puedoObtenerLaHabilidadEsperadaAtaqueMagicoSiSeCumpleElCriterio() {
        val tacticaDeAtaqueMagico = Tactica(
            1,
            Criterio.MAYOR_QUE,
            TipoDeEstadistica.ARMADURA,
            76.0,
            TipoDeReceptor.ENEMIGO,
            Accion.ATAQUE_MAGICO
        )

        val habilidadObtenida = tacticaDeAtaqueMagico.obtenerHabilidad(
            aventurero, aventurerosAliados, aventurerosEnemigos,
            Randomizador()
        )!!

        assertTrue(habilidadObtenida is AtaqueMagico)
    }

    @Test
    fun puedoObtenerLaHabilidadEsperadaMeditarSiSeCumpleElCriterio() {
        val tacticaDeAtaqueMagico = Tactica(
            1,
            Criterio.MENOR_QUE,
            TipoDeEstadistica.MANA,
            1000000.0,
            TipoDeReceptor.UNO_MISMO,
            Accion.MEDITAR
        )

        val habilidadObtenida = tacticaDeAtaqueMagico.obtenerHabilidad(
            aventurero,
            aventurerosAliados,
            aventurerosEnemigos,
            Randomizador()
        )!!

        assertTrue(habilidadObtenida is Meditacion)
    }

    @Test
    fun puedoObtenerLaHabilidadEsperadaAtacarFisicamenteSiSeCumpleElCriterio() {
        val tacticaDeAtaqueMagico = Tactica(
            1,
            Criterio.MENOR_QUE,
            TipoDeEstadistica.VIDA,
            1000.0,
            TipoDeReceptor.ENEMIGO,
            Accion.ATAQUE_FISICO
        )

        val habilidadObtenida = tacticaDeAtaqueMagico.obtenerHabilidad(
            aventurero,
            aventurerosAliados,
            aventurerosEnemigos,
            Randomizador()
        )!!

        assertTrue(habilidadObtenida is Ataque)
    }

    @Test
    fun puedoObtenerLaHabilidadEsperadaDefenderSiSeCumpleElCriterio() {
        val tacticaDeAtaqueMagico = Tactica(
            1,
            Criterio.MAYOR_QUE,
            TipoDeEstadistica.VIDA,
            10.0,
            TipoDeReceptor.ALIADO,
            Accion.DEFENDER
        )

        val habilidadObtenida = tacticaDeAtaqueMagico.obtenerHabilidad(
            aventurero, aventurerosAliados, aventurerosEnemigos,
            Randomizador()
        )!!

        assertTrue(habilidadObtenida is Defensa)
    }

    @Test
    fun puedoObtenerLaHabilidadEsperadaCurarSiSeCumpleElCriterio() {
        val tacticaDeCuracion = Tactica(
            1,
            Criterio.MENOR_QUE,
            TipoDeEstadistica.MANA,
            1000.0,
            TipoDeReceptor.ALIADO,
            Accion.CURAR
        )

        val habilidadObtenida = tacticaDeCuracion.obtenerHabilidad(
            aventurero, aventurerosAliados, aventurerosEnemigos,
            Randomizador()
        )!!

        assertTrue(habilidadObtenida is Curacion)
    }

    @Test
    fun cuandoNoSeCumpleElCriterioRetornaNull() {
        val tacticaDeCura = Tactica(
            2,
            Criterio.MENOR_QUE,
            TipoDeEstadistica.VIDA,
            10.0,
            TipoDeReceptor.ALIADO,
            Accion.CURAR
        )

        val habilidadObtenida = tacticaDeCura.obtenerHabilidad(
            aventurero, aventurerosAliados, aventurerosEnemigos,
            Randomizador()
        )

        assertNull(habilidadObtenida)
    }

    @Test
    fun puedoObtenerElReceptorAdecuadoDependiendoElCriterio() {
        val tacticaDeMeditar = Tactica(
            4,
            Criterio.MENOR_QUE,
            TipoDeEstadistica.MANA,
            4500000.0,
            TipoDeReceptor.UNO_MISMO,
            Accion.MEDITAR
        )

        val receptorAdecuado =
            tacticaDeMeditar.hayReceptorAdecuado(aventurero, aventurerosAliados, aventurerosEnemigos)!!

        assertEquals(receptorAdecuado, aventurero)
    }

    @Test
    fun cuandoNoHayUnReceptorAdecuadoDevuelveFalse() {
        val tacticaDeAtaqueFisico = Tactica(
            2,
            Criterio.MENOR_QUE,
            TipoDeEstadistica.VIDA,
            45.0,
            TipoDeReceptor.ENEMIGO,
            Accion.ATAQUE_FISICO
        )

        val receptorAdecuado =
            tacticaDeAtaqueFisico.hayReceptorAdecuado(aventurero, aventurerosAliados, aventurerosEnemigos)

        assertNull(receptorAdecuado)
    }
}