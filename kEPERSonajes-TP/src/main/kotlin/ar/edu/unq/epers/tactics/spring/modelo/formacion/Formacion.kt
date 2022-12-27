package ar.edu.unq.epers.tactics.spring.modelo.formacion

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document(collection = "Formacion")
class Formacion {
    @MongoId
    var id: ObjectId? = null

    var nombre: String? = null
    var requisitosFormacion: List<Requerimiento>? = listOf()
    var atributosFormacion: List<AtributoDeFormacion>? = listOf()

    constructor() {}

    constructor(
        nombre: String,
        requisitosFormacion: List<Requerimiento>,
        atributosFormacion: List<AtributoDeFormacion>
    ) {
        this.nombre = nombre
        this.requisitosFormacion = requisitosFormacion
        this.atributosFormacion = atributosFormacion
    }
}
