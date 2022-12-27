package ar.edu.unq.epers.tactics.spring.service

import ar.edu.unq.epers.tactics.spring.controllers.dto.GananciasDTO
import ar.edu.unq.epers.tactics.spring.modelo.Item
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.repository.query.Param

interface MercadoService {
    fun guardarItem(item: Item): Item
    fun recuperarTodosLosItems(): List<Item>
    fun recuperarItem(id: String): Item
    fun borraItem(id: String)
    fun recuperarItemsDeUsuario(owner: String): List<Item>
    fun recuperarPorNombreYDescripcion(nombre: String, descripcion: String): List<Item>
    fun recuperarPorItemsQueTieneMercadoEnvioOPuis(): List<Item>
    fun recuperarItemConMayorPuntaje(): Item
    fun recuperarItemConMenorPuntaje() : Item
    fun recuperarGananciaTotalEsperadaDeUnOwner(owner: String) : GananciasDTO

}