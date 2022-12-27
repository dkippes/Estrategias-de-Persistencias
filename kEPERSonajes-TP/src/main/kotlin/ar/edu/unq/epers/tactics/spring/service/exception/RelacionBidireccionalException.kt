package ar.edu.unq.epers.tactics.spring.service.exception

class RelacionBidireccionalException(val relacion: String) : RuntimeException() {
    override val message: String?
        get() = "La relacion $relacion no puede ser bidireccional"
}