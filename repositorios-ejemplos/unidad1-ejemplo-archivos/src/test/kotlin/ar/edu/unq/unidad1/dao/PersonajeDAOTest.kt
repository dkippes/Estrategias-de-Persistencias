package ar.edu.unq.unidad1.dao

import ar.edu.unq.unidad1.modelo.Item
import ar.edu.unq.unidad1.modelo.Personaje
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS


/**
 * TODO: Esta es una implementacion sencilla que tiene como objeto
 * mostrar algunas formas de persistir en archivos (usando distintas
 * herramientas para guardar en distintos formatos)
 *
 * Entre todas las distintas implementaciones de test el codigo es
 * exactamente el mismo. Se mantiene asi solo para mostrar el funcionamiento
 * sin demasiada complicacion, pero el codigo repetido NO deberia existir.
 *
 */
@TestInstance(PER_CLASS)
abstract class PersonajeDAOTest {
    private val dao: PersonajeDAO
    lateinit var maguito: Personaje

    init {
        dao = createDAO()
    }

    @BeforeAll
    fun crearModelo() {
        maguito = Personaje("Maguito")
        maguito.pesoMaximo = 15
        maguito.vida = 198
        maguito.xp = 2500
        maguito.recoger(Item("Tunica gris", 1))
        maguito.recoger(Item("Baculo gris", 5))
    }

    @Test
    fun al_guardar_y_luego_recuperar_se_obtiene_objetos_similares() {
        dao.guardar(maguito)

        //Los personajes son iguales
        val otroMaguito = dao.recuperar("Maguito")
        Assertions.assertEquals(maguito.nombre, otroMaguito!!.nombre)
        Assertions.assertEquals(maguito.pesoMaximo, otroMaguito.pesoMaximo)
        Assertions.assertEquals(maguito.vida, otroMaguito.vida)
        Assertions.assertEquals(maguito.xp, otroMaguito.xp)
        Assertions.assertEquals(maguito.inventario.size.toLong(), otroMaguito.inventario.size.toLong()
        )

        //Pero no son el mismo objeto =(
        //A esto nos referimos con "perdida de identidad"
        Assertions.assertTrue(maguito != otroMaguito)
    }

    abstract fun createDAO(): PersonajeDAO
}