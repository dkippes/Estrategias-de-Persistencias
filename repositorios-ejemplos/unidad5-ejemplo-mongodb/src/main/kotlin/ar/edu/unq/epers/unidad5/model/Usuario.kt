package ar.edu.unq.epers.unidad5.model

class Usuario {
    var username: String? = null
    //Se necesita un constructor vacio para que jackson pueda
    //convertir de JSON a este objeto.
    protected constructor() {}
    constructor(username: String?) {
        this.username = username
    }

}