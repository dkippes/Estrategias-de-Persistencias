package helpers

import ar.edu.unq.epers.tactics.spring.service.runner.HibernateTransactionRunner
import ar.edu.unq.epers.tactics.spring.service.runner.HibernateTransactionRunner.runTrx

class DataDAOImpl : DataDAO {

    override fun clear() {
        runTrx { initDatabases() }
    }

    private fun initDatabases() {
        val session = HibernateTransactionRunner.currentSession
        val nombreDeTablas = session.createNativeQuery("show tables").resultList
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS=0;").executeUpdate()
        nombreDeTablas.forEach { result ->
            var tabla = ""
            when (result) {
                is String -> tabla = result
                is Array<*> -> tabla = result[0].toString()
            }
            session.createNativeQuery("truncate table $tabla").executeUpdate()
        }
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS=1;").executeUpdate()
    }
}