package ar.edu.unq.epers.tactics.spring.service.exception

import javax.persistence.EntityNotFoundException

class PartyNotFoundException(val id: Long) : EntityNotFoundException() {
    override val message: String
        get() = "La party $id no existe"
}

class PeleaNotFoundException(val id: Long) : EntityNotFoundException() {
    override val message: String
        get() = "La pelea $id no existe"
}

class AventureroNotFoundException(val id: Long) : EntityNotFoundException() {
    override val message: String
        get() = "El aventurero $id no existe"
}