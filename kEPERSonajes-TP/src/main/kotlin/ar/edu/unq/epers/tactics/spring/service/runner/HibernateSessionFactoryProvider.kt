package ar.edu.unq.epers.tactics.spring.service.runner

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

class HibernateSessionFactoryProvider private constructor() {
    private val sessionFactory: SessionFactory?

    init {
        val env = System.getenv()
        val user = "root"
        val password = "root"
        val dataBase = "kEPERSonajes_hibernate"
        val host = "localhost"

        val url = env.getOrDefault(
            "SQL_URL",
            "jdbc:mysql://$host:3306/$dataBase?createDatabaseIfNotExist=true&serverTimezone=UTC"
        )
        val dialect = env.getOrDefault("HIBERNATE_DIALECT", "org.hibernate.dialect.MySQL8Dialect")
        val driver = env.getOrDefault("SQL_DRIVER", "com.mysql.cj.jdbc.Driver")

        val configuration = Configuration()
        configuration.configure("hibernate.cfg.xml")
        configuration.setProperty("hibernate.connection.username", user)
        configuration.setProperty("hibernate.connection.password", password)
        configuration.setProperty("hibernate.connection.url", url)
        configuration.setProperty("connection.driver_class", driver)
        configuration.setProperty("dialect", dialect)
        this.sessionFactory = configuration.buildSessionFactory()
    }

    fun createSession(): Session {
        return this.sessionFactory!!.openSession()
    }

    companion object {

        private var INSTANCE: HibernateSessionFactoryProvider? = null

        val instance: HibernateSessionFactoryProvider
            get() {
                if (INSTANCE == null) {
                    INSTANCE =
                        HibernateSessionFactoryProvider()
                }
                return INSTANCE!!
            }

        fun destroy() {
            if (INSTANCE != null && INSTANCE!!.sessionFactory != null) {
                INSTANCE!!.sessionFactory!!.close()
            }
            INSTANCE = null
        }
    }
}