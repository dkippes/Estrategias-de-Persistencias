package ar.edu.unq.unidad1.dao

import ar.edu.unq.unidad1.dao.impl.NativePersonajeDAO

class NativePersonajeDAOTest : PersonajeDAOTest() {
    override fun createDAO(): PersonajeDAO {
        return NativePersonajeDAO()
    }
}