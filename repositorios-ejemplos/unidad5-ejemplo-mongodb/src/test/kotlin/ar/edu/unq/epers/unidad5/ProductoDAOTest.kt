package ar.edu.unq.epers.unidad5

import ar.edu.unq.epers.unidad5.dao.ProductoDAO
import ar.edu.unq.epers.unidad5.dao.result.PrecioPromedio
import ar.edu.unq.epers.unidad5.model.Precio
import ar.edu.unq.epers.unidad5.model.Producto
import ar.edu.unq.epers.unidad5.model.Usuario
import ar.edu.unq.epers.unidad5.model.Zona
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.function.Consumer
import java.util.stream.IntStream

class ProductoDAOTest {
    lateinit var dao: ProductoDAO

    @Before
    fun setup() {
        dao = ProductoDAO()
        val usuarios =  listOf(Usuario("Pepe"), Usuario("Juan"), Usuario("Cosme"))
        val zonas = listOf(
            Zona("Bernal", "Saens Peña", "3550", "Argentina"),
            Zona("Quilmes", "Rivadavia", "435", "Argentina"),
            Zona("Ciudad De Buenos Aires", "Callao", "1500", "Argentina")
        )
        val productos = listOf(
            Producto(CODIGO_PRODUCTO_1, "Triple de chocolate", "Capitan del Espacio"),
            Producto(CODIGO_PRODUCTO_2, "Dulce de leche 500 grs", "La Serenisima"),
            Producto(CODIGO_PRODUCTO_3, "Sprite 2.25lts", "Cocacola"),
            Producto(CODIGO_PRODUCTO_4, "Express x3", "Terrabusi")
        )
        IntStream.range(0, 1000).forEach { i: Int ->
            productos.forEach(Consumer { p: Producto ->
                zonas.forEach(Consumer { z: Zona? ->
                    usuarios.forEach(
                        Consumer { u: Usuario? ->
                            val precio = Precio(z, u, i + p.codigo.toInt(), p)
                            p.addPrecio(precio)
                        }
                    )
                })
            })
        }
        dao.save(productos)
    }

    @After
    fun dropAll() {
        dao.deleteAll()
    }

    @Test
    fun saveAndGetByCode() {
        val zonaUS = Zona("Amazon St.", "1024", "Dellaware", "USA")
        val zonaUK = Zona("Amazon Rd.", "1024", "London", "UK")
        val user = Usuario("claudio")
        val producto = Producto("0001", "Longboard", "Santa Cruz")
        producto.addPrecio(Precio(zonaUS, user, 78, producto))
        producto.addPrecio(Precio(zonaUK, user, 82, producto))
        dao.save(producto)
        val producto2 = dao.getByCode(producto.codigo)
        Assert.assertEquals("0001", producto2!!.codigo)
        Assert.assertEquals("Longboard", producto2.nombre)
        Assert.assertEquals("Santa Cruz", producto2.marca)
        Assert.assertEquals(2, producto2.precios.size.toLong())
    }

    @Test
    fun findByMarca() {
        val productos = dao.getByMarca("Terrabusi")
        Assert.assertEquals(1, productos.size.toLong())
        val producto = productos[0]
        Assert.assertEquals("444", producto!!.codigo)
        Assert.assertEquals("Express x3", producto.nombre)
        Assert.assertEquals("Terrabusi", producto.marca)
    }

    @Test
    fun findByPrecio() {
        var productos = dao!!.getByPrecio(446)
        Assert.assertEquals(
            "Todos los productos deben tener algún precio igual a 446",
            4,
            productos!!.size.toLong()
        )
        productos = dao!!.getByPrecio(112)
        Assert.assertEquals(
            "Solo el primer producto debe tener precios menores a 222",
            1,
            productos!!.size.toLong()
        )
    }

    @Test
    fun findByPrecioInRange() {
        var productos: List<Producto?> = dao!!.getByRangoPrecio(100, 500)
        Assert.assertEquals(
            "Todos los productos deben tener algún precio entre 100 y 500",
            4,
            productos.size.toLong()
        )
        productos = dao!!.getByRangoPrecio(100, 250)
        Assert.assertEquals(
            "Solo los dos primeros producto debe ntener precios entre 100 y 250",
            2,
            productos.size.toLong()
        )
    }

    @Test
    fun findByPrecioAndZona() {
        val zonaUS = Zona("Amazon St.", "1024", "Dellaware", "USA")
        val zonaUK = Zona("Amazon Rd.", "1024", "London", "UK")
        val user = Usuario("claudio")
        val producto1 = Producto("0001", "Longboard", "Santa Cruz")
        producto1.addPrecio(Precio(zonaUS, user, 78, producto1))
        producto1.addPrecio(Precio(zonaUK, user, 82, producto1))
        dao.save(producto1)
        val producto2 = Producto("0002", "Skateboard", "Santa Cruz")
        producto2.addPrecio(Precio(zonaUS, user, 60, producto2))
        producto2.addPrecio(Precio(zonaUK, user, 62, producto2))
        dao.save(producto2)
        val productos = dao.getPorPrecioEnZona(80, zonaUK)
        Assert.assertEquals("Solo el Skateboard es mas barato en que 80 en UK", 1, productos.size.toLong())
    }

    @Test
    fun testPrecioPromedio() {
        val precios: List<PrecioPromedio> = dao.getPrecioPromedio(listOf(CODIGO_PRODUCTO_1, CODIGO_PRODUCTO_2))

        Assert.assertEquals(CODIGO_PRODUCTO_2, precios[0].codigo)
        Assert.assertEquals(721.5, precios[0].value, 0.toDouble())
        Assert.assertEquals(CODIGO_PRODUCTO_1, precios[1].codigo)
        Assert.assertEquals(610.5, precios[1].value, 0.toDouble())
    }


    companion object {
        private const val CODIGO_PRODUCTO_1 = "111"
        private const val CODIGO_PRODUCTO_2 = "222"
        private const val CODIGO_PRODUCTO_3 = "333"
        private const val CODIGO_PRODUCTO_4 = "444"
    }
}