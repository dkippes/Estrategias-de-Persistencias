package ar.edu.unq.epers.tactics.spring.modelo.habilidades

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.excepcion.NoSePuedeDefenderException
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn

@Entity
@PrimaryKeyJoinColumn(name="id")
class Defensa(aventureroReceptor: Aventurero, aventureroEmisor: Aventurero) :
    Habilidad(aventureroReceptor, aventureroEmisor) {
    override fun ejecutar() {
        if (elIdDelEmisorEsNulo() || elEmisorEsElMismoQueElReceptor() || aventureroReceptor.tieneDefensor()) {
            throw NoSePuedeDefenderException(aventureroReceptor.id!!)
        }
        aventureroReceptor.percibirEfectosPorSerDefendido(aventureroEmisor!!)
    }
    private fun elIdDelEmisorEsNulo(): Boolean {
        return aventureroEmisor!!.id == null
    }

    private fun elEmisorEsElMismoQueElReceptor(): Boolean {
        return aventureroEmisor!!.id == aventureroReceptor.id
    }
}