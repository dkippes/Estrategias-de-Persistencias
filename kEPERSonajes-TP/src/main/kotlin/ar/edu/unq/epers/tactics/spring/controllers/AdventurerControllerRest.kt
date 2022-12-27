package ar.edu.unq.epers.tactics.spring.controllers

import ar.edu.unq.epers.tactics.spring.controllers.dto.AventureroDTO
import ar.edu.unq.epers.tactics.spring.service.AventureroService
import ar.edu.unq.epers.tactics.spring.service.PartyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@ServiceREST
@RequestMapping("/party/{partyId}/adventurer")
class AdventurerControllerRest(
    @Autowired val aventureroService: AventureroService,
    @Autowired val partyService: PartyService
) {

    @GetMapping
    fun getAdventurers(@PathVariable partyId: Long): List<AventureroDTO> {
        val party = partyService.recuperar(partyId)
        return party.aventureros.map { AventureroDTO.desdeModelo(it) }
    }

    @GetMapping("/{id}")
    fun getAdventurer(@PathVariable id: Long): AventureroDTO {
        return AventureroDTO.desdeModelo(aventureroService.recuperar(id))
    }

    @PutMapping("/{id}")
    fun updateAdventurer(@PathVariable id: Long, @RequestBody adventurerData: AventureroDTO): AventureroDTO {
        val aventurero = aventureroService.recuperar(id)
        adventurerData.actualizarModelo(aventurero)
        return AventureroDTO.desdeModelo(aventureroService.actualizar(aventurero))
    }

    @PostMapping
    fun createAdventurer(@PathVariable partyId: Long, @RequestBody adventurerData: AventureroDTO): AventureroDTO {
        val aventureroAgregado = partyService.agregarAventureroAParty(partyId, adventurerData.aModelo())
        return AventureroDTO.desdeModelo(aventureroAgregado)
    }

    @DeleteMapping("/{id}")
    fun deleteAdventurer(@PathVariable id: Long): StatusResponse {
        try {
            aventureroService.eliminar(id)
        } catch (err: Error) {
            return StatusResponse(500)
        }
        return StatusResponse(201)
    }

}

data class StatusResponse(var status: Int)