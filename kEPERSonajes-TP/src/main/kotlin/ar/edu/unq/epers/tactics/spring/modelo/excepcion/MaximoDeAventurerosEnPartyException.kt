package ar.edu.unq.epers.tactics.spring.modelo.excepcion

class MaximoDeAventurerosEnPartyException : RuntimeException() {
    override val message: String?
        get() = "Se alcanzó el máximo de aventureros en la party"
}
