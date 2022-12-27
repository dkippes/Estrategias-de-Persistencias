package ar.edu.unq.epers.tactics.spring.service.exception

class PeleaTerminadaException(val id: Long) : RuntimeException() {
    override val message: String
        get() = "La pelea $id ya fue terminada"
}