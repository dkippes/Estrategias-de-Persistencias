package ar.edu.unq.epers.tactics.spring.controllers.dto

import ar.edu.unq.epers.tactics.spring.service.PartyPaginadas

data class PartyPaginadasDTO(val parties: List<PartyDTO>, val total: Int) {
    companion object {
        fun desdeModelo(partyPaginadas: PartyPaginadas): PartyPaginadasDTO {
            return PartyPaginadasDTO(partyPaginadas.parties.map { PartyDTO.desdeModelo(it) }, partyPaginadas.total)
        }
    }
}