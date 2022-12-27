package ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.mongo

import ar.edu.unq.epers.tactics.spring.modelo.formacion.Formacion
import ar.edu.unq.epers.tactics.spring.modelo.formacion.Requerimiento
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component

@Component
class FormacionDAO {
    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    fun formacionesQuePosee(requerimientos: List<Requerimiento>): List<Formacion> {
        val req = ObjectMapper().writeValueAsString(requerimientos)
        val cumpleRequisitos = """
            function(){
                return this.requisitosFormacion.every(formReq => {
      let requisitoDePartyCorrespondiente = $req.find(reqInput => reqInput.clase === formReq.clase)
      return (requisitoDePartyCorrespondiente &&  requisitoDePartyCorrespondiente.cantidadDeAventureros >= formReq.cantidadDeAventureros)
    })
            }
        """
        val query = Query()
        query.addCriteria(Criteria.where("${'$'}where").`is`(cumpleRequisitos))

        return mongoTemplate.find(query, Formacion::class.java, "Formacion")
    }
}
