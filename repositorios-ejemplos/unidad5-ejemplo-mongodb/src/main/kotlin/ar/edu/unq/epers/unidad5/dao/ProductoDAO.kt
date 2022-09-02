package ar.edu.unq.epers.unidad5.dao

import ar.edu.unq.epers.unidad5.dao.result.PrecioPromedio
import ar.edu.unq.epers.unidad5.model.Producto
import ar.edu.unq.epers.unidad5.model.Zona
import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.Projections

class ProductoDAO : GenericMongoDAO<Producto>(Producto::class.java) {
    fun getByMarca(marca: String): List<Producto?> {
        return findEq("marca", marca)
    }

    fun getByCode(code: String): Producto? {
        return getBy("codigo",code)
    }

    fun getByPrecio(precio: Int): List<Producto?> {
        return findEq("precios.precio", precio)
    }

    fun getByRangoPrecio(min: Int, max: Int): List<Producto> {
        // Esta query escrita de esta forma solamente asegura que existe algun objeto
        // en precios cuyo precio es mayor que X y que existe algun objeto en precios
        // cuyo preico es menor que Y, no necesariamente X e Y son el mismo objeto.
        // return this.find("{ precios.precio: { $gt: #, $lt: #} }", min, max);

		return this.find(and(gt("precios.precio", min), lt("precios.precio", max)))
    }

    fun getPorPrecioEnZona(max: Int, zona: Zona?): List<Producto> {
        return this.find(and(gte("precios.precio", max), eq("precios.zona", zona)))
    }

    fun getPrecioPromedio(codigos: List<String>): List<PrecioPromedio> {
        // match:   primero selecciona los items
        // unwind:  luego convierte una lista de n productos con m precios cada uno en una lista de n*m productos con 1 precio cada uno
        // project:  solo se queda con el codigo y el precios.precio de cada producto
        // group:    agrupa por codigo (recordemos que despues del unwind codigo no es unico sino hay m productos con el mismo codigo
        //                y diferentes precios).  Dentro de cada grupo calcula el promedio del campo precios.precio
        // sort:       ordena el resultado por _id (codigo) de forma ascendiente

        val match = Aggregates.match(`in`("codigo", codigos))
        val unwind = Aggregates.unwind("\$precios")
        val project = Aggregates.project(Projections.fields(Projections.include("codigo"), Projections.include("precios.precio")))
        val group = Aggregates.group("\$codigo", Accumulators.avg("value", "\$precios.precio"))
        val sort = Aggregates.sort(Indexes.descending("value"))

        return aggregate(listOf(match, unwind, project, group, sort), PrecioPromedio::class.java)
    }
}