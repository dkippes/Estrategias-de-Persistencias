package ar.edu.unq.epers.tactics.spring.modelo.excepcion

class MutiplePeleaException : RuntimeException() {
    override val message: String?
        get() = "Una party no puede empezar otra pelea si esta peleando"
}