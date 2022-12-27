package ar.edu.unq.epers.tactics.spring.modelo.excepcion


class AventureroEstaMuertoException : RuntimeException() {
    override val message: String
        get() = "El aventurero esta muerto"
}