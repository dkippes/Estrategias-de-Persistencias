package ar.edu.unq.epers.tactics.spring.service.exception

class AventureroNoTieneClaseException(val idAventurero: Long, val clase: String) : RuntimeException() {
    override val message: String
        get() = "El aventurero $idAventurero no tiene clase $clase"
}