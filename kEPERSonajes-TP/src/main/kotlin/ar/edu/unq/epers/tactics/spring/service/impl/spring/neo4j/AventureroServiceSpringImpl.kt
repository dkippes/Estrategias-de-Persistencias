package ar.edu.unq.epers.tactics.spring.service.impl.spring.neo4j

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.spring.SpringAventureroDAO
import ar.edu.unq.epers.tactics.spring.service.AventureroService
import ar.edu.unq.epers.tactics.spring.service.exception.AventureroIdNullException
import ar.edu.unq.epers.tactics.spring.service.exception.AventureroNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AventureroServiceSpringImpl() : AventureroService {
    @Autowired
    private lateinit var aventureroDAO: SpringAventureroDAO
    override fun actualizar(aventurero: Aventurero): Aventurero {
        if (aventurero.id == null) throw AventureroIdNullException()
        if (!aventureroDAO.existsById(aventurero.id!!)) throw AventureroNotFoundException(aventurero.id!!)
        return aventureroDAO.save(aventurero)
    }

    override fun recuperar(idDelAventurero: Long): Aventurero {
        return aventureroDAO.findById(idDelAventurero).orElseThrow { AventureroNotFoundException(idDelAventurero) }
    }

    override fun eliminar(aventureroId: Long) {
        if (!aventureroDAO.existsById(aventureroId)) throw AventureroNotFoundException(aventureroId)
        aventureroDAO.deleteById(aventureroId)
    }

    fun clearAll() {
        aventureroDAO.deleteAll()
    }
}