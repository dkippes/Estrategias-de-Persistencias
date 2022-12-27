package ar.edu.unq.epers.tactics.spring.controllers

import ar.edu.unq.epers.tactics.spring.controllers.dto.PartyDTO
import ar.edu.unq.epers.tactics.spring.controllers.dto.PartyPaginadasDTO
import ar.edu.unq.epers.tactics.spring.service.Direccion
import ar.edu.unq.epers.tactics.spring.service.Orden
import ar.edu.unq.epers.tactics.spring.service.PartyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@ServiceREST
@RequestMapping("/party")
class PartyControllerRest(@Autowired private val partyService: PartyService) {
    @GetMapping
    fun getParties(): List<PartyDTO> = partyService.recuperarTodas().map { PartyDTO.desdeModelo(it) }

    @GetMapping("/ordenadas")
    fun partiesOrdenadas(@RequestBody request: PartiesOrdenadasRequest): PartyPaginadasDTO {
        return PartyPaginadasDTO.desdeModelo(
            partyService.recuperarOrdenadas(
                request.orden,
                request.direccion,
                request.pagina
            )
        )
    }

    @GetMapping("/{id}")
    fun getParty(@PathVariable id: Long) = PartyDTO.desdeModelo(partyService.recuperar(id))

    @PutMapping("/{id}")
    fun updateParty(@PathVariable id: Long, @RequestBody partyData: PartyDTO): PartyDTO {
        val party = partyService.recuperar(id)
        partyData.actualizarModelo(party)
        return PartyDTO.desdeModelo(partyService.actualizar(party))
    }

    @PostMapping
    fun createParty(@RequestBody partyData: PartyDTO): PartyDTO {
        return PartyDTO.desdeModelo(partyService.crear(partyData.aModelo()))
    }
}

data class PartiesOrdenadasRequest(var orden: Orden, var direccion: Direccion, var pagina: Int?)

