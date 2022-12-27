package ar.edu.unq.epers.tactics.spring.controllers

import ar.edu.unq.epers.tactics.spring.controllers.dto.AventureroDTO
import ar.edu.unq.epers.tactics.spring.controllers.dto.HabilidadDTO
import ar.edu.unq.epers.tactics.spring.controllers.dto.PeleasPaginadasDTO
import ar.edu.unq.epers.tactics.spring.service.PeleaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@ServiceREST
@RequestMapping("/fight")
class FightControllerRest(@Autowired private val peleaService: PeleaService) {

    @PostMapping
    fun createFight(@RequestBody request: CreateFightRequest) {
        peleaService.iniciarPelea(request.partyId, request.partyEnemiga)
    }

    @PostMapping("/{fightId}/finish")
    fun finish(@PathVariable fightId: Long): StatusResponse {
        peleaService.terminarPelea(fightId)
        return StatusResponse(200)
    }

    @PostMapping("/{fightId}/resolveTurn")
    fun resolveTurn(@PathVariable fightId: Long, @RequestBody request: ResolveTurnRequest): HabilidadDTO? {
        val enemigosModelo = request.enemies.map { it.aModelo() }
        val habilidad = peleaService.resolverTurno(fightId, request.adventurerId, enemigosModelo)
        return HabilidadDTO.desdeModelo(habilidad)
    }

    @PostMapping("/{fightId}/receiveAbility")
    fun receiveAbility(@PathVariable fightId: Long, @RequestBody request: ReceiveAbilityRequest): AventureroDTO {
        val aventurero = peleaService.recibirHabilidad(request.adventurerId, request.ability.aModelo())
        return AventureroDTO.desdeModelo(aventurero)
    }

    @GetMapping("/party/{partyId}")
    fun recuperarOrdenadas(
        @PathVariable partyId: Long,
        @RequestParam(required = false) pagina: Int?
    ): PeleasPaginadasDTO {
        val pelea = peleaService.recuperarOrdenadas(partyId, pagina)
        return PeleasPaginadasDTO.desdeModelo(pelea)
    }
}

data class CreateFightRequest(val partyId: Long, val partyEnemiga: String)
data class ResolveTurnRequest(val adventurerId: Long, val enemies: List<AventureroDTO>)
data class ReceiveAbilityRequest(val adventurerId: Long, val ability: HabilidadDTO)

