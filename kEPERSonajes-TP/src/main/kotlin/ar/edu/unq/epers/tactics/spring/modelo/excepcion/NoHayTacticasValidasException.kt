package ar.edu.unq.epers.tactics.spring.modelo.excepcion


class NoHayTacticasValidasException : RuntimeException() {
    override val message: String
        get() = "El aventurero no tiene tacticas validas para este turno"
}