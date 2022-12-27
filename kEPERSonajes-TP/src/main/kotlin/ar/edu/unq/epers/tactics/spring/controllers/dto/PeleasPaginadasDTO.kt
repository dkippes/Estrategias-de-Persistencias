package ar.edu.unq.epers.tactics.spring.controllers.dto

import ar.edu.unq.epers.tactics.spring.service.PeleasPaginadas


data class PeleasPaginadasDTO(val peleas: List<PeleaDTO>, val total: Int) {
    companion object {
        fun desdeModelo(peleasPaginadas: PeleasPaginadas): PeleasPaginadasDTO {
            val peleas = peleasPaginadas.peleas.map { PeleaDTO.desdeModelo(it) }
            return PeleasPaginadasDTO(peleas, peleasPaginadas.total)
        }
    }
}