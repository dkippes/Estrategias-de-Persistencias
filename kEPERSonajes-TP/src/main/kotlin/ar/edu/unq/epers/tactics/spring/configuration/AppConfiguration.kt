package ar.edu.unq.epers.tactics.spring.configuration


import org.elasticsearch.client.RestHighLevelClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.RestClients
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate


@Configuration
class AppConfiguration {

    @Bean
    fun groupName(): String {
        val groupName: String? = System.getenv()["GROUP_NAME"]
        if (groupName == null)
            return "EPERS"
        else
            return groupName!!
    }

    @Bean
    fun client(): RestHighLevelClient? {
        val clientConfiguration: ClientConfiguration = ClientConfiguration.builder()
            .connectedTo("localhost:9200")
            .build()
        return RestClients.create(clientConfiguration).rest()
    }

    @Bean
    fun elasticsearchTemplate(): ElasticsearchOperations? {
        return ElasticsearchRestTemplate(client()!!)
    }
}