package ar.edu.unq.unidad1.dao

import ar.edu.unq.unidad1.dao.impl.JSONPersonajeDAO

class JSONPersonajeDAOTest : PersonajeDAOTest() {
    override fun createDAO(): PersonajeDAO {
        return JSONPersonajeDAO()
    }
}