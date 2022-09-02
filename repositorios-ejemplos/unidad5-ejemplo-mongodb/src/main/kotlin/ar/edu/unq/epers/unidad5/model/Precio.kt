package ar.edu.unq.epers.unidad5.model

import org.bson.codecs.pojo.annotations.BsonIgnore

class Precio {
    var zona: Zona? = null
    var user: Usuario? = null
    var precio = 0
    @BsonIgnore var producto: Producto? = null

    //Se necesita un constructor vacio para que jackson pueda
    //convertir de JSON a este objeto.
    protected constructor() {}
    constructor(zona: Zona?, user: Usuario?, precio: Int, producto: Producto?) {
        this.zona = zona
        this.user = user
        this.precio = precio
        this.producto = producto
    }
}