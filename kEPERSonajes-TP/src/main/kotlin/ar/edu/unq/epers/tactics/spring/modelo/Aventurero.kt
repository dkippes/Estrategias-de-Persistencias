package ar.edu.unq.epers.tactics.spring.modelo

import ar.edu.unq.epers.tactics.spring.modelo.clases.Mejora
import ar.edu.unq.epers.tactics.spring.modelo.excepcion.AventureroEstaMuertoException
import ar.edu.unq.epers.tactics.spring.modelo.excepcion.SinManaException
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.AtaqueMagico
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.Curacion
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.Habilidad
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.HabilidadNula
import ar.edu.unq.epers.tactics.spring.modelo.randomizador.Randomizador
import ar.edu.unq.epers.tactics.spring.service.exception.AventureroNoTieneProficienciaParaGastar
import javax.persistence.*

@Entity
class Aventurero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    var party: Party? = null

    var nombre: String
    var nivel: Int = 1
    var proficienciaAGastar: Int = 0

    var vidaPorInteraccion: Double = 0.0
    var manaPorInteraccion: Int = 0

    @ManyToOne
    var defensor: Aventurero? = null

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var tacticas: List<Tactica> = listOf()

    var imagenUrl: String
    val vidaMaxima: Double
    val manaMaximo: Double

    @OneToOne(cascade = [CascadeType.ALL])
    var atributos: Atributos? = null


    @ElementCollection
    var clases = mutableSetOf("Aventurero")

    constructor(
        nombre: String,
        party: Party?,
        nivel: Int,
        imagenUrl: String,
        atributos: Atributos
    ) {
        this.nombre = nombre
        this.nivel = nivel
        this.imagenUrl = imagenUrl
        this.party = party
        this.atributos = atributos
        this.vidaMaxima = getVida()
        this.manaMaximo = getMana()
        this.atributos = atributos
    }

    fun tacticasPorPrioridad() = tacticas.sortedBy { it.prioridad }
    fun aliadosDelAventurero() = party?.aventureros?.filter { av -> av != this && !av.estaMuerto() }?.toMutableSet()!!
    fun enemigosDelAventurero(enemigos: List<Aventurero>) = enemigos.filter { !it.estaMuerto() }.toMutableSet()
    fun resolverTactica(enemigos: List<Aventurero>): Habilidad {
        val aliados = aliadosDelAventurero()
        val enemigosVivos = enemigosDelAventurero(enemigos)

        for (tactic in tacticasPorPrioridad()) {
            val habilidad = tactic.obtenerHabilidad(this, aliados, enemigosVivos, Randomizador())
            if (habilidad != null) {
                if (habilidad is AtaqueMagico || habilidad is Curacion) {
                    this.percibirEfectosPorUsoDeMana()
                }
                return habilidad
            }
        }
        return HabilidadNula(this, this)
    }

    fun agregarTactica(tactica: Tactica): Aventurero {
        val tacticasMutableList = this.tacticas.toMutableList()
        tacticasMutableList.add(tactica)
        this.tacticas = tacticasMutableList
        return this
    }

    fun getVida(): Double {
        return this.nivel * 5 + this.atributos?.constitucion!! * 2 + this.atributos?.fuerza!! + this.vidaPorInteraccion
    }

    fun getArmadura(): Double {
        return this.nivel + this.atributos?.constitucion!!
    }

    fun getMana(): Double {
        return this.nivel + this.atributos?.inteligencia!! + this.manaPorInteraccion
    }

    fun getVelocidad(): Double {
        return this.nivel + this.atributos?.destreza!!
    }

    fun getDanoFisico(): Double {
        return this.nivel + this.atributos?.fuerza!! + this.atributos?.destreza!! / 2
    }

    fun getPoderMagico(): Double {
        return this.nivel + this.atributos?.inteligencia!!
    }

    fun getPrecisionFisica(): Double {
        return this.nivel + this.atributos?.fuerza!! + this.atributos?.destreza!!
    }

    fun percibirEfectosPorSerAtacado(dano: Double) {
        val vidaARestar = this.vidaPorInteraccion - dano
        if (this.estaMuerto()) {
            throw AventureroEstaMuertoException()
        }
        if (this.vidaMaxima + vidaARestar < 0) {
            this.vidaPorInteraccion = -(this.vidaMaxima)
        } else {
            this.vidaPorInteraccion = vidaARestar
        }
    }

    fun percibirEfectosPorSerDefendido(defensor: Aventurero) {
        this.defensor = defensor
    }

    fun tieneDefensor(): Boolean {
        return this.defensor != null
    }

    fun percibirEfectosPorSerCurado(poderMagicoDeEmisor: Double) {
        if (getVida() + poderMagicoDeEmisor > vidaMaxima) {
            this.vidaPorInteraccion = 0.0
        } else {
            this.vidaPorInteraccion += poderMagicoDeEmisor
        }
    }

    fun percibirEfectosPorUsoDeMana() {
        val manaARestar = this.manaPorInteraccion - 5
        if (this.getMana() + manaARestar < 0) {
            throw SinManaException()
        }
        this.manaPorInteraccion = manaARestar
    }

    fun percibirEfectosPorMeditar() {
        val manaRecuperado = this.nivel
        if (getMana() + manaRecuperado > manaMaximo) {
            this.manaPorInteraccion = 0
        } else {
            this.manaPorInteraccion = this.manaPorInteraccion + manaRecuperado
        }
    }

    fun volverAEstadoInicial() {
        this.vidaPorInteraccion = 0.0
        this.manaPorInteraccion = 0
        this.defensor = null
    }

    fun estaMuerto(): Boolean {
        return (this.vidaMaxima + this.vidaPorInteraccion) == 0.0
    }

    fun subirExperienciaYProficiencia() {
        this.nivel += 1
        this.proficienciaAGastar += 1
    }

    @PreRemove
    fun removerParty() {
        this.party?.removerAventurero(this)
        this.party = null
    }

    fun ganarProficiencia(mejora: Mejora) {
        if (proficienciaAGastar <= 0) throw AventureroNoTieneProficienciaParaGastar()
        this.clases.add(mejora.claseAMejorar.nombre)
        mejora.atributos.forEach { it.sumarAtributoAAventurero(this, mejora.bonoPorAtributo) }
    }
}
