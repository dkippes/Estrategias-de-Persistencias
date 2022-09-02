package ar.edu.unq.unidad1.dao

import ar.edu.unq.unidad1.dao.impl.XMLPersonajeDAO

class XMLPersonajeDAOTest : PersonajeDAOTest() {
    override fun createDAO(): PersonajeDAO {
        return XMLPersonajeDAO()
    }
}