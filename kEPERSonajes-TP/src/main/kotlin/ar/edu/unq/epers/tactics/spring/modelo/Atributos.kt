package ar.edu.unq.epers.tactics.spring.modelo

import ar.edu.unq.epers.tactics.spring.modelo.excepcion.ValorDeAtributoIncorrecto
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Atributos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var fuerza: Double = 0.0
    var destreza: Double = 0.0
    var constitucion: Double = 0.0
    var inteligencia: Double = 0.0

    constructor(fuerza: Double, destreza: Double, constitucion: Double, inteligencia: Double) {
        checkearSiEsUnRangoDeAtributoValido(listOf(fuerza, destreza, inteligencia, constitucion))
        this.fuerza = fuerza
        this.destreza = destreza
        this.constitucion = constitucion
        this.inteligencia = inteligencia
    }

    private fun checkearSiEsUnRangoDeAtributoValido(atributos: List<Double>) {
        for (atributo in atributos) {
            if (!estaEntre1Y100(atributo)) {
                throw ValorDeAtributoIncorrecto()
            }
        }
    }

    private fun estaEntre1Y100(valor: Double): Boolean {
        return (1 <= valor) && (valor <= 100)
    }
}