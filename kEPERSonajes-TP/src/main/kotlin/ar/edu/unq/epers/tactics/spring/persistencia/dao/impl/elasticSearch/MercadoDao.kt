package ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.elasticSearch

import ar.edu.unq.epers.tactics.spring.modelo.Item
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface MercadoDao: ElasticsearchRepository<Item, String> {
}