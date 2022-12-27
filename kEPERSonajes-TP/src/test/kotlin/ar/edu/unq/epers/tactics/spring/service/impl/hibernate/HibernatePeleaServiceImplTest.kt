package ar.edu.unq.epers.tactics.spring.service.impl.hibernate

import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.hibernate.HibernateAventureroDAO
import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.hibernate.HibernatePeleaDAO
import ar.edu.unq.epers.tactics.spring.service.impl.PeleaServiceTest
import helpers.DataDAO
import helpers.DataDAOImpl
import org.junit.jupiter.api.*

@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernatePeleaServiceImplTest : PeleaServiceTest() {
    lateinit var dataDAO: DataDAO;
    init {
        val partyDAO = HibernatePartyDAO()
        val aventureroDAO = HibernateAventureroDAO()
        val peleaDAO = HibernatePeleaDAO()
        peleaService = HibernatePeleaServiceImpl(peleaDAO, partyDAO, aventureroDAO)
        partyService = HibernatePartyServiceImpl(partyDAO)
        aventureroService = HibernateAventureroServiceImpl(aventureroDAO, partyDAO)
        dataDAO = DataDAOImpl()
    }

    @AfterEach
    override fun cleanup(){
        dataDAO.clear()
    }
}
