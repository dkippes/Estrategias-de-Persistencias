package ar.edu.unq.epers.tactics.spring.modelo.habilidades

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import javax.persistence.*

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
open abstract class Habilidad {
    @OneToOne(fetch=FetchType.EAGER)
    var aventureroReceptor: Aventurero
    @OneToOne(fetch=FetchType.EAGER)
    var aventureroEmisor: Aventurero?
    constructor(aventureroReceptor: Aventurero, aventureroEmisor: Aventurero?) {
        this.aventureroReceptor = aventureroReceptor
        this.aventureroEmisor = aventureroEmisor
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     var id: Long? = null

    abstract fun ejecutar()
}