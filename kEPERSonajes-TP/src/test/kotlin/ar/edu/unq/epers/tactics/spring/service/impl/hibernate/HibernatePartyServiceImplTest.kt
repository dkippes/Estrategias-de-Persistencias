package ar.edu.unq.epers.tactics.spring.service.impl.hibernate

import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.spring.service.impl.PartyServiceTest
import helpers.DataDAO
import helpers.DataDAOImpl
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance


@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernatePartyServiceImplTest : PartyServiceTest() {
    lateinit var dataDAO: DataDAO;
    init {
        partyDAO = HibernatePartyDAO()
        partyService = HibernatePartyServiceImpl(partyDAO)
        dataDAO = DataDAOImpl()
    }

    @AfterEach
    override fun cleanup(){
        dataDAO.clear()
    }
}