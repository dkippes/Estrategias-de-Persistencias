package ar.edu.unq.epers.tactics.spring.service.exception

class YaExisteLaRelacionException(val relacion: String) : RuntimeException() {
    override val message: String?
        get() = "Ya existe la relacion $relacion entre las clases"
}