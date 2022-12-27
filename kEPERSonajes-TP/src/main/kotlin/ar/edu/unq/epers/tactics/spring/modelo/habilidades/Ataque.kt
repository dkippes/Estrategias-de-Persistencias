package ar.edu.unq.epers.tactics.spring.modelo.habilidades

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.randomizador.Random
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn
import javax.persistence.Transient

@Entity
@PrimaryKeyJoinColumn(name = "id")
class Ataque : Habilidad {
    val precisionFisica: Double
    val danoFisico: Double
    @Transient
    val randomizador: Random

    constructor(
        aventureroReceptor: Aventurero,
        aventureroEmisor: Aventurero?,
        precisionFisica: Double,
        danoFisico: Double,
        randomizador: Random
    ) : super(aventureroReceptor, aventureroEmisor) {
        this.precisionFisica = precisionFisica
        this.danoFisico = danoFisico
        this.randomizador = randomizador
    }

    private fun armaduraDelReceptor(receptor: Aventurero): Double = receptor.getArmadura()

    private fun velocidadDeReceptor(receptor: Aventurero): Double = receptor.getVelocidad()

    override fun ejecutar() {
        if (puedeAtacarA(aventureroReceptor) && esAtaqueExitosoSobre(aventureroReceptor)) {
            if (this.aventureroReceptor.tieneDefensor() && (!this.aventureroReceptor.defensor!!.estaMuerto())) {
                this.aventureroReceptor.defensor?.percibirEfectosPorSerAtacado(danoFisico)
                this.aventureroReceptor.defensor = null
            } else {
                this.aventureroReceptor.percibirEfectosPorSerAtacado(danoFisico)
            }
        }
    }

    protected fun puedeAtacarA(receptor: Aventurero): Boolean {
        return this.aventureroEmisor != receptor
    }

    fun esAtaqueExitosoSobre(receptor: Aventurero): Boolean {
        return randomizador!!.lanzarDados() + this.precisionFisica > armaduraDelReceptor(receptor) + velocidadDeReceptor(
            receptor
        ) / 2
    }
}