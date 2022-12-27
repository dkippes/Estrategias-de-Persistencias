package ar.edu.unq.epers.tactics.spring.modelo.habilidades

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.excepcion.CuracionInvalidaException
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn

@Entity
@PrimaryKeyJoinColumn(name="id")
class Curacion : Habilidad {
    val poderMagicoEmisor: Double

    constructor(aventureroReceptor: Aventurero, aventureroEmisor: Aventurero?, poderMagicoEmisor: Double) : super(
        aventureroReceptor,
        aventureroEmisor
    ) {
        this.poderMagicoEmisor = poderMagicoEmisor
    }

    override fun ejecutar() {
        if(noPuedeCurar()){
            throw CuracionInvalidaException();
        }
        aventureroReceptor.percibirEfectosPorSerCurado(poderMagicoEmisor)
    }

    fun noPuedeCurar(): Boolean {
        return   aventureroReceptor.estaMuerto()
    }
}