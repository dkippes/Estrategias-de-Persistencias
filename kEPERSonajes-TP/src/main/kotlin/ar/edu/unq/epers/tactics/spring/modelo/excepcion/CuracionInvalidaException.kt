package ar.edu.unq.epers.tactics.spring.modelo.excepcion

class CuracionInvalidaException : RuntimeException() {
    override val message: String?
        get() = "El aventurero no puede curar"
}