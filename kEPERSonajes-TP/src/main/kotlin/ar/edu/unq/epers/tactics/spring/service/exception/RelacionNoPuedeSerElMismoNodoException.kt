package ar.edu.unq.epers.tactics.spring.service.exception

class RelacionNoPuedeSerElMismoNodoException(val relacion: String, val clase: String) : RuntimeException() {
    override val message: String?
        get() = "No se puede crear una relacion $relacion con la misma clase para $clase"
}