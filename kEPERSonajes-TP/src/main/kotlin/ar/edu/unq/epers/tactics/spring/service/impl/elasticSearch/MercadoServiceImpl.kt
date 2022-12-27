package ar.edu.unq.epers.tactics.spring.service.impl.elasticSearch

import ar.edu.unq.epers.tactics.spring.controllers.dto.GananciasDTO
import ar.edu.unq.epers.tactics.spring.modelo.Item
import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.elasticSearch.MercadoDao
import ar.edu.unq.epers.tactics.spring.service.MercadoService
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.index.query.TermsQueryBuilder
import org.elasticsearch.script.Script
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.metrics.ParsedSum
import org.elasticsearch.search.sort.ScriptSortBuilder
import org.elasticsearch.search.sort.SortBuilder
import org.elasticsearch.search.sort.SortBuilders
import org.elasticsearch.search.sort.SortOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.elasticsearch.core.AggregationsContainer
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MercadoServiceImpl(@Autowired val mercadoDao: MercadoDao, @Autowired val elasticTemplate: ElasticsearchOperations): MercadoService {
    override fun guardarItem(item: Item): Item {
        return mercadoDao.save(item)
    }

    override fun recuperarTodosLosItems(): List<Item> {
        return mercadoDao.findAll().toList()
    }

    override fun recuperarItem(id: String): Item {
        val item = mercadoDao.findById(id)
        if (!item.isPresent) {
            throw RuntimeException("GET RESPONSE: no existe el item con id ${id}")
        }
        return item.get()
    }

    override fun borraItem(id: String) {
        val item = mercadoDao.findById(id)
        if (!item.isPresent) {
            throw RuntimeException("DELETE RESPONSE: no existe el item con id ${id}")
        }
        mercadoDao.delete(item.get())
    }

    override fun recuperarItemsDeUsuario(owner: String): List<Item> {
        val q = NativeSearchQueryBuilder()
            .withQuery(
                QueryBuilders.matchQuery("owner", owner)
            ).build()
        return elasticTemplate.search(q,Item::class.java).toList().map { it.content }
    }

    override fun recuperarPorNombreYDescripcion(nombre: String, descripcion: String): List<Item> {
        val q = NativeSearchQueryBuilder()
            .withQuery(
                QueryBuilders.matchQuery("nombre", nombre)
            ).withQuery(
                QueryBuilders.matchQuery("descripcion", descripcion)
            ).build()

        val resultado = elasticTemplate.search(q,Item::class.java).searchHits.map { it.content }

        return resultado
    }

    /*
    * PUIS: PICK UP IN STORE.
    * */
    override fun recuperarPorItemsQueTieneMercadoEnvioOPuis(): List<Item> {
        val boolqueryBuilder = BoolQueryBuilder()
            .should(QueryBuilders.termQuery("mercadoEnvio", true))
            .should(QueryBuilders.termQuery("pickUpInStore", true))

        val query=NativeSearchQueryBuilder().withQuery(boolqueryBuilder).build()

        val resultado = elasticTemplate.search(query, Item::class.java).searchHits.map { it.content }

        return resultado
    }


    override fun recuperarItemConMayorPuntaje() : Item {
        val q = NativeSearchQueryBuilder().withQuery(QueryBuilders.matchAllQuery()).withSorts(
            SortBuilders.scriptSort(
                Script("def s = 0; for (i in params['_source'].opiniones ){ s += i.puntuacion;} return s;"),
                ScriptSortBuilder.ScriptSortType.NUMBER
            ).order(SortOrder.DESC)
        ).withMaxResults(1)
            .build()

        return elasticTemplate.search(q, Item::class.java).searchHits[0].content
    }

    override fun recuperarItemConMenorPuntaje() : Item {
        val q = NativeSearchQueryBuilder().withQuery(QueryBuilders.matchAllQuery()).withSorts(
            SortBuilders.scriptSort(
                Script("def s = 0; for (i in params['_source'].opiniones ){ s += i.puntuacion;} return s;"),
                ScriptSortBuilder.ScriptSortType.NUMBER
            ).order(SortOrder.ASC)
        ).withMaxResults(1)
            .build()

        return elasticTemplate.search(q, Item::class.java).searchHits[0].content
    }

    override fun recuperarGananciaTotalEsperadaDeUnOwner(owner: String) : GananciasDTO {
        val q = NativeSearchQueryBuilder().withQuery(
            QueryBuilders.matchQuery("owner", owner)
        ).withAggregations(
            AggregationBuilders.sum("ganancias").field("precio")
        ).withMaxResults(0).build()

        val sum = (elasticTemplate.search(q, Item::class.java)
            .aggregations?.aggregations() as Iterable<ParsedSum>)
            .toList()[0].value

        return GananciasDTO(owner, sum)
    }

    fun borrarTodo() {
        mercadoDao.deleteAll()
    }
}