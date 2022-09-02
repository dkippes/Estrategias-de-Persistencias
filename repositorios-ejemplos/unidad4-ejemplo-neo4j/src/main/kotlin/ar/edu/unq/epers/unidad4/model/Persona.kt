package ar.edu.unq.epers.unidad4.model

class Persona(var dni: String, var nombre: String, var apellido: String) {

    override fun equals(obj: Any?): Boolean {
        if (obj === this) {
            return true
        }
        if (obj is Persona) {
            return dni == obj.dni
        }
        return false
    }

    override fun hashCode(): Int {
        return dni.hashCode()
    }

}