package ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.spring

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import org.springframework.data.repository.CrudRepository

interface SpringAventureroDAO : CrudRepository<Aventurero, Long> {
}