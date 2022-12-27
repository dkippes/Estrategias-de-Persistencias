package ar.edu.unq.epers.tactics.spring.service.exception

class ClaseCannotCreateException(val clase: String) : RuntimeException() {
    override val message: String
        get() = "La clase $clase ya existe y no se puede crear"
}