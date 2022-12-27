package ar.edu.unq.epers.tactics.spring.helpers

import ar.edu.unq.epers.tactics.spring.modelo.Atributo
import ar.edu.unq.epers.tactics.spring.modelo.clases.Clase
import ar.edu.unq.epers.tactics.spring.modelo.clases.Mejora
import ar.edu.unq.epers.tactics.spring.modelo.formacion.AtributoDeFormacion
import org.bson.Document
import org.neo4j.driver.internal.value.ListValue
import java.util.*
import kotlin.collections.ArrayList

class MapperHelper {
    companion object {
        fun map(list: Any, listaMejora: MutableCollection<Mejora>) {
            val mejoras = if ((list as Optional<*>).isPresent) (list.get() as ListValue).asList() else listOf()
            for (mejora in mejoras) {
                val clase = (mejora as Map<*, *>).get("claseAMejorar")
                val bonoPorAtributo = (mejora as Map<String, Long>).get("bonoPorAtributo")
                val atributosSinMappear = (mejora as Map<String, List<*>>).get("atributos")
                val atributos = atributosSinMappear!!.map { Atributo.valueOf(it.toString()) }
                val mejoraAgregar = Mejora(Clase(clase.toString()), bonoPorAtributo!!.toInt(), atributos)
                listaMejora.add(mejoraAgregar)
            }
        }

        fun map(list: ArrayList<*>, listaAgregar: MutableList<AtributoDeFormacion>) {
            for (atributoList in list) {
                for (a in atributoList as ArrayList<*>) {
                    val atributeDocument = a as Document
                    val atributo = atributeDocument.get("tipo") as String
                    val bono = atributeDocument.get("bono") as Int
                    listaAgregar.add(AtributoDeFormacion(Atributo.valueOf(atributo), bono))
                }
            }
        }
    }
}