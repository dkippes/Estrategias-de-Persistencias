package ar.edu.unq.epers.tactics.spring.service.impl.elastic

import ar.edu.unq.epers.tactics.spring.modelo.Item
import ar.edu.unq.epers.tactics.spring.modelo.Opinion
import ar.edu.unq.epers.tactics.spring.service.MercadoService
import ar.edu.unq.epers.tactics.spring.service.impl.elasticSearch.MercadoServiceImpl
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate


@ExtendWith(SpringExtension::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MercadoServiceTest {
    @Autowired
    lateinit var mercadoService: MercadoServiceImpl
    lateinit var espadaJuramentada : Item
    lateinit var espadaSoleada : Item

    init {
        espadaJuramentada = Item(
            null,
            "Espada Esquirldada Juramentada",
            200.9,
            1,
            "Dalinar Kholin",
            LocalDate.now(),
            "Es una de las armas más poderosas provenientes del planeta Roshar.",
            true,
            false
        )
        espadaSoleada = Item(
            null,
            "Espada Esquirldada Soleada",
            100.9,
            2,
            "Elhokar Kholin",
            LocalDate.now(),
            "Es una de las armas más poderosas provenientes del planeta Roshar perteneciente al fallecido rey alezi Gavilar Kholin.",
            false,
            false
        )
    }

    @Test
    fun puedoGuardarUnItemEnElMercado() {
        val itemPersistido = mercadoService.guardarItem(espadaJuramentada)

        Assertions.assertTrue(itemPersistido.id !== null)
        Assertions.assertEquals(itemPersistido.nombre, espadaJuramentada.nombre)
    }

    @Test
    fun puedoRecuperarUnItemEnElMercado() {
        val itemPersistido = mercadoService.guardarItem(espadaJuramentada)

        val itemRecuperado = mercadoService.recuperarItem(itemPersistido.id!!)

        Assertions.assertEquals(itemRecuperado.id, itemPersistido.id)
        Assertions.assertEquals(itemPersistido.nombre, itemRecuperado.nombre)
    }

    @Test
    fun puedoRecuperarTodosLosItemsDelMercado() {
        mercadoService.guardarItem(espadaJuramentada)
        mercadoService.guardarItem(espadaSoleada)

        val items = mercadoService.recuperarTodosLosItems()
        val (item1, item2) = items

        Assertions.assertEquals(items.size, 2)
        Assertions.assertEquals(item1.nombre, espadaJuramentada.nombre)
        Assertions.assertEquals(item2.nombre, espadaSoleada.nombre)
    }

    @Test
    fun puedoBorrarUnItemGuardado() {
        val item = mercadoService.guardarItem(espadaJuramentada)

        mercadoService.borraItem(item.id!!)

        assertThrows<RuntimeException> { mercadoService.recuperarItem(item.id!!) }
    }

    @Test
    fun puedoRecuperarItemsDeUsuario() {
        val itemPersistido = mercadoService.guardarItem(espadaJuramentada)

        val (item1) = mercadoService.recuperarItemsDeUsuario(espadaJuramentada.owner!!)

        Assertions.assertEquals(item1.nombre, itemPersistido.nombre)
        Assertions.assertEquals(item1.id, itemPersistido.id)
        Assertions.assertEquals(item1.owner, itemPersistido.owner )
    }

    @Test
    fun puedoRecuperarPorNombreYDescripcion() {
        mercadoService.guardarItem(espadaJuramentada)
        val itemPersistido2 = mercadoService.guardarItem(espadaSoleada)

        val (resultado1) = mercadoService.recuperarPorNombreYDescripcion("Espada", "Gavilar")

        Assertions.assertEquals(itemPersistido2.id, resultado1.id)
    }

    @Test
    fun puedoRecuperarPorItemsQueTieneMercadoEnvioOPuis() {
        mercadoService.guardarItem(espadaJuramentada)
        mercadoService.guardarItem(espadaSoleada)

        val (item1) = mercadoService.recuperarPorItemsQueTieneMercadoEnvioOPuis()

        Assertions.assertTrue(item1.mercadoEnvio!!)
        Assertions.assertFalse(item1.pickUpInStore!!)
    }

    @Test
    fun puedoRecuperarElItemConMayorPuntaje() {
        espadaJuramentada.agregarOpinion(Opinion("Muy buena", "Dalinar Kholin", 10))
        espadaJuramentada.agregarOpinion(Opinion("Muy nefasta", "Torol Sadeas", 1))

        espadaSoleada.agregarOpinion(Opinion("Decente", "Dalinar Kholin", 5))


        val juramentada = mercadoService.guardarItem(espadaJuramentada)
        mercadoService.guardarItem(espadaSoleada)

        val item = mercadoService.recuperarItemConMayorPuntaje()

        Assertions.assertEquals(item.nombre, juramentada.nombre)
        Assertions.assertEquals(item.id, juramentada.id)
    }

    @Test
    fun puedoRecuperarElItemConMenorPuntaje() {
        espadaJuramentada.agregarOpinion(Opinion("Muy buena", "Dalinar Kholin", 10))
        espadaJuramentada.agregarOpinion(Opinion("Muy nefasta", "Torol Sadeas", 1))

        espadaSoleada.agregarOpinion(Opinion("Decente", "Dalinar Kholin", 5))

        mercadoService.guardarItem(espadaJuramentada)
        val soleada = mercadoService.guardarItem(espadaSoleada)

        val item = mercadoService.recuperarItemConMenorPuntaje()

        Assertions.assertEquals(item.nombre, soleada.nombre)
        Assertions.assertEquals(item.id, soleada.id)
    }

    @Test
    fun puedoRecuperarGananciaTotalEsperadaDeUnOwner() {

        val espadaTormenta = Item(
            null,
            "Espada Esquirldada Tormenta",
            50.9,
            2,
            "Elhokar Kholin",
            LocalDate.now(),
            "Es una de las armas más poderosas provenientes del planeta Roshar perteneciente al fallecido rey alezi Gavilar Kholin.",
            false,
            false
        )
        mercadoService.guardarItem(espadaJuramentada)
        mercadoService.guardarItem(espadaTormenta)
        mercadoService.guardarItem(espadaSoleada)

        val item = mercadoService.recuperarGananciaTotalEsperadaDeUnOwner("Elhokar")

        Assertions.assertEquals(item.owner, "Elhokar")
        Assertions.assertEquals(item.ganancias.toInt(), espadaTormenta.precio?.plus(espadaSoleada.precio!!)!!.toInt())
    }



    @BeforeEach
    @AfterEach
    fun cleanup() {
        mercadoService.borrarTodo()
    }
}