package ar.edu.unq.epers.unidad5.dao.result

import org.bson.codecs.pojo.annotations.BsonId

class PrecioPromedio {
    @BsonId
    var codigo: String? = null
    var value = 0.toDouble()

}