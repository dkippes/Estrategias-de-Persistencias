package ar.edu.unq.epers.tactics.spring.service.impl.spring.neo4j

import ar.edu.unq.epers.tactics.spring.modelo.*
import ar.edu.unq.epers.tactics.spring.modelo.excepcion.MutiplePeleaException
import ar.edu.unq.epers.tactics.spring.modelo.excepcion.SinManaException
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.Ataque
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.AtaqueMagico
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.HabilidadNula
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.Meditacion
import ar.edu.unq.epers.tactics.spring.modelo.randomizador.RandomizadorSimulado
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.Accion
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.Criterio
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.TipoDeEstadistica
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.TipoDeReceptor
import ar.edu.unq.epers.tactics.spring.service.exception.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(PER_CLASS)
class PeleaServiceSpringImplTest {
    lateinit var party: Party

    @Autowired
    lateinit var peleaService: PeleaServiceSpringImpl

    @Autowired
    lateinit var partyService: PartyServiceSpringImpl

    @BeforeEach
    fun setup() {
        party = Party("Diego", "imagen")
    }

    @Test
    fun unaPartyEmpiezaUnaPelea() {
        val partyCreada = partyService.crear(party)
        val pelea = peleaService.iniciarPelea(partyCreada.id!!, "Los cafres")
        assertTrue(peleaService.estaEnPelea(partyCreada.id!!))
    }

    @Test
    fun unaPartyNoPuedePelearSiEstaPeleando() {
        val partyCreada = partyService.crear(party)
        val pelea = peleaService.iniciarPelea(partyCreada.id!!, "Los cafres")
        assertThrows<MutiplePeleaException> { peleaService.iniciarPelea(partyCreada.id!!, "Los cafres") }
    }

    @Test
    fun unaPartyQueNoExisteNoPuedeEmpezarUnaPelea() {
        assertThrows<PartyNotFoundException> { peleaService.iniciarPelea(-1, "Los cafres") }
    }

    @Test
    fun unaAventureroResuelveSuTurnoYLeDaMeditacion() {
        val partyCreada = partyService.crear(party)
        val aventurero = Aventurero("Gandalff", partyCreada, 1, "img", Atributos(1.0, 1.0, 1.0, 1.0))
        aventurero.tacticas = listOf(
            Tactica(
                1,
                Criterio.MAYOR_QUE,
                TipoDeEstadistica.VIDA,
                5.0,
                TipoDeReceptor.UNO_MISMO,
                Accion.MEDITAR
            )
        )
        partyService.agregarAventureroAParty(partyCreada.id!!, aventurero)
        val pelea = peleaService.iniciarPelea(partyCreada.id!!, "Los borbotones")
        val habilidad = peleaService.resolverTurno(pelea.id!!, aventurero.id!!, listOf())
        assertTrue(habilidad is Meditacion)
    }

    @Test
    fun alResolverElTurnoElAventureroNoTieneMana() {
        val partyCreada = partyService.crear(party)
        val aventurero = Aventurero("Gandalff", partyCreada, 1, "img", Atributos(1.0, 1.0, 1.0, 1.0))
        aventurero.tacticas = listOf(
            Tactica(
                1,
                Criterio.MAYOR_QUE,
                TipoDeEstadistica.VIDA,
                5.0,
                TipoDeReceptor.UNO_MISMO,
                Accion.ATAQUE_MAGICO
            )
        )
        partyService.agregarAventureroAParty(partyCreada.id!!, aventurero)
        val pelea = peleaService.iniciarPelea(partyCreada.id!!, "Los borbotones")
        assertThrows<SinManaException> { peleaService.resolverTurno(pelea.id!!, aventurero.id!!, listOf()) }
    }

    @Test
    fun unaAventureroResuelveSuTurnoYNoTieneHabilidadParaTirar() {
        val partyCreada = partyService.crear(party)
        val aventurero = Aventurero("Gandalff", partyCreada, 1, "img", Atributos(1.0, 1.0, 1.0, 1.0))
        aventurero.tacticas =
            listOf(Tactica(1, Criterio.IGUAL, TipoDeEstadistica.VIDA, 5.0, TipoDeReceptor.ENEMIGO, Accion.MEDITAR))
        partyService.agregarAventureroAParty(partyCreada.id!!, aventurero)
        val pelea = peleaService.iniciarPelea(partyCreada.id!!, "Los borbotones")
        val habilidad = peleaService.resolverTurno(pelea.id!!, aventurero.id!!, listOf())
        assertTrue(habilidad is HabilidadNula)
    }

    @Test
    fun unaPeleaEsTerminadaYResultaGanador() {
        val partyCreada = partyService.crear(party)
        val pelea = peleaService.iniciarPelea(partyCreada.id!!, "Los borbotones")
        val aventurero = Aventurero("Gandalff", partyCreada, 1, "img", Atributos(1.0, 1.0, 1.0, 1.0))
        aventurero.tacticas =
            listOf(Tactica(1, Criterio.IGUAL, TipoDeEstadistica.VIDA, 5.0, TipoDeReceptor.ENEMIGO, Accion.MEDITAR))
        aventurero.vidaPorInteraccion = -1.0
        partyService.agregarAventureroAParty(partyCreada.id!!, aventurero)
        val partyTerminada = peleaService.terminarPelea(pelea.id!!)
        assertFalse(partyTerminada.estaPeleando)
        assertTrue(partyTerminada.aventureros.first().vidaPorInteraccion == 0.0)
        assertTrue(peleaService.recuperar(pelea.id!!).resultado == EstadoResultado.GANADOR)
    }

    @Test
    fun unaPeleaEsTerminadaYResultaPerdedor() {
        val partyCreada = partyService.crear(party)
        val pelea = peleaService.iniciarPelea(partyCreada.id!!, "Los borbotones")
        val aventurero = Aventurero("Gandalff", partyCreada, 1, "img", Atributos(1.0, 1.0, 1.0, 1.0))
        aventurero.tacticas =
            listOf(Tactica(1, Criterio.IGUAL, TipoDeEstadistica.VIDA, 5.0, TipoDeReceptor.ENEMIGO, Accion.MEDITAR))
        aventurero.vidaPorInteraccion = -8.0
        partyService.agregarAventureroAParty(partyCreada.id!!, aventurero)
        peleaService.terminarPelea(pelea.id!!)
        assertTrue(peleaService.recuperar(pelea.id!!).resultado == EstadoResultado.PERDEDOR)
    }

    @Test
    fun unaPeleaQueYaFueTerminadaNoPuedeTerminarse() {
        val partyCreada = partyService.crear(party)
        val pelea = peleaService.iniciarPelea(partyCreada.id!!, "Los borbotones")
        peleaService.terminarPelea(pelea.id!!)
        assertThrows<PeleaTerminadaException> { peleaService.terminarPelea(pelea.id!!) }
    }

    @Test
    fun unaPeleaEsTerminadaYNoSePuedeResolverElTurno() {
        val partyCreada = partyService.crear(party)
        val pelea = peleaService.iniciarPelea(partyCreada.id!!, "Los borbotones")
        peleaService.terminarPelea(pelea.id!!)
        assertThrows<PeleaTerminadaException> { peleaService.resolverTurno(pelea.id!!, 1, listOf()) }
    }

    @Test
    fun unaAventureroQueNoExisteEnLaPeleaNoPuedeResolverSuTurno() {
        val partyCreada = partyService.crear(party)
        val partyCreada2 = partyService.crear(Party("Los cafres", "img"))
        val aventurero = Aventurero("Gandalff", partyCreada, 1, "img", Atributos(1.0, 1.0, 1.0, 1.0))
        partyService.agregarAventureroAParty(partyCreada2.id!!, aventurero)
        val pelea = peleaService.iniciarPelea(partyCreada.id!!, "Los borbotones")
        assertThrows<AventureroNoExisteEnLaPeleaException> {
            peleaService.resolverTurno(
                pelea.id!!,
                aventurero.id!!,
                listOf()
            )
        }
    }

    @Test
    fun unPeleaEsActualizada() {
        val partyCreada = partyService.crear(party)
        val pelea = peleaService.iniciarPelea(partyCreada.id!!, "Los borbotones")
        pelea.partyEnemiga = "Los simulaEPERS"
        val peleaActualizada = peleaService.actualizar(pelea)

        assertEquals(peleaActualizada.partyEnemiga, peleaService.recuperar(peleaActualizada.id!!).partyEnemiga)
    }

    @Test
    fun recuperaLasPeleasPorOrdenamiento() {
        val partyCreada = partyService.crear(party)
        val pelea1 = peleaService.iniciarPelea(partyCreada.id!!, "Los borbotones")
        peleaService.terminarPelea(pelea1.id!!)
        val pelea2 = peleaService.iniciarPelea(partyCreada.id!!, "Los Simuladores")
        peleaService.terminarPelea(pelea2.id!!)
        val paginamiento = peleaService.recuperarOrdenadas(partyCreada.id!!, 0)
        val peleaEsperada = this.peleaService.recuperar(paginamiento.peleas.first().id!!)
        assertTrue(paginamiento.peleas.first().id!! == peleaEsperada.id!!)
        assertTrue(paginamiento.total == 2)
    }

    @Test
    fun unAventureroRecibeUnaHabilidad() {
        val partyCreada = partyService.crear(party)
        val aventurero = Aventurero("Gandalff", partyCreada, 1, "img", Atributos(1.0, 1.0, 1.0, 1.0))
        aventurero.manaPorInteraccion = -3
        val aventureroAgregado = partyService.agregarAventureroAParty(partyCreada.id!!, aventurero)
        peleaService.iniciarPelea(partyCreada.id!!, "Los borbotones")
        val meditacion = Meditacion(aventureroAgregado, aventureroAgregado)
        val aventureroConHabilidad = peleaService.recibirHabilidad(aventureroAgregado.id!!, meditacion)
        assertTrue(aventureroConHabilidad.manaPorInteraccion == -2)
    }

    @Test
    fun unAventureroNoExiste() {
        val partyCreada = partyService.crear(party)
        val aventurero = Aventurero("Gandalff", partyCreada, 1, "img", Atributos(1.0, 1.0, 1.0, 1.0))
        peleaService.iniciarPelea(partyCreada.id!!, "Los borbotones")
        val meditacion = Meditacion(aventurero, aventurero)
        assertThrows<AventureroNotFoundException> { peleaService.recibirHabilidad(-1, meditacion) }
    }

    @Test
    fun unbAventureroNoEstaPeleando() {
        val partyCreada = partyService.crear(party)
        val aventurero = Aventurero("Gandalff", partyCreada, 1, "img", Atributos(1.0, 1.0, 1.0, 1.0))
        aventurero.manaPorInteraccion = -3
        val aventureroAgregado = partyService.agregarAventureroAParty(partyCreada.id!!, aventurero)
        val meditacion = Meditacion(aventureroAgregado, aventureroAgregado)
        assertThrows<AventureroNoPeleandoException> {
            peleaService.recibirHabilidad(
                aventureroAgregado.id!!,
                meditacion
            )
        }
    }

    @Test
    fun recuperarPelea() {
        val partyCreada = partyService.crear(party)
        val pelea = peleaService.iniciarPelea(partyCreada.id!!, "Los simuladores")
        val peleaRecuperada = peleaService.recuperar(pelea.id!!)
        assertEquals(peleaRecuperada.id!!, pelea.id!!)
    }

    @Test
    fun recuperarPeleaInexistente() {
        assertThrows<PeleaNotFoundException> { peleaService.recuperar(-1) }
    }

    @Test
    fun cuandoUnaPartyGanaLosAventureroSubenUnNivelYTienenUnPuntoDeProficienciaAGastar() {
        val partyCreada = partyService.crear(party)
        val pelea = peleaService.iniciarPelea(partyCreada.id!!, "Los borbotones")
        val aventurero = Aventurero("Gandalff", partyCreada, 1, "img", Atributos(1.0, 1.0, 1.0, 1.0))
        aventurero.tacticas =
            listOf(Tactica(1, Criterio.IGUAL, TipoDeEstadistica.VIDA, 5.0, TipoDeReceptor.ENEMIGO, Accion.MEDITAR))
        aventurero.vidaPorInteraccion = -1.0
        partyService.agregarAventureroAParty(partyCreada.id!!, aventurero)
        val partyTerminada = peleaService.terminarPelea(pelea.id!!)
        assertTrue(peleaService.recuperar(pelea.id!!).resultado == EstadoResultado.GANADOR)
        assertTrue(partyTerminada.aventureros.first().nivel == 2)
        assertTrue(partyTerminada.aventureros.first().proficienciaAGastar == 1)
    }

    @Test
    fun unAventureroRecibeUnaHabilidadDeAtaque() {
        val partyCreada = partyService.crear(party)
        val otraParty = partyService.crear(Party("Los Simuladores", "imagen"))

        val unAventurero = Aventurero("Gandalff", partyCreada, 1, "img", Atributos(1.0, 1.0, 1.0, 1.0))
        val otroAventurero = Aventurero("Sauron", otraParty, 1, "img", Atributos(1.0, 1.0, 1.0, 1.0))

        val emisor = partyService.agregarAventureroAParty(partyCreada.id!!, unAventurero)
        val receptor = partyService.agregarAventureroAParty(otraParty.id!!, otroAventurero)

        peleaService.iniciarPelea(partyCreada.id!!, "Los Simuladores")
        peleaService.iniciarPelea(otraParty.id!!, "Diego")
        val ataque =
            Ataque(receptor, emisor, emisor.getPrecisionFisica(), emisor.getDanoFisico(), RandomizadorSimulado(1))
        val aventureroConHabilidad = peleaService.recibirHabilidad(receptor.id!!, ataque)
        assertEquals(aventureroConHabilidad.getVida(), 5.5)
    }

    @Test
    fun unAventureroRecibeUnaHabilidadDeAtaqueMagico() {
        val partyCreada = partyService.crear(party)
        val otraParty = partyService.crear(Party("Los Simuladores", "imagen"))

        val unAventurero = Aventurero("Gandalff", partyCreada, 1, "img", Atributos(1.0, 1.0, 1.0, 1.0))
        val otroAventurero = Aventurero("Sauron", otraParty, 1, "img", Atributos(1.0, 1.0, 1.0, 1.0))

        val emisor = partyService.agregarAventureroAParty(partyCreada.id!!, unAventurero)
        val receptor = partyService.agregarAventureroAParty(otraParty.id!!, otroAventurero)

        peleaService.iniciarPelea(partyCreada.id!!, "Los Simuladores")
        peleaService.iniciarPelea(otraParty.id!!, "Diego")
        val ataqueMagico =
            AtaqueMagico(receptor, emisor, emisor.nivel, emisor.getPoderMagico(), RandomizadorSimulado(1))
        val aventureroConHabilidad = peleaService.recibirHabilidad(receptor.id!!, ataqueMagico)
        assertEquals(aventureroConHabilidad.getVida(), 6.0)
        assertEquals(emisor.getMana(), 2.0)
    }

    @AfterEach
    fun tearDown() {
        peleaService.clearAll()
    }
}