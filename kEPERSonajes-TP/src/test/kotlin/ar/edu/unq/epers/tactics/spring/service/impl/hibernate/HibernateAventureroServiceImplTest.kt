package ar.edu.unq.epers.tactics.spring.service.impl.hibernate

import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.hibernate.HibernateAventureroDAO
import ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.spring.service.impl.AventureroServiceTest
import helpers.DataDAO
import helpers.DataDAOImpl
import org.junit.jupiter.api.*

@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernateAventureroServiceImplTest : AventureroServiceTest() {
    lateinit var dataDAO: DataDAO;
    init {
        val partyDAO = HibernatePartyDAO()
        partyService = HibernatePartyServiceImpl(partyDAO)
        val aventureroDAO = HibernateAventureroDAO()
        aventureroService = HibernateAventureroServiceImpl(aventureroDAO, partyDAO)
        dataDAO = DataDAOImpl()
    }

    @AfterEach
    override fun cleanup(){
        dataDAO.clear()
    }
}