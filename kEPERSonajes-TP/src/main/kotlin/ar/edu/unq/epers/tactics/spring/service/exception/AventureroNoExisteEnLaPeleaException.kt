package ar.edu.unq.epers.tactics.spring.service.exception

class AventureroNoExisteEnLaPeleaException(val idAventurero: Long, val idPelea: Long) : RuntimeException() {
    override val message: String
        get() = "El aventurero $idAventurero no existe en la pelea $idPelea"
}