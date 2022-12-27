package ar.edu.unq.epers.tactics.spring.modelo.tacticas

enum class Criterio {
    IGUAL {
        override fun evaluar(aNumber: Double, otherNumber: Double): Boolean = aNumber == otherNumber
    },
    MAYOR_QUE {
        override fun evaluar(aNumber: Double, otherNumber: Double): Boolean = aNumber > otherNumber
    },
    MENOR_QUE {
        override fun evaluar(aNumber: Double, otherNumber: Double): Boolean = aNumber < otherNumber
    };

    abstract fun evaluar(aNumber: Double, otherNumber: Double): Boolean
}