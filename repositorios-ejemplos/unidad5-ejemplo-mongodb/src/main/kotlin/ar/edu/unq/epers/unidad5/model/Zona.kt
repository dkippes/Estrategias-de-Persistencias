package ar.edu.unq.epers.unidad5.model

class Zona {
    var calle: String? = null
    var altura: String? = null
    var localidad: String? = null
    var pais: String? = null

    //Se necesita un constructor vacio para que jackson pueda
    //convertir de JSON a este objeto.
    protected constructor() {}
    constructor(calle: String?, altura: String?, localidad: String?, pais: String?) {
        this.localidad = localidad
        this.calle = calle
        this.altura = altura
        this.pais = pais
    }

}