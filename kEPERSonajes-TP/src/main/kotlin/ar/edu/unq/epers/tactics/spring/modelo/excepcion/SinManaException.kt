package ar.edu.unq.epers.tactics.spring.modelo.excepcion


class SinManaException : RuntimeException() {
    override val message: String
        get() = "El aventurero no tiene mana"
}