package ar.edu.unq.epers.tactics.spring.controllers

import ar.edu.unq.epers.tactics.spring.controllers.dto.GananciasDTO
import ar.edu.unq.epers.tactics.spring.modelo.Item
import ar.edu.unq.epers.tactics.spring.service.MercadoService
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@ServiceREST
@RequestMapping("/mercado")
class MercadoControllerRest(@Autowired private val mercadoService: MercadoService) {

    val  logger = LogManager.getLogger(MercadoControllerRest::class)

    @PostMapping("/guardar")
    fun guardar(@Valid @RequestBody item: Item): Item {
        logger.info( "POST REQUEST. Item ${item}");
        val response = mercadoService.guardarItem(item)
        logger.info( "POST RESPONSE. Item guardado: ${item}");
        return response
    }

    @GetMapping("/{id}")
    fun recuperarItem(@PathVariable id: String): Item {

        logger.info("GET REQUEST. Item con id ${id}");
        val response = mercadoService.recuperarItem(id)
        logger.info("GET RESPONSE. Item con id ${id} : ${response}");
        return response
    }

    @GetMapping
    fun recuperarTodosLosItems(): List<Item> {
        logger.info("GET REQUEST. todos los items");
        val response =  mercadoService.recuperarTodosLosItems()
        logger.info("GET RESPONSE. Todos los items ${response}");
        return response
    }

    @DeleteMapping("/{id}")
    fun borrar(@PathVariable id: String): ResponseEntity<String> {
        logger.info("DELETE REQUEST: Item con id ${id}");
        mercadoService.borraItem(id)
        logger.info("DELETE RESPONSE: Item con id ${id} borrado existosamente");
        return ResponseEntity.ok("Item borrado")
    }

    @GetMapping("/owner/{owner}")
    fun recuperarItemsDeUsuario(@PathVariable owner: String): List<Item> {
        return mercadoService.recuperarItemsDeUsuario(owner)
    }

    @GetMapping("/owner/{owner}/gananciasEsperadas")
    fun recuperarGanaciasEsperadas(@PathVariable owner: String): GananciasDTO {
        return mercadoService.recuperarGananciaTotalEsperadaDeUnOwner(owner)
    }

    @GetMapping("/search")
    fun recuperarPorNombreYDescripcion(@RequestParam nombre: String, @RequestParam descripcion: String): List<Item> {
        return mercadoService.recuperarPorNombreYDescripcion(nombre, descripcion)
    }

    @GetMapping("/mercadoEnvioOPuis")
    fun recuperarPorItemsQueTieneMercadoEnvioOPuis(): List<Item> {
        return mercadoService.recuperarPorItemsQueTieneMercadoEnvioOPuis()
    }

    @GetMapping("/maximoPuntaje")
    fun recuperarItemConMayorPuntaje(): Item {
        return mercadoService.recuperarItemConMayorPuntaje()
    }

    @GetMapping("/minimoPuntaje")
    fun recuperarItemConMenorPuntaje(): Item {
        return mercadoService.recuperarItemConMenorPuntaje()
    }

}