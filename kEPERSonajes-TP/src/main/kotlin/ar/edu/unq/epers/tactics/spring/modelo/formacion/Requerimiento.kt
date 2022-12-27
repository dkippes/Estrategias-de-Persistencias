package ar.edu.unq.epers.tactics.spring.modelo.formacion


class Requerimiento {
    var clase: String? = null
    var cantidadDeAventureros: Int = 0

    constructor() {}

    constructor(clase: String, cantidadDeAventureros: Int) {
        this.clase = clase
        this.cantidadDeAventureros = cantidadDeAventureros
    }
}