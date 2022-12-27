package ar.edu.unq.epers.tactics.spring.controllers.dto

import ar.edu.unq.epers.tactics.spring.modelo.Tactica
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.Accion
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.Criterio
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.TipoDeEstadistica
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.TipoDeReceptor

data class TacticaDTO(
    var id: Long?,
    var prioridad: Int,
    var receptor: TipoDeReceptor,
    var tipoDeEstadistica: TipoDeEstadistica,
    var criterio: Criterio,
    var valor: Double,
    var accion: Accion
) {

    companion object {
        fun desdeModelo(tactica: Tactica): TacticaDTO {
            return TacticaDTO(
                tactica.id,
                tactica.prioridad,
                tactica.receptor,
                tactica.tipoDeEstadistica,
                tactica.criterio,
                tactica.valor,
                tactica.accion
            )
        }
    }

    fun aModelo(): Tactica {
        val tactica = Tactica(prioridad, criterio, tipoDeEstadistica, valor, receptor, accion)
        tactica.id = id
        return tactica
    }


    fun actualizarModelo(tactica: Tactica) = tactica.actualizarse(this.aModelo())
}