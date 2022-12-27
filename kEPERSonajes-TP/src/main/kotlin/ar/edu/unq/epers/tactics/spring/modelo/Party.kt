package ar.edu.unq.epers.tactics.spring.modelo

import ar.edu.unq.epers.tactics.spring.modelo.excepcion.MaximoDeAventurerosEnPartyException
import ar.edu.unq.epers.tactics.spring.modelo.excepcion.MutiplePeleaException
import ar.edu.unq.epers.tactics.spring.modelo.excepcion.NoEstaPeleandoException
import ar.edu.unq.epers.tactics.spring.modelo.formacion.Requerimiento
import javax.persistence.*

@Entity
class Party {

    val numeroDeAventureros: Int
        get() {
            return aventureros.size
        }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var nombre: String
    var imagenUrl: String

    @OneToMany(mappedBy = "party", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var aventureros: MutableSet<Aventurero> = HashSet()

    var estaPeleando: Boolean

    constructor(nombre: String, imagenUrl: String) {
        this.nombre = nombre
        this.imagenUrl = imagenUrl
        this.estaPeleando = false
    }

    fun agregarAventurero(aventurero: Aventurero) {
        if (this.aventureros.size < 5) {
            this.aventureros.add(aventurero)
            aventurero.party = this
        } else {
            throw MaximoDeAventurerosEnPartyException()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Party

        if (nombre != other.nombre) return false
        if (imagenUrl != other.imagenUrl) return false
        if (id != other.id) return false

        return true
    }

    fun empezarPelea() {
        if (this.estaPeleando) throw throw MutiplePeleaException()
        this.estaPeleando = true
    }

    fun dejarDePelear() {
        if (!this.estaPeleando) throw NoEstaPeleandoException()
        this.estaPeleando = false
        resetearAventureros()
    }

    private fun resetearAventureros() {
        for (aventurero in this.aventureros) {
            aventurero.volverAEstadoInicial()
        }
    }

    fun removerAventurero(aventurero: Aventurero) {
        this.aventureros.remove(aventurero)
    }

    fun hayAlguienVivo(): Boolean {
        return this.aventureros.any { av -> !av.estaMuerto() }
    }

    fun subirExperienciaYProficiencia() {
        this.aventureros.forEach {
            it.subirExperienciaYProficiencia()
        }
    }

    fun requerimientosDeLaParty(): MutableList<Requerimiento> {
        return aventureros.fold(mutableListOf()) { acc, av ->
            av.clases.forEach { clase ->
                val req = acc.find { it.clase == clase }
                if (req == null) {
                    acc.add(Requerimiento(clase, 1))
                } else {
                    req.cantidadDeAventureros += 1
                }
            }
            acc
        }
    }
}
