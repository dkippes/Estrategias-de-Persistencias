package ar.edu.unq.epers.tactics.spring.modelo

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.DateFormat
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.time.LocalDate
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@Document(indexName = "mercado", shards = 1, replicas = 0, createIndex = true, refreshInterval = "1s")
class Item {
    @Id
    var id: String? = null

    @NotBlank
    var nombre: String? = null

    @Min(1)
    var precio: Double? = null

    @Min(1)
    var stock: Int? = null
    var owner: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Field(type = FieldType.Date, pattern = ["dd-MM-yyyy"], format = [DateFormat.basic_date])
    var fecha: LocalDate? = null
    var descripcion: String? = null
    var mercadoEnvio: Boolean? = null
    var pickUpInStore: Boolean? = null

    @Valid
    var opiniones: MutableList<Opinion>? = mutableListOf()

    constructor(
        id: String?,
        nombre: String?,
        precio: Double?,
        stock: Int?,
        owner: String?,
        fecha: LocalDate?,
        descripcion: String?,
        mercadoEnvio: Boolean?,
        pickUpInStore: Boolean?
    ) {
        this.id = id
        this.nombre = nombre
        this.precio = precio
        this.stock = stock
        this.owner = owner
        this.fecha = fecha
        this.descripcion = descripcion
        this.mercadoEnvio = mercadoEnvio
        this.pickUpInStore = pickUpInStore
    }

    fun agregarOpinion(op: Opinion) {
        opiniones?.add(op)
    }
}