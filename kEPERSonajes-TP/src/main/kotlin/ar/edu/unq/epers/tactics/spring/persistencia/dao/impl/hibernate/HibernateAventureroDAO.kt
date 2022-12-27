package ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.hibernate

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.spring.service.runner.HibernateTransactionRunner

class HibernateAventureroDAO : HibernateDAO<Aventurero>(Aventurero::class.java), AventureroDAO {
    override fun eliminar(entity: Aventurero) {
        val session = HibernateTransactionRunner.currentSession
        session.delete(entity)
    }
}
