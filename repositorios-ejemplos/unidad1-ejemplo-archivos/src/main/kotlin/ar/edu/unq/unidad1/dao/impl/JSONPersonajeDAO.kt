package ar.edu.unq.unidad1.dao.impl

import ar.edu.unq.unidad1.dao.PersonajeDAO
import ar.edu.unq.unidad1.modelo.Personaje
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import java.io.File
import java.io.IOException

/**
 * Esta implementacion de [PersonajeDAO] persistirá toda la agregación
 * del [Personaje] (es decir, el [Personaje] y sus [Item])
 * en un archivo JSON
 *
 */
class JSONPersonajeDAO : BaseFileDAO("json"), PersonajeDAO {
    private val mapper: ObjectMapper = ObjectMapper()

    init {
        mapper.enable(SerializationFeature.INDENT_OUTPUT)
    }

    override fun guardar(personaje: Personaje) {
        val dataFile: File = getStorage(personaje.nombre)
        deleteIfExists(dataFile)
        mapper.writeValue(dataFile, personaje)
    }

    override fun recuperar(nombre: String): Personaje? {
        val dataFile: File = getStorage(nombre)
        if (!dataFile.exists()) {
            return null
        }
        return mapper.readValue(dataFile, Personaje::class.java)
    }
}