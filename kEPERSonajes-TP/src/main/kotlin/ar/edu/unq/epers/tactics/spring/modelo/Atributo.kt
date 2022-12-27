package ar.edu.unq.epers.tactics.spring.modelo

enum class Atributo(value: String) {
    FUERZA("FUERZA") {
        override fun sumarAtributoAAventurero(aventurero: Aventurero, bonoPorAtributo: Int) {
            aventurero.atributos!!.fuerza += bonoPorAtributo
        }
    }, INTELIGENCIA("INTELIGENCIA") {
        override fun sumarAtributoAAventurero(aventurero: Aventurero, bonoPorAtributo: Int) {
            aventurero.atributos!!.inteligencia += bonoPorAtributo
        }
    }, CONSTITUCION("CONSTITUCION") {
        override fun sumarAtributoAAventurero(aventurero: Aventurero, bonoPorAtributo: Int) {
            aventurero.atributos!!.constitucion += bonoPorAtributo
        }
    }, DESTREZA("DESTREZA") {
        override fun sumarAtributoAAventurero(aventurero: Aventurero, bonoPorAtributo: Int) {
            aventurero.atributos!!.destreza += bonoPorAtributo
        }
    };

    abstract fun sumarAtributoAAventurero(aventurero: Aventurero, bonoPorAtributo: Int)
}