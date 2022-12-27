package ar.edu.unq.epers.tactics.spring.service.exception

class ClaseNotFoundException(val clase: String) : RuntimeException() {
    override val message: String
        get() = "La clase $clase no existe"
}