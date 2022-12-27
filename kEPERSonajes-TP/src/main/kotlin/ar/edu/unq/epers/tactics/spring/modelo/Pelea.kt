package ar.edu.unq.epers.tactics.spring.modelo

import ar.edu.unq.epers.tactics.spring.modelo.habilidades.Habilidad
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Pelea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @OneToOne
    var party: Party?

    var partyEnemiga: String

    var date: LocalDateTime

    var resultado: EstadoResultado = EstadoResultado.UNDEFINED

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var habilidadesUsadas: MutableList<Habilidad> = mutableListOf()

    fun agregarHabilidad(
        habilidad: Habilidad
    ) {
        habilidadesUsadas.add(habilidad)
    }

    constructor(party: Party, partyEnemiga: String) {
        this.party = party
        this.date = LocalDateTime.now()
        this.partyEnemiga = partyEnemiga
    }

    fun dejarDePelear() {
        if (party!!.hayAlguienVivo()) {
            party!!.subirExperienciaYProficiencia()
            resultado = EstadoResultado.GANADOR
        } else {
            resultado = EstadoResultado.PERDEDOR
        }
        party!!.dejarDePelear()
    }

    fun estaLaPeleaTerminada(): Boolean {
        return resultado != EstadoResultado.UNDEFINED
    }

    fun aventureroExisteEnLaPelea(aventurero: Aventurero): Boolean {
        return party!!.aventureros.any { av -> av == aventurero }
    }
}

enum class EstadoResultado(estado: String) {
    GANADOR("ganador"),
    PERDEDOR("perdedor"),
    UNDEFINED("indefinido")
}