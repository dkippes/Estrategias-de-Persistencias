package ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.hibernate

import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.spring.service.runner.HibernateTransactionRunner

open class HibernatePartyDAO : HibernateDAO<Party>(Party::class.java), PartyDAO {

    override fun crear(party: Party): Party {
        guardar(party)
        return party
    }

    override fun recuperarTodas(): List<Party> {
        val session = HibernateTransactionRunner.currentSession

        val hql = """
                    from Party
        """

        val query = session.createQuery(hql, Party::class.java)
        return query.resultList
    }
}