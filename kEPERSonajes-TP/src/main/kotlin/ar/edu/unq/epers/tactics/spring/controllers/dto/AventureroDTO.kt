package ar.edu.unq.epers.tactics.spring.controllers.dto

import ar.edu.unq.epers.tactics.spring.modelo.Atributos
import ar.edu.unq.epers.tactics.spring.modelo.Aventurero

data class AventureroDTO(
    var id: Long?,
    var nivel: Int,
    var nombre: String,
    var imagenURL: String,
    var dañoRecibido: Double,
    var tacticas: List<TacticaDTO>,
    var atributos: AtributosDTO
) {

    companion object {

        fun desdeModelo(aventurero: Aventurero): AventureroDTO {
            val tacticasDT0 = aventurero.tacticas.map { tac -> TacticaDTO.desdeModelo(tac) }
            val atributosDTO = AtributosDTO.desdeModelo(aventurero.atributos!!)
            return AventureroDTO(
                aventurero.id,
                aventurero.nivel,
                aventurero.nombre,
                aventurero.imagenUrl,
                aventurero.vidaPorInteraccion,
                tacticasDT0,
                atributosDTO
            )
        }
    }

    fun aModelo(): Aventurero {

        val tacticasModelo = this.tacticas.map { tacDTO -> tacDTO.aModelo() }
        val atributosModelo = this.atributos.aModelo()
        atributosModelo.id = this.atributos.id
        val aventurero = Aventurero(this.nombre, null, this.nivel, this.imagenURL, atributosModelo)
        aventurero.id = id
        aventurero.tacticas = tacticasModelo
        return aventurero

    }

    fun actualizarModelo(aventurero: Aventurero) {
        aventurero.nivel = this.nivel
        aventurero.nombre = this.nombre
        aventurero.imagenUrl = this.imagenURL
        aventurero.vidaPorInteraccion = this.dañoRecibido
        this.tacticas.map { tacDto -> tacDto.actualizarModelo(tacDto.aModelo()) }
        this.atributos.actualizarModelo(aventurero.atributos!!)
    }
}

data class AtributosDTO(
    var id: Long?,
    var fuerza: Double,
    var destreza: Double,
    var constitucion: Double,
    var inteligencia: Double
) {
    companion object {
        fun desdeModelo(atributos: Atributos): AtributosDTO {
            return AtributosDTO(
                atributos.id,
                atributos.fuerza!!,
                atributos.destreza!!,
                atributos.constitucion!!,
                atributos.inteligencia!!
            )
        }
    }

    fun aModelo(): Atributos {
        return Atributos(fuerza, destreza, constitucion, inteligencia)
    }

    fun actualizarModelo(atributos: Atributos) {
        atributos.fuerza = this.fuerza
        atributos.constitucion = this.constitucion
        atributos.destreza = this.destreza
        atributos.inteligencia = this.inteligencia
    }
}