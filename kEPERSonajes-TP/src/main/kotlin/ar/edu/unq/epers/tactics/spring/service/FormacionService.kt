package ar.edu.unq.epers.tactics.spring.service

import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.modelo.formacion.AtributoDeFormacion
import ar.edu.unq.epers.tactics.spring.modelo.formacion.Formacion
import ar.edu.unq.epers.tactics.spring.modelo.formacion.Requerimiento

/*
    TODO: Se cambio el parametro de partyId por Party ya que al interactuar con el PartyDao (MySql),
    TODO: traia problemas de transacciones y lanzaba el error ConcurrentModificationException al querer
    TODO: borrar la base de datos de PartyService (Eso si se ejecutaba mongo primero).
*/
interface FormacionService {
    fun crearFormacion(nombreFormacion: String, requerimientos: List<Requerimiento>, stats: List<AtributoDeFormacion>): Formacion
    fun todasLasFormaciones(): List<Formacion>
    fun atributosQueCorresponden(party: Party): List<AtributoDeFormacion>
    fun formacionesQuePosee(party: Party): List<Formacion>
}
