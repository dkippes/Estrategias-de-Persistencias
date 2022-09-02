package ar.edu.unq.epers.unidad5.model

import org.bson.codecs.pojo.annotations.BsonProperty

class Producto {
    @BsonProperty("id")
    val id: String? = null
    lateinit var codigo: String
    var nombre: String? = null
    var marca: String? = null
    var precios: MutableList<Precio> = mutableListOf()

    //Se necesita un constructor vacio para que jackson pueda
    //convertir de JSON a este objeto.
    protected constructor() {
    }
    constructor(codigo: String, nombre: String, marca: String) {
        this.codigo = codigo
        this.nombre = nombre
        this.marca = marca
    }

    fun addPrecio(precio: Precio) {
        precios.add(precio)
    }

}