package ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.hibernate

import ar.edu.unq.epers.tactics.spring.service.runner.HibernateTransactionRunner
import javax.persistence.EntityNotFoundException


open class HibernateDAO<T>(private val entityType: Class<T>) {
    open fun guardar(entity: T) {
        val session = HibernateTransactionRunner.currentSession
        session.save(entity)
    }

    fun recuperar(id: Long): T {
        val session = HibernateTransactionRunner.currentSession
        return session.get(entityType, id) ?: throw EntityNotFoundException()
    }

    fun actualizar(entity: T): T {
        val session = HibernateTransactionRunner.currentSession
        session.update(entity)

        return entity
    }
}
