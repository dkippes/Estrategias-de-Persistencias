package ar.edu.unq.epers.tactics.spring.modelo.habilidades

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn

@Entity
@PrimaryKeyJoinColumn(name="id")
class HabilidadNula(aventureroReceptor: Aventurero, aventureroEmisor: Aventurero) :
    Habilidad(aventureroReceptor, aventureroEmisor) {
    override fun ejecutar() {
    }
}