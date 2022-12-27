package ar.edu.unq.epers.tactics.spring.modelo.tacticas

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero

enum class TipoDeEstadistica {
    VIDA {
        override fun obtenerValorEnAventurero(aventurero: Aventurero): Double {
            return aventurero.getVida()
        }
    },
    ARMADURA {
        override fun obtenerValorEnAventurero(aventurero: Aventurero): Double {
            return aventurero.getArmadura()
        }
    },
    MANA {
        override fun obtenerValorEnAventurero(aventurero: Aventurero): Double {
            return aventurero.getMana()
        }
    },
    VELOCIDAD {
        override fun obtenerValorEnAventurero(aventurero: Aventurero): Double {
            return aventurero.getVelocidad()
        }
    },
    DANIO_FISICO {
        override fun obtenerValorEnAventurero(aventurero: Aventurero): Double {
            return aventurero.getDanoFisico()
        }
    },
    DANIO_MAGICO {
        override fun obtenerValorEnAventurero(aventurero: Aventurero): Double {
            return aventurero.getPoderMagico()
        }
    },
    PRECISION_FISICA {
        override fun obtenerValorEnAventurero(aventurero: Aventurero): Double {
            return aventurero.getPrecisionFisica()
        }
    };

    abstract fun obtenerValorEnAventurero(aventurero: Aventurero): Double
}