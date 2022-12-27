package ar.edu.unq.epers.tactics.spring.modelo

import ar.edu.unq.epers.tactics.spring.modelo.habilidades.*
import ar.edu.unq.epers.tactics.spring.modelo.randomizador.Randomizador
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.Accion
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.Criterio
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.TipoDeEstadistica
import ar.edu.unq.epers.tactics.spring.modelo.tacticas.TipoDeReceptor
import javax.persistence.*

@Entity
class Tactica(
    val prioridad: Int,
    val criterio: Criterio,
    val tipoDeEstadistica: TipoDeEstadistica,
    val valor: Double,
    val receptor: TipoDeReceptor,
    val accion: Accion
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    /**
     * Devuelve una habilidad si hay un receptor que cumpla con el critero
     * Sino retorna null como indicador para pasar a la siguiente tactica
     * @param aliados MutableSet<Aventurero> - por si se indica que el receptor esperado es un aliado
     * @param enemigos MutableSet<Aventurero> - por si se indica que el receptor esperado es un enemigo
     * @return Habilidad?
     */
    fun obtenerHabilidad(
        aventurero: Aventurero,
        aliados: MutableSet<Aventurero>,
        enemigos: MutableSet<Aventurero>,
        randomizador: Randomizador
    ): Habilidad? {
        val receptorDeHabilidad = hayReceptorAdecuado(aventurero, aliados, enemigos)
            ?: return null

        when (accion) {
            Accion.ATAQUE_FISICO -> {
                return Ataque(
                    receptorDeHabilidad,
                    aventurero,
                    aventurero.getPrecisionFisica(),
                    aventurero.getDanoFisico(),
                    randomizador
                )
            }
            Accion.ATAQUE_MAGICO -> {
                return AtaqueMagico(
                    receptorDeHabilidad,
                    aventurero,
                    aventurero.nivel,
                    aventurero.getPoderMagico(),
                    randomizador
                )
            }
            Accion.CURAR -> {
                return Curacion(receptorDeHabilidad, aventurero, aventurero.getPoderMagico())
            }
            Accion.DEFENDER -> {
                return Defensa(receptorDeHabilidad, aventurero)
            }
            Accion.MEDITAR -> {
                return Meditacion(aventurero, aventurero)
            }
        }
    }

    /**
     * Devuelve una aventurero si cumple con el critero
     * Sino retorna null
     * @param aliados MutableSet<Aventurero> - por si se indica que el receptor esperado es un aliado
     * @param enemigos MutableSet<Aventurero> - por si se indica que el receptor esperado es un enemigo
     * @return Aventurero?
     */
    fun hayReceptorAdecuado(
        aventurero: Aventurero,
        aliados: MutableSet<Aventurero>,
        enemigos: MutableSet<Aventurero>
    ): Aventurero? {
        var receptorDeHabilidad: Aventurero? = null
        when (receptor) {
            TipoDeReceptor.ALIADO -> {
                receptorDeHabilidad =
                    aliados.find { av -> evaluarCriterio(tipoDeEstadistica.obtenerValorEnAventurero(av)) }
            }
            TipoDeReceptor.ENEMIGO -> {
                receptorDeHabilidad =
                    enemigos.find { av -> evaluarCriterio(tipoDeEstadistica.obtenerValorEnAventurero(av)) }
            }
            TipoDeReceptor.UNO_MISMO -> {
                if (evaluarCriterio(tipoDeEstadistica.obtenerValorEnAventurero(aventurero))) {
                    receptorDeHabilidad = aventurero
                }
            }
        }
        return receptorDeHabilidad
    }

    fun evaluarCriterio(valorDeEstadistica: Double): Boolean = criterio.evaluar(valorDeEstadistica, valor)

    fun actualizarse(aModelo: Tactica) {
        //TODO("Hay que armarlo")
    }

}