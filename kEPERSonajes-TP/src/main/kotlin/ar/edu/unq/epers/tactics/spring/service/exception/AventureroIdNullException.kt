package ar.edu.unq.epers.tactics.spring.service.exception

class AventureroIdNullException : RuntimeException() {
    override val message: String?
        get() = "El aventurero tiene el id en null"
}