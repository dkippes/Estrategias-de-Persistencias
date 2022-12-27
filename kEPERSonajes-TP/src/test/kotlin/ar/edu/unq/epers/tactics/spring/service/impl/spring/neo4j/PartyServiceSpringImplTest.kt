package ar.edu.unq.epers.tactics.spring.service.impl.spring.neo4j

import ar.edu.unq.epers.tactics.spring.modelo.Atributos
import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.service.Direccion
import ar.edu.unq.epers.tactics.spring.service.Orden
import ar.edu.unq.epers.tactics.spring.service.exception.PartyNotFoundException
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
class PartyServiceSpringImplTest {
    lateinit var party: Party
    lateinit var partyCreada: Party
    lateinit var partyCreada2: Party
    lateinit var partyCreada3: Party
    lateinit var partyCreada4: Party
    lateinit var partyCreada5: Party
    lateinit var partyCreada6: Party
    lateinit var partyCreada7: Party
    lateinit var partyCreada8: Party
    lateinit var partyCreada9: Party
    lateinit var partyCreada10: Party
    lateinit var partyCreada11: Party

    @Autowired
    lateinit var partyService: PartyServiceSpringImpl

    @BeforeEach
    fun setup() {
        party = Party("Diego", "imagen")
        partyCreada = partyService.crear(party)

        val party2 = Party("Los pepes2", "imagen")
        partyCreada2 = partyService.crear(party2)

        val party3 = Party("Los pepes3", "imagen")
        partyCreada3 = partyService.crear(party3)

        val party4 = Party("Los pepes4", "imagen")
        partyCreada4 = partyService.crear(party4)

        val party5 = Party("Los pepes5", "imagen")
         partyCreada5 = partyService.crear(party5)

        val party6 = Party("Los pepes6", "imagen")
        partyCreada6 = partyService.crear(party6)

        val party7 = Party("Los pepes7", "imagen")
       partyCreada7 = partyService.crear(party7)

        val party8 = Party("Los pepes3", "imagen")
        partyCreada8 = partyService.crear(party8)

        val party9 = Party("Los pepes9", "imagen")
        partyCreada9 = partyService.crear(party9)

        val party10 = Party("Los pepes10", "imagen")
        partyCreada10 = partyService.crear(party10)

        val party11 = Party("Los pepes11", "imagen")
        partyCreada11 = partyService.crear(party11)
    }

    @Test
    fun unaPartySeCrea() {
        val partyCreada = partyService.crear(party)
        assertNotNull(partyCreada.id)
        assertTrue(partyService.recuperar(partyCreada.id!!) == partyCreada)
    }

    @Test
    fun seActualizaUnaParty() {
    //    val partyCreada = partyService.crear(party)
        partyCreada.nombre = "Los juanes"
        val partyActualizada = partyService.actualizar(party)
        assertNotNull(partyActualizada.id)
    }

    @Test
    fun noSePuedeActualizarUnaPartyQueNoExiste() {
        party.id = 1
        assertThrows<PartyNotFoundException> { partyService.actualizar(party) }
    }

    @Test
    fun seRecuperaUnaParty() {
        val partyRecuperada = partyService.recuperar(partyCreada.id!!)
        assertEquals(partyCreada, partyRecuperada)
    }

    @Test
    fun recuperaTodasLasPartysCuandoNoTienePartys() {
        partyService.clearAll()
        val partysRecuperadas = partyService.recuperarTodas()
        assertTrue(partysRecuperadas.isEmpty())
    }

    @Test
    fun recuperaTodasLasPartysCuando() {
        partyService.crear(party)
        val partysRecuperadas = partyService.recuperarTodas()
        assertFalse(partysRecuperadas.isEmpty())
    }

    @Test
    fun agregarAventureroAParty() {
        val aventurero = Aventurero("Lucas", partyCreada, 1, "imagen", Atributos(1.0, 1.0, 1.0, 1.0))
        val aventureroAgregado = partyService.agregarAventureroAParty(partyCreada.id!!, aventurero)

        assertTrue(aventureroAgregado.party == partyCreada)
    }

    @Test
    fun recuperarOrdenadasPorPoder() {

        val aventurero = Aventurero("Lucas", partyCreada, 1, "imagen", Atributos(1.0, 1.0, 1.0, 1.0))
        partyService.agregarAventureroAParty(partyCreada2.id!!, aventurero)

        val partyOrdenadas = partyService.recuperarOrdenadas(Orden.PODER, Direccion.DESCENDENTE, 0)
        assertEquals(partyOrdenadas.parties.first(), partyCreada2)
        assertTrue(partyOrdenadas.total == 11)
    }

    @Test
    fun recuperarOrdenadasPorVictorias() {

        val aventurero = Aventurero("Lucas", partyCreada, 1, "imagen", Atributos(1.0, 1.0, 1.0, 1.0))
        partyService.agregarAventureroAParty(partyCreada2.id!!, aventurero)

        val partyOrdenadas = partyService.recuperarOrdenadas(Orden.VICTORIAS, Direccion.ASCENDENTE, 0)
        assertEquals(partyOrdenadas.parties.first(), partyCreada)
        assertTrue(partyOrdenadas.total == 11)
    }

    @Test
    fun recuperarOrdenadasPorDerrotas() {


        val aventurero = Aventurero("Lucas", partyCreada, 1, "imagen", Atributos(1.0, 1.0, 1.0, 1.0))
        partyService.agregarAventureroAParty(partyCreada2.id!!, aventurero)


        val partyOrdenadas = partyService.recuperarOrdenadas(Orden.DERROTAS, Direccion.ASCENDENTE, 0)
        assertEquals(partyOrdenadas.parties.first(), partyCreada)
        assertTrue(partyOrdenadas.total == 11)
    }

    @Test
    fun recuperarParty() {
        val partyCreada = partyService.crear(party)
        val partyRecuperada = partyService.recuperar(partyCreada.id!!)
        assertEquals(partyRecuperada, partyCreada)
    }

    @Test
    fun recuperarPartyQueNoExiste() {
        assertThrows<PartyNotFoundException> { partyService.recuperar(-1) }
    }

    @AfterEach
    fun tearDown() {
        partyService.clearAll()
    }
}
