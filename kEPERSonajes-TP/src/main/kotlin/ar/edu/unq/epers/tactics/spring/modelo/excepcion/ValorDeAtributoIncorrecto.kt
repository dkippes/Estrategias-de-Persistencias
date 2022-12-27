package ar.edu.unq.epers.tactics.spring.modelo.excepcion

class ValorDeAtributoIncorrecto() : RuntimeException() {
    override val message: String?
        get() = "Uno de los valores de los atributos esta fuera del rango"
}