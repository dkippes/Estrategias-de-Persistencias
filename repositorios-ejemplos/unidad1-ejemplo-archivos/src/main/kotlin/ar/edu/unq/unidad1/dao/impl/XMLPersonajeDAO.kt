package ar.edu.unq.unidad1.dao.impl

import ar.edu.unq.unidad1.dao.PersonajeDAO
import ar.edu.unq.unidad1.modelo.Item
import ar.edu.unq.unidad1.modelo.Personaje
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.StaxDriver
import java.io.FileOutputStream
import java.io.IOException

/**
 * Esta implementacion de [PersonajeDAO] persistirá toda la agregación
 * del [Personaje] (es decir, el [Personaje] y sus [Item])
 * en un archivo XML
 *
 */
class XMLPersonajeDAO : BaseFileDAO("xml"), PersonajeDAO {
    private val xstream: XStream

    init {
        xstream = XStream(StaxDriver())
        xstream.allowTypesByRegExp(arrayOf( ".*" ));
        xstream.processAnnotations(Personaje::class.java)
        xstream.processAnnotations(Item::class.java)
    }

    override fun guardar(personaje: Personaje) {
        val dataFile = getStorage(personaje.nombre)
        deleteIfExists(dataFile)

        FileOutputStream(dataFile)
            .use { stream -> xstream.toXML(personaje, stream) }
    }

    override fun recuperar(nombre: String): Personaje? {
        val dataFile = getStorage(nombre)
        if (!dataFile.exists()) {
            // No existe el personaje
            return null
        }
        return xstream.fromXML(dataFile) as Personaje
    }

}