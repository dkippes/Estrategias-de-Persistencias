package ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.hibernate

import ar.edu.unq.epers.tactics.spring.modelo.Pelea
import ar.edu.unq.epers.tactics.spring.persistencia.dao.PeleaDAO
import ar.edu.unq.epers.tactics.spring.service.runner.HibernateTransactionRunner

class HibernatePeleaDAO : HibernateDAO<Pelea>(Pelea::class.java), PeleaDAO {

    override fun crear(pelea: Pelea): Pelea {
        guardar(pelea)
        return pelea
    }

    override fun encontrarPorParty(partyId: Long): Pelea? {
        val session = HibernateTransactionRunner.currentSession
        val hql = "select i from Pelea i where party_id = :partyId"
        val query = session.createQuery(hql, Pelea::class.java)

        query.setParameter("partyId", partyId)

        if (query.resultList.isNullOrEmpty()) return null

        return query.resultList[0]
    }
}