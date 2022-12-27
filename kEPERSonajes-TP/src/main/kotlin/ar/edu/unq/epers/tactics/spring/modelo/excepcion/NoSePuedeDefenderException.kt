package ar.edu.unq.epers.tactics.spring.modelo.excepcion


class NoSePuedeDefenderException(val id: Long) : RuntimeException() {
    override val message: String
        get() = "El aventurero $id no puede ser defendido"
}