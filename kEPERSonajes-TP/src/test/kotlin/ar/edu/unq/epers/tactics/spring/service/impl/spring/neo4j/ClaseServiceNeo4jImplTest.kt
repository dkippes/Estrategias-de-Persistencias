package ar.edu.unq.epers.tactics.spring.service.impl.spring.neo4j

import ar.edu.unq.epers.tactics.spring.modelo.Atributo
import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.modelo.clases.Clase
import ar.edu.unq.epers.tactics.spring.modelo.clases.Mejora
import ar.edu.unq.epers.tactics.spring.service.exception.*
import helpers.Factory
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ClaseServiceNeo4jImplTest {
    @Autowired
    lateinit var claseService: ClaseServiceSpringImpl

    @Autowired
    lateinit var partyService: PartyServiceSpringImpl

    var mejora: Mejora? = null

    @BeforeEach
    fun setUp() {
        claseService.deleteAll()
        val aventurero = "Aventurero"
        val fisico = "Fisico"
        val mago = "Mago"
        val magico = "Magico"
        val guerrero = "Guerrero"
        val paladin = "Paladin"
        val clerigo = "Clerigo"
        mejora = Mejora(Clase(paladin), 3, listOf(Atributo.INTELIGENCIA))
        claseService.crearClase(aventurero)
        claseService.crearClase(mago)
        claseService.crearClase(magico)
        claseService.crearClase(fisico)
        claseService.crearClase(guerrero)
        claseService.crearClase(paladin)
        claseService.crearClase(clerigo)
        claseService.crearMejora(aventurero, fisico, listOf(Atributo.FUERZA), 2)
        claseService.crearMejora(aventurero, magico, listOf(Atributo.INTELIGENCIA), 2)
        claseService.crearMejora(magico, fisico, listOf(Atributo.CONSTITUCION), 1)
        claseService.crearMejora(magico, mago, listOf(Atributo.INTELIGENCIA), 4)
        claseService.crearMejora(fisico, clerigo, listOf(Atributo.DESTREZA, Atributo.CONSTITUCION), 2)
        claseService.crearMejora(fisico, guerrero, listOf(Atributo.CONSTITUCION, Atributo.FUERZA), 2)
        claseService.crearMejora(magico, clerigo, listOf(Atributo.DESTREZA, Atributo.INTELIGENCIA), 2)
        claseService.crearMejora(clerigo, paladin, listOf(Atributo.CONSTITUCION, Atributo.FUERZA), 3)
        claseService.crearMejora(clerigo, mago, listOf(Atributo.CONSTITUCION, Atributo.INTELIGENCIA), 2)
        claseService.crearMejora(clerigo, guerrero, listOf(Atributo.CONSTITUCION, Atributo.FUERZA), 2)
        claseService.crearMejora(guerrero, paladin, listOf(Atributo.CONSTITUCION), 6)
        claseService.requerir(paladin, guerrero)
        claseService.requerir(paladin, clerigo)
    }

    @Test
    fun crearUnaClase() {
        val nombreDeClase = "Aventurero"
        val recuperado = claseService.recuperar(nombreDeClase)
        assertEquals(recuperado.nombre, nombreDeClase)
    }

    @Test
    fun noPuedoCrearUnaClaseYaExistente() {
        val nombreDeClase = "Aventurero"
        assertThrows<ClaseCannotCreateException> { claseService.crearClase(nombreDeClase) }
    }

    @Test
    fun noPuedoRecuperarUnaClaseNoExistente() {
        val nombreDeClase = "Guardian"
        assertThrows<ClaseNotFoundException> { claseService.recuperar(nombreDeClase) }
    }

    @Test
    fun seCreaUnaRelacionMejoraEntreClases() {
        val aventurero = "Aventurero"
        val magico = "Magico"

        val aventureroRecuperado = claseService.recuperar(aventurero)
        val magicoRecuperado = claseService.recuperar(magico)

        assertTrue(aventureroRecuperado.existeLaRelacionDeMejora(magicoRecuperado))
    }

    @Test
    fun noSePuedeCrearUnaRelacionMejoraConLaMismaClase() {
        val aventurero = "Aventurero"
        assertThrows<RelacionNoPuedeSerElMismoNodoException> {
            claseService.crearMejora(
                aventurero,
                aventurero,
                listOf(Atributo.INTELIGENCIA),
                3
            )
        }
    }

    @Test
    fun seActualizaLaRelacionMejoraSiEstaYaExiste() {
        val aventurero = "Aventurero"
        val magico = "Magico"
        claseService.crearMejora(aventurero, magico, listOf(Atributo.FUERZA), 3)
        val aventureroRecuperado = claseService.recuperar(aventurero)
        val magicoRecuperado = claseService.recuperar(magico)

        assertTrue(aventureroRecuperado.mejora!!.first().bonoPorAtributo == 3)
        assertTrue(aventureroRecuperado.mejora!!.first().atributos.first() == Atributo.FUERZA)
        assertTrue(aventureroRecuperado.existeLaRelacionDeMejora(magicoRecuperado))
    }

    @Test
    fun seCreaLaRelacionRequerir() {
        val aventurero = "Aventurero"
        val guerrero = "Guerrero"
        claseService.requerir(aventurero, guerrero)

        val aventureroRecuperado = claseService.recuperar(aventurero)
        val guerreroRecuperado = claseService.recuperar(guerrero)

        assertTrue(aventureroRecuperado.existeLaRelacionDeRequerimiento(guerreroRecuperado))
    }

    @Test
    fun noSePuedeCrearUnaRelacionRequerimientoSobreLaMismaClase() {
        val aventurero = "Aventurero"
        assertThrows<RelacionNoPuedeSerElMismoNodoException> { claseService.requerir(aventurero, aventurero) }
    }

    @Test
    fun noSePuedeCrearUnaRelacionBidireccionalDeRequiereEntre2Clases() {
        val aventurero = "Aventurero"
        val guerrero = "Guerrero"
        claseService.requerir(aventurero, guerrero)

        assertThrows<RelacionBidireccionalException> { claseService.requerir(guerrero, aventurero) }
    }

    @Test
    fun noSePuedeCrearOtraRelacionRequiereSiYaExisteSobreUnaClaseAOtra() {
        val aventurero = "Aventurero"
        val guerrero = "Guerrero"
        claseService.requerir(aventurero, guerrero)

        assertThrows<YaExisteLaRelacionException> { claseService.requerir(aventurero, guerrero) }
    }

    @Test
    fun aventureroPuedeGanarProficiencia() {
        val aventurero = "Aventurero"
        val magico = "Magico"
        val party = partyService.crear(Party("Los Simuladores", "img"))
        val aventureroAgregado = Factory.crearAventurero("Ada")
        aventureroAgregado.proficienciaAGastar = 2
        val aventureroCreado = partyService.agregarAventureroAParty(party.id!!, aventureroAgregado)

        val aventureroProficienciado = claseService.ganarProficiencia(aventureroCreado.id!!, aventurero, magico)

        assertAll(
            { assertEquals(aventureroProficienciado.clases.last(), magico) },
            {
                assertEquals(
                    aventureroProficienciado.atributos!!.inteligencia,
                    aventureroCreado.atributos!!.inteligencia + 2.0
                )
            })
    }

    @Test
    fun aventureroNoPuedeGanarProficienciaCuandoNoTieneProfienciaParaGastar() {
        val aventurero = "Aventurero"
        val magico = "Magico"
        val party = partyService.crear(Party("Los Simuladores", "img"))
        val aventureroAgregado = Factory.crearAventurero("Ada")
        val aventureroCreado = partyService.agregarAventureroAParty(party.id!!, aventureroAgregado)

        assertThrows<AventureroNoTieneProficienciaParaGastar> {
            claseService.ganarProficiencia(
                aventureroCreado.id!!,
                aventurero,
                magico
            )
        }
    }

    @Test
    fun aventureroNoPuedeGanarProficienciaCuandoNoExisteLasClasesPasadas() {
        val aventurero = "Aventurero"
        val guerrero = "Guerrero"
        val party = partyService.crear(Party("Los Simuladores", "img"))
        val aventureroAgregado = Factory.crearAventurero("Ada")
        aventureroAgregado.proficienciaAGastar = 2
        val aventureroCreado = partyService.agregarAventureroAParty(party.id!!, aventureroAgregado)
        assertThrows<ClaseNotFoundException> {
            claseService.ganarProficiencia(
                aventureroCreado.id!!,
                aventurero,
                guerrero
            )
        }
    }

    @Test
    fun aventureroNoPuedeGanarProficienciaCuandoNoPuedeMejorar() {
        val guerrero = "Guerrero"
        val party = partyService.crear(Party("Los Simuladores", "img"))
        val aventureroAgregado = Factory.crearAventurero("Ada")
        aventureroAgregado.clases.add("Guerrero")
        aventureroAgregado.proficienciaAGastar = 2
        val aventureroCreado = partyService.agregarAventureroAParty(party.id!!, aventureroAgregado)
        assertThrows<RelacionNoExisteException> {
            claseService.ganarProficiencia(
                aventureroCreado.id!!,
                guerrero,
                "Paladin"
            )
        }
    }

    @Test
    fun aventureroNoPuedeGanarProficienciaCuandoNoTieneLaClaseDePartida() {
        val guerrero = "Guerrero"
        val party = partyService.crear(Party("Los Simuladores", "img"))
        val aventureroAgregado = Factory.crearAventurero("Ada")
        aventureroAgregado.proficienciaAGastar = 2
        val aventureroCreado = partyService.agregarAventureroAParty(party.id!!, aventureroAgregado)
        assertThrows<AventureroNoTieneClaseException> {
            claseService.ganarProficiencia(
                aventureroCreado.id!!,
                guerrero,
                "Paladin"
            )
        }
    }

    @Test
    fun aventureroNoPuedeGanarProficienciaCuandoNoExisteMejoraEntreLasClasesPasadas() {
        val aventurero = "Aventurero"
        val guerrero = "Guerrero"
        val party = partyService.crear(Party("Los Simuladores", "img"))
        val aventureroAgregado = Factory.crearAventurero("Ada")
        aventureroAgregado.proficienciaAGastar = 2
        val aventureroCreado = partyService.agregarAventureroAParty(party.id!!, aventureroAgregado)
        assertThrows<ClaseNotFoundException> {
            claseService.ganarProficiencia(
                aventureroCreado.id!!,
                aventurero,
                guerrero
            )
        }
    }


    @Test
    fun unAventureroPuedeSaberSuCaminoMasRentable() {
        val aventurero = "Aventurero"
        val magico = "Magico"
        val clerigo = "Clerigo"
        val guerrero = "Guerrero"
        val paladin = "Paladin"

        val party = partyService.crear(Party("Ejemplo", "Ammm"))
        val aventureroSinPersistir = Factory.crearAventurero("Alfa").apply { proficienciaAGastar = 5 }
        val aventureroPersistido = partyService.agregarAventureroAParty(
            party.id!!,
            aventureroSinPersistir
        )
        val mejoras =
            claseService.caminoMasRentable(aventureroPersistido.id!!, Atributo.INTELIGENCIA, aventurero, paladin)
        val mejorasSinCamino =
            claseService.caminoMasRentable(aventureroPersistido.id!!, Atributo.DESTREZA, aventurero, magico)

        assertEquals(mejoras.map { it.claseAMejorar.nombre }, listOf(paladin, guerrero, clerigo, magico))
        assertTrue(mejorasSinCamino.isEmpty())
    }


    @Test
    fun puedeMejorarAGuerreroPorqueEsAventurero() {
        val aventurero = "Aventurero"
        val guerrero = "Guerrero"
        val mejora = Mejora(Clase(guerrero), 3, listOf(Atributo.INTELIGENCIA))
        claseService.crearMejora(aventurero, guerrero, mejora.atributos, mejora.bonoPorAtributo)
        val party = partyService.crear(Party("Los Simuladores", "img"))
        val aventureroAgregado = Factory.crearAventurero("Ada")
        val aventureroCreado = partyService.agregarAventureroAParty(party.id!!, aventureroAgregado)

        assertTrue(claseService.puedeMejorar(aventureroCreado.id!!, mejora))
    }

    @Test
    fun noPuedeMejorarPorqueYaEsLaClaseAventurero() {
        val aventurero = "Aventurero"
        val guerrero = "Guerrero"
        val mejora = Mejora(Clase(aventurero), 3, listOf(Atributo.INTELIGENCIA))
        claseService.crearMejora(aventurero, guerrero, mejora.atributos, mejora.bonoPorAtributo)
        val party = partyService.crear(Party("Los Simuladores", "img"))
        val aventureroAgregado = Factory.crearAventurero("Ada")
        val aventureroCreado = partyService.agregarAventureroAParty(party.id!!, aventureroAgregado)

        assertFalse(claseService.puedeMejorar(aventureroCreado.id!!, mejora))
    }

    @Test
    fun noPuedeMejorarPorqueNoAlcanzaLaMejoraCorrespondiente() {
        val paladin = "Paladin"
        val mejora = Mejora(Clase(paladin), 3, listOf(Atributo.INTELIGENCIA))
        val party = partyService.crear(Party("Los Simuladores", "img"))
        val aventureroAgregado = Factory.crearAventurero("Ada")
        val aventureroCreado = partyService.agregarAventureroAParty(party.id!!, aventureroAgregado)

        assertFalse(claseService.puedeMejorar(aventureroCreado.id!!, mejora))
    }

    @Test
    fun noPuedeMejorarAPaladinPorqueRequiereQueSeaClerigo() {
        val party = partyService.crear(Party("Los Simuladores", "img"))
        val aventureroAgregado = Factory.crearAventurero("Ada")
        aventureroAgregado.clases.add("Fisico")
        aventureroAgregado.clases.add("Guerrero")
        val aventureroCreado = partyService.agregarAventureroAParty(party.id!!, aventureroAgregado)

        assertFalse(claseService.puedeMejorar(aventureroCreado.id!!, mejora!!))
    }

    @Test
    fun encontrarPosiblesMejorasDeUnaClase() {
        val party = Party("Diego", "imagen")
        val partyCreada = partyService.crear(party)
        val aventurero = Factory.crearAventurero("Ada")
        val aventureroAgregado = partyService.agregarAventureroAParty(partyCreada.id!!, aventurero)

        val magico = "Magico"
        val fisico = "Fisico"
        val mejorasPosibles = claseService.posiblesMejoras(aventureroAgregado.id!!)
        assertEquals(2, mejorasPosibles.size)
        assertTrue(mejorasPosibles.map { it.claseAMejorar.nombre }.contains(magico))
        assertTrue(mejorasPosibles.map { it.claseAMejorar.nombre }.contains(fisico))
    }

    @Test
    fun unaClasePuedeNoTenerMejoras() {
        val party = Party("Diego", "imagen")
        val partyCreada = partyService.crear(party)
        val aventurero = Factory.crearAventurero("Ada")
        aventurero.clases.add("Magico")
        aventurero.clases.add("Fisico")
        aventurero.clases.add("Clerigo")
        aventurero.clases.add("Guerrero")
        aventurero.clases.add("Mago")
        aventurero.clases.add("Paladin")
        val aventureroAgregado = partyService.agregarAventureroAParty(partyCreada.id!!, aventurero)
        val mejorasPosibles = claseService.posiblesMejoras(aventureroAgregado.id!!)
        assertTrue(mejorasPosibles.isEmpty())
    }

    @Test
    fun noSePuedeObtenerPosiblesMejorasParaUnAventureroQueNoExiste() {
        val idAventurero = -1
        assertThrows<AventureroNotFoundException> { claseService.posiblesMejoras(idAventurero.toLong()) }
    }

    @AfterEach
    fun cleanUp() {
        claseService.deleteAll()
    }
}