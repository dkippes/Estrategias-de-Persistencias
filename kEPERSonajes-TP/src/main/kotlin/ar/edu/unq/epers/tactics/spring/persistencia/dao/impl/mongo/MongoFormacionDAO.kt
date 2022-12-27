package ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.mongo

import ar.edu.unq.epers.tactics.spring.modelo.formacion.AtributoDeFormacion
import ar.edu.unq.epers.tactics.spring.modelo.formacion.Formacion
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MongoFormacionDAO : MongoRepository<Formacion, String> {

    @Aggregation(pipeline = [
        "{\$match: {nombre: {\$in: ?0}}}",
        "{\$unwind: '\$atributosFormacion'}",
        "{\$project: {atributos: '\$atributosFormacion'}}",
        "{\$group: {_id: '\$atributos.tipo', bono: {\$sum: '\$atributos.bono'}}}",
        "{\$addFields: { tipo: '\$_id'}}",
        "{\$sort: { '_id': 1 }}",
    ])
    fun atributosDeLaFormacion(formaciones: List<String>): List<AtributoDeFormacion>
}
