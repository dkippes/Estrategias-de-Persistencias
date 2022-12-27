package ar.edu.unq.epers.tactics.spring.service.impl.hibernate

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.spring.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.spring.service.AventureroService
import ar.edu.unq.epers.tactics.spring.service.exception.AventureroIdNullException
import ar.edu.unq.epers.tactics.spring.service.runner.HibernateTransactionRunner.runTrx

class HibernateAventureroServiceImpl(val aventureroDAO: AventureroDAO, val partyDAO: PartyDAO) : AventureroService {

    override fun actualizar(aventurero: Aventurero): Aventurero {
        noSePuedeAgregarAventureroConIdNull(aventurero)
        return runTrx {
            aventureroDAO.actualizar(aventurero)
        }
    }

    override fun recuperar(idDelAventurero: Long): Aventurero {
        return runTrx { aventureroDAO.recuperar(idDelAventurero) }
    }

    override fun eliminar(aventureroId: Long) {
        runTrx {
            val aventurero = aventureroDAO.recuperar(aventureroId)
            aventureroDAO.eliminar(aventurero)
        }
    }
    private fun noSePuedeAgregarAventureroConIdNull(aventurero: Aventurero) {
        if (aventurero.id == null) {
            throw AventureroIdNullException()
        }
    }
}