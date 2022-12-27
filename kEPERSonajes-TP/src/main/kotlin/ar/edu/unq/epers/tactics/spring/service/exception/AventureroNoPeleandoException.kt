package ar.edu.unq.epers.tactics.spring.service.exception

class AventureroNoPeleandoException(val id: Long) : RuntimeException() {
    override val message: String
        get() = "El aventurero $id no esta peleando"
}