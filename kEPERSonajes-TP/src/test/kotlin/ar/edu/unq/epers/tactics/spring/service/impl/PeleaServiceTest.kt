package ar.edu.unq.epers.tactics.spring.service.impl

import ar.edu.unq.epers.tactics.spring.modelo.Atributos
import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.modelo.Tactica
import ar.edu.unq.epers.tactics.spring.modelo.excepcion.MutiplePeleaException
import ar.edu.unq.epers.tactics.spring.modelo.excepcion.NoEstaPeleandoException
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.Ataque
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.AtaqueMagico
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.Defensa
import ar.edu.unq.epers.tactics.spring.modelo.randomizador.RandomizadorSimulado
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.Accion
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.Criterio
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.TipoDeEstadistica
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.TipoDeReceptor
import ar.edu.unq.epers.tactics.spring.service.AventureroService
import ar.edu.unq.epers.tactics.spring.service.PartyService
import ar.edu.unq.epers.tactics.spring.service.PeleaService
import helpers.DataDAO
import helpers.Factory
import org.junit.jupiter.api.*
import javax.persistence.EntityNotFoundException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class PeleaServiceTest {
    lateinit var peleaService: PeleaService
    lateinit var partyService: PartyService
    lateinit var aventureroService: AventureroService


    @Test
    fun unaPartyIniciaUnaPelea() {
        val party = partyService.crear(Party("Team Rocket", "imagen.jpg"))
        val peleaActual = peleaService.iniciarPelea(party.id!!, "party enemiga")

        Assertions.assertEquals(peleaActual.party, party)
    }

    @Test
    fun recuperarUnaPelea() {
        val party = partyService.crear(Party("Team Rocket", "imagen.jpg"))
        val peleaActual = peleaService.iniciarPelea(party.id!!, "party enemiga")
        val peleaRecuperada = peleaService.recuperar(peleaActual.id!!)

        assertAll(
            { Assertions.assertEquals(peleaActual.id!!, peleaRecuperada.id) },
            { Assertions.assertEquals(peleaActual.party!!, peleaRecuperada.party) }
        )
    }

    @Test
    fun actualizarUnaPelea() {
        val party = partyService.crear(Party("Team Rocket", "imagen.jpg"))
        val party2 = partyService.crear(Party("Team Rocket", "imagen.jpg"))
        val peleaActual = peleaService.iniciarPelea(party.id!!, "party enemiga")
        peleaActual.party = party2
        peleaService.actualizar(peleaActual)
        val peleaRecuperada = peleaService.recuperar(peleaActual.id!!)

        assertAll(
            { Assertions.assertEquals(peleaActual.id!!, peleaRecuperada.id) },
            { Assertions.assertEquals(peleaActual.party!!, peleaRecuperada.party) }
        )
    }

    @Test
    fun dosPartysIniciaUnaPeleaDistinta() {
        val party = partyService.crear(Party("Team Rocket", "imagen.jpg"))
        val party2 = partyService.crear(Party("Juanitos", "imagen2.jpg"))
        val pelea1Actual = peleaService.iniciarPelea(party.id!!, "party enemiga")
        val pelea2Actual = peleaService.iniciarPelea(party2.id!!, "party enemiga")

        assertAll(
            { Assertions.assertEquals(pelea1Actual.party, party) },
            { Assertions.assertEquals(pelea2Actual.party, party2) }
        )
    }

    @Test
    fun unaPartyNoPuedeIniciarUnaPeleaQueYaInicio() {
        val party = partyService.crear(Party("Team Rocket", "imagen.jpg"))
        peleaService.iniciarPelea(party.id!!, "party enemiga")

        assertThrows<MutiplePeleaException>("Una party no puede empezar otra pelea si esta peleando") {
            peleaService.iniciarPelea(
                party.id!!, "party enemiga"
            )
        }
    }

    @Test
    fun unaPartyQueNoExisteNoPuedeIniciarUnaPelea() {
        assertThrows<EntityNotFoundException> { peleaService.iniciarPelea(20, "party enemiga") }
    }

    @Test
    fun puedoSaberSiUnaPartyEstaEnUnaPelea() {
        val party1 = partyService.crear(Party("River", "River.jpg"))
        val party2 = partyService.crear(Party("Boca", "Boca.jpg"))
        val party3 = partyService.crear(Party("Independiente", "Independiente.jpg"))

        peleaService.iniciarPelea(party1.id!!, "party enemiga")
        peleaService.iniciarPelea(party2.id!!, "party enemiga")

        assertAll(
            { Assertions.assertTrue(peleaService.estaEnPelea(party1.id!!)) },
            { Assertions.assertTrue(peleaService.estaEnPelea(party2.id!!)) },
            { Assertions.assertFalse(peleaService.estaEnPelea(party3.id!!)) }
        )
    }

    @Test
    fun unaPartyQueNoExisteNoPuedeTerminarUnaPelea() {

        assertThrows<EntityNotFoundException> { peleaService.terminarPelea(20) }
    }

    @Test
    fun unaPartyQueNoEstaEnPeleaNoPuedeTerminarUnaPelea() {
        val party = partyService.crear(Party("Team Rocket", "imagen.jpg"))

        assertThrows<NoEstaPeleandoException> { peleaService.terminarPelea(party.id!!) }
    }


    @Test
    fun alTerminarUnaPeleaLaPartyInvolucradaVuelveASuEstadoInicial() {
        val party = partyService.crear(Party("Team Rocket", "imagen.jpg"))
        val party2 = partyService.crear(Party("Team Enemigo", "imagen2.jpg"))
        val aventureroPrePelea = partyService.agregarAventureroAParty(
            party.id!!,
            Aventurero("Carlos", null, 1, "imagenb", Atributos(2.0, 3.0, 4.0, 2.0))
        )

        val aventurero2PrePelea = partyService.agregarAventureroAParty(
            party2.id!!,
            Factory.crearAventurero("Carlos", null, 2, "imagenb", 2.0, 3.0, 4.0, 5.0)
        )


        val vidaEsperadaReceptor = aventureroPrePelea.vidaMaxima
        val manaEsperadoEmisor = aventurero2PrePelea.manaMaximo


        peleaService.iniciarPelea(party.id!!, "party enemiga")
        val ataque = Ataque(
            aventureroPrePelea,
            aventurero2PrePelea,
            aventurero2PrePelea.getPrecisionFisica(),
            aventurero2PrePelea.getDanoFisico(),
            RandomizadorSimulado(2)
        )

        peleaService.recibirHabilidad(aventureroPrePelea.id!!, ataque)


        peleaService.terminarPelea(party.id!!)

        val aventureroPostPelea = aventureroService.recuperar(aventureroPrePelea.id!!)
        val aventurero2PostPelea = aventureroService.recuperar(aventurero2PrePelea.id!!)

        Assertions.assertFalse(aventureroPostPelea.party!!.estaPeleando)
        Assertions.assertEquals(aventureroPostPelea.getVida(), vidaEsperadaReceptor)
        Assertions.assertEquals(aventurero2PostPelea.getMana(), manaEsperadoEmisor)

    }


    @Test
    fun unAventureroPuedeRecibirUnaHabilidad() {
        val partyPersistida = partyService.crear(Party("Boca", "dsfdsfsd"))
        val partyEnemiga = partyService.crear(Party("River", "sdasd"))
        val emisorPersistido = partyService.agregarAventureroAParty(
            partyPersistida.id!!,
            Factory.crearAventurero("Valentin", null, 50, "Img.jpg", 1.0, 15.0, 1.0, 1.0)
        )
        val receptorEnemigo =
            partyService.agregarAventureroAParty(
                partyEnemiga.id!!,
                Factory.crearAventurero("Leo", null, 1, "Im2g.jpg", 1.0, 15.0, 1.0, 1.0)
            )

        val vidaEsperada = receptorEnemigo.getVida() - emisorPersistido.getPoderMagico()
        val manaEsperado = emisorPersistido.getMana() - 5

        val habilidad = AtaqueMagico(
            receptorEnemigo,
            emisorPersistido,
            emisorPersistido.nivel,
            emisorPersistido.getPoderMagico(),
            RandomizadorSimulado(2)
        )
        val receptorGolpeadoPersistido = peleaService.recibirHabilidad(receptorEnemigo.id!!, habilidad)
        val emisorPersistidoDespuesDeHabilidad = aventureroService.recuperar(emisorPersistido.id!!)

        assertAll(
            { Assertions.assertEquals(vidaEsperada, receptorGolpeadoPersistido.getVida()) },
            { Assertions.assertEquals(manaEsperado, emisorPersistidoDespuesDeHabilidad.getMana()) }
        )
    }

    @Test
    fun unAventureroDefiendeAOtroYRecibirElDaño() {
        val partyPersistida = partyService.crear(Party("Boca", "dsfdsfsd"))
        val partyEnemiga = partyService.crear(Party("River", "sdasd"))
        val emisorPersistido = partyService.agregarAventureroAParty(
            partyPersistida.id!!,
            Factory.crearAventurero("Valentin", null, 50, "Img.jpg", 1.0, 15.0, 1.0, 1.0)
        )
        val defendido =
            partyService.agregarAventureroAParty(
                partyEnemiga.id!!,
                Factory.crearAventurero("Leo", null, 1, "Im2g.jpg", 1.0, 15.0, 1.0, 1.0)
            )

        val habilidad = Defensa(defendido, emisorPersistido)
        val defendidoDespuesDeSerDefendido = peleaService.recibirHabilidad(defendido.id!!, habilidad)
        val defensorGolpeado = aventureroService.recuperar(emisorPersistido.id!!)

        assertAll(
            { Assertions.assertEquals(defendidoDespuesDeSerDefendido.defensor!!.id, defensorGolpeado.id) }
        )
    }

    @Test
    fun puedoResolverElTurnoDeUnaPelea() {
        val party = partyService.crear(Party("Hola", "ADASDSAD"))
        val partyEnemiga = partyService.crear(Party("Aloha", "ADsASDSAD"))
        val aventurero = partyService.agregarAventureroAParty(party.id!!, Factory.crearAventurero("Gohan"))
        val pelea = peleaService.iniciarPelea(partyEnemiga.id!!, "party enemiga")

        aventurero.agregarTactica(
            Tactica(
                1,
                Criterio.MAYOR_QUE, TipoDeEstadistica.VIDA, 50.0, TipoDeReceptor.ENEMIGO, Accion.ATAQUE_MAGICO
            )
        )

        aventureroService.actualizar(aventurero)

        val enemigos = listOf(
            partyService.agregarAventureroAParty(
                partyEnemiga.id!!, Factory.crearAventurero(
                    "Vin",
                    null, 5, "Vin.png", 100.0, 23.0, 100.0, 100.0
                )
            ),
            partyService.agregarAventureroAParty(
                partyEnemiga.id!!, Factory.crearAventurero(
                    "Dockson",
                    null, 15, "Vin.png", 50.0, 100.0, 78.0, 23.0
                )
            )
        )

        val habilidad = peleaService.resolverTurno(
            pelea.id!!,
            aventurero.id!!,
            enemigos
        )

        Assertions.assertTrue(habilidad is AtaqueMagico)
    }

    @AfterEach
    abstract fun cleanup()
}
