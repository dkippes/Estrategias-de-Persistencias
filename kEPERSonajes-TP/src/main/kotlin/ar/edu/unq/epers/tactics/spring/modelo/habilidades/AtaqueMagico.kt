package ar.edu.unq.epers.tactics.spring.modelo.habilidades

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.randomizador.Random
import ar.edu.unq.epers.tactics.spring.modelo.randomizador.Randomizador
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn
import javax.persistence.Transient

@Entity
@PrimaryKeyJoinColumn(name="id")
class AtaqueMagico : Habilidad {
    val nivelEmisor: Int
    val poderMagicoEmisor: Double
    @Transient
    val randomizador: Random

    constructor(
        aventureroReceptor: Aventurero,
        aventureroEmisor: Aventurero?,
        nivelEmisor: Int,
        poderMagicoEmisor: Double,
        randomizador: Random
    ) : super(aventureroReceptor, aventureroEmisor) {
        this.nivelEmisor = nivelEmisor
        this.poderMagicoEmisor = poderMagicoEmisor
        this.randomizador = randomizador
    }

    override fun ejecutar() {
        if (puedeAtacarA(aventureroReceptor) && this.fueAtaqueExistoso()) {
            if (this.aventureroReceptor.tieneDefensor() && (!this.aventureroReceptor.defensor!!.estaMuerto())) {
                this.aventureroReceptor.defensor?.percibirEfectosPorSerAtacado(poderMagicoEmisor)
                this.aventureroReceptor.defensor = null
            } else {
                this.aventureroReceptor.percibirEfectosPorSerAtacado(poderMagicoEmisor)
            }
        }
    }

    private fun puedeAtacarA(receptor: Aventurero): Boolean {
        return this.aventureroEmisor != receptor
    }

    fun fueAtaqueExistoso(): Boolean {
        return (randomizador.lanzarDados() + nivelEmisor) >= aventureroReceptor.getVelocidad() / 2
    }
}