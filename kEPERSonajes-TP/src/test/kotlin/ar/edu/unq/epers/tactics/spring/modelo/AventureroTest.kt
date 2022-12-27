package ar.edu.unq.epers.tactics.spring.modelo

import ar.edu.unq.epers.tactics.spring.modelo.habilidades.AtaqueMagico
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.Habilidad
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.HabilidadNula
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.Accion
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.Criterio
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.TipoDeEstadistica
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.TipoDeReceptor
import helpers.Factory.crearAventurero
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows

class AventureroTest {
    private lateinit var aventurero: Aventurero
    private lateinit var party: Party

    @BeforeEach
    fun setup() {
        party = Party("Team Rocket", "imagen.jpg")
        aventurero = crearAventurero("Rodolfo", party)
    }

    @Test
    fun crearAventurero() {
        assertAll(
            { assertEquals(aventurero.id, null) },
            { assertEquals(aventurero.nombre, "Rodolfo") },
            { assertEquals(aventurero.party, party) },
            { assertEquals(aventurero.nivel, 1) },
            { assertEquals(aventurero.atributos?.fuerza, 1.0) },
            { assertEquals(aventurero.atributos?.destreza, 1.0) },
            { assertEquals(aventurero.atributos?.inteligencia, 1.0) },
            { assertEquals(aventurero.atributos?.constitucion, 1.0) },
            { assertEquals(aventurero.tacticas.size, 0) },
            { assertEquals(aventurero.imagenUrl, "Ejemplo.jpg") },
            { assertEquals(aventurero.vidaMaxima, 8.0) },
            { assertEquals(aventurero.manaMaximo, 2.0) }
        )
    }

    @Test
    fun noSePuedeCrearAventureroConMasDe100DeFuerza() {
        assertThrows<Exception> { crearAventurero("Rodolfo", party, 1, "Ejemplo.jpg", 101.0) }
    }

    @Test
    fun noSePuedeCrearAventureroConMenosDe1DeFuerza() {
        assertThrows<Exception> { crearAventurero("Rodolfo", party, 1, "Ejemplo.jpg", 0.0) }
    }

    @Test
    fun noSePuedeCrearAventureroConMasDe100DeDestreza() {
        assertThrows<Exception> { crearAventurero("Rodolfo", party, 1, "Ejemplo.jpg", 95.0, 101.0) }
    }

    @Test
    fun noSePuedeCrearAventureroConMenosDe1DeDestreza() {
        assertThrows<Exception> { crearAventurero("Rodolfo", party, 1, "Ejemplo.jpg", 95.0, 0.0) }
    }

    @Test
    fun noSePuedeCrearAventureroConMasDe100DeOInteligencia() {
        assertThrows<Exception> { crearAventurero("Rodolfo", party, 1, "Ejemplo.jpg", 95.0, 95.0, 101.0) }
    }

    @Test
    fun noSePuedeCrearAventureroConMenosDe1DeInteligencia() {
        assertThrows<Exception> { crearAventurero("Rodolfo", party, 1, "Ejemplo.jpg", 95.0, 95.0, 0.0) }
    }

    @Test
    fun noSePuedeCrearAventureroConMasDe100DeOConstitucion() {
        assertThrows<Exception> { crearAventurero("Rodolfo", party, 1, "Ejemplo.jpg", 95.0, 95.0, 95.0, 101.0) }
    }

    @Test
    fun noSePuedeCrearAventureroConMenosDe1DeConstitucion() {
        assertThrows<Exception> { crearAventurero("Rodolfo", party, 1, "Ejemplo.jpg", 95.0, 95.0, 95.0, 0.0) }
    }

    @Test
    fun seAgregaUnaTacticaAlAventurero() {
        val tactica = Tactica(
            1,
            Criterio.MAYOR_QUE,
            TipoDeEstadistica.ARMADURA,
            15.0,
            TipoDeReceptor.ENEMIGO,
            Accion.ATAQUE_MAGICO
        )
        aventurero.agregarTactica(tactica)
        assertEquals(aventurero.tacticas[0], tactica)
    }

    @Test
    fun puedoObtenerLasTacticasOrdenadasPorPrioridad() {
        aventureroConTacticas()

        val (tactica1, tactica2) = aventurero.tacticasPorPrioridad()

        assertAll({
            assertEquals(tactica1.prioridad, 1)
        }, { assertEquals(tactica2.prioridad, 2) })
    }

    private fun aventureroConTacticas() {
        val tacticaDeAtaqueMagico = Tactica(
            2,
            Criterio.MAYOR_QUE,
            TipoDeEstadistica.ARMADURA,
            76.0,
            TipoDeReceptor.ENEMIGO,
            Accion.ATAQUE_MAGICO
        )
        val tacticaDeCura = Tactica(
            1,
            Criterio.MENOR_QUE,
            TipoDeEstadistica.VIDA,
            10.0,
            TipoDeReceptor.ALIADO,
            Accion.CURAR
        )

        aventurero.agregarTactica(tacticaDeCura)
        aventurero.agregarTactica(tacticaDeAtaqueMagico)
    }

    @Test
    fun aventureroObtieneEstadisticas() {
        val aventurero = crearAventurero("Rodolfo", party, 1, "Ejemplo.jpg", 10.0, 25.0, 5.0, 75.0)

        assertAll(
            { assertEquals(aventurero.getVida(), 165.0) },
            { assertEquals(aventurero.getArmadura(), 76.0) },
            { assertEquals(aventurero.getMana(), 6.0) },
            { assertEquals(aventurero.getVelocidad(), 26.0) },
            { assertEquals(aventurero.getDanoFisico(), 23.5) },
            { assertEquals(aventurero.getPoderMagico(), 6.0) },
            { assertEquals(aventurero.getPrecisionFisica(), 36.0) }
        )
    }

    @Test
    fun puedoObtenerLosAliadosDelAventurero() {
        val otroAventurero = crearAventurero("Pepin", party)
        party.agregarAventurero(otroAventurero)

        val aliados = aventurero.aliadosDelAventurero()
        assertAll({ assertTrue(aliados.contains(otroAventurero)) },
            { assertEquals(aliados.size, 1) }
        )
    }

    @Test
    fun cuandoNoHayTacticasValidasLanzaUnaExcepcion() {
        val tacticaDeCura = Tactica(
            1,
            Criterio.MENOR_QUE,
            TipoDeEstadistica.VIDA,
            10.0,
            TipoDeReceptor.ALIADO,
            Accion.DEFENDER
        )
        val enemigos = obtenerEnemigos()

        aventurero.agregarTactica(tacticaDeCura)

        assertTrue(aventurero.resolverTactica(enemigos) is HabilidadNula)
    }

    @Test
    fun puedoElegirUnaTacticaQueMeDeUnaHabilidadSegunLosEnemigos() {
        aventureroConTacticas()
        val enemigos = obtenerEnemigos()
        aventurero.manaPorInteraccion = 5
        val habilidad: Habilidad = aventurero.resolverTactica(enemigos)

        assertTrue(habilidad is AtaqueMagico)
    }

    private fun obtenerEnemigos(): List<Aventurero> {
        val partyDelEnemigo = Party("Banda de kelsier", "Image2n.jpg")

        val aventurerosEnemigos = listOf(
            Aventurero(
                "Kelsier",
                partyDelEnemigo, 14, "Kelsier.png", Atributos(100.0, 100.0, 100.0, 30.0)
            ),
            Aventurero(
                "Vin",
                partyDelEnemigo, 5, "Vin.png", Atributos(100.0, 100.0, 100.0, 78.0)
            )
        )
        return aventurerosEnemigos
    }
}