package ar.edu.unq.epers.tactics.spring.modelo.formacion

import ar.edu.unq.epers.tactics.spring.modelo.Atributo
import org.bson.codecs.pojo.annotations.BsonId

class AtributoDeFormacion {

    @BsonId
    var _id: String? = null
    var tipo: Atributo? = null
    var bono: Int? = null

    constructor() {
    }

    constructor(tipo: Atributo, bono: Int) {
        this.tipo = tipo
        this.bono = bono
    }
}
