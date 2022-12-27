package ar.edu.unq.epers.tactics.spring.service.impl.hibernate

import ar.edu.unq.epers.tactics.spring.modelo.Aventurero
import ar.edu.unq.epers.tactics.spring.modelo.Party
import ar.edu.unq.epers.tactics.spring.modelo.Pelea
import ar.edu.unq.epers.tactics.spring.modelo.habilidades.Habilidad
import ar.edu.unq.epers.tactics.spring.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.spring.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.spring.persistencia.dao.PeleaDAO
import ar.edu.unq.epers.tactics.spring.service.PeleaService
import ar.edu.unq.epers.tactics.spring.service.PeleasPaginadas
import ar.edu.unq.epers.tactics.spring.service.runner.HibernateTransactionRunner.runTrx

class HibernatePeleaServiceImpl(val peleaDAO: PeleaDAO, val partyDAO: PartyDAO, val aventureroDAO: AventureroDAO) :
    PeleaService {

    override fun iniciarPelea(idDeLaParty: Long, partyEnemiga: String): Pelea {
        return runTrx {
            val party = partyDAO.recuperar(idDeLaParty)
            party.empezarPelea()
            val pelea = Pelea(party, "")
            partyDAO.actualizar(party)
            peleaDAO.crear(pelea)
            pelea
        }
    }

    override fun estaEnPelea(partyId: Long): Boolean {
        return runTrx {
            val peleaEncontrada: Pelea? = peleaDAO.encontrarPorParty(partyId)
            peleaEncontrada != null
        }
    }

    override fun actualizar(pelea: Pelea): Pelea {
        return runTrx {
            peleaDAO.actualizar(pelea)
        }
    }

    override fun recuperar(idDeLaPelea: Long): Pelea {
        return runTrx {
            peleaDAO.recuperar(idDeLaPelea)
        }
    }

    override fun recuperarOrdenadas(partyId: Long, pagina: Int?): PeleasPaginadas {
        TODO("Not yet implemented")
    }

    override fun resolverTurno(peleaId: Long, aventureroId: Long, enemigos: List<Aventurero>): Habilidad {
        TODO()
//        return runTrx {
//            val aventurero = aventureroDAO.recuperar(aventureroId)
//            peleaDAO.recuperar(peleaId)
//
//            aventurero.ejecutarTactica(enemigos)
//        }
    }

    override fun recibirHabilidad(aventureroId: Long, habilidad: Habilidad): Aventurero {
        TODO()
//        return runTrx {
//            val receptor = aventureroDAO.recuperar(aventureroId)
//            habilidad.ejecutar(receptor)
//
//            aventureroDAO.actualizar(habilidad.emisor)
//            aventureroDAO.actualizar(receptor)
//        }
    }

    override fun terminarPelea(partyId: Long): Party {

        return runTrx {

            val party = partyDAO.recuperar(partyId)
            party.dejarDePelear()
            val partyActualizada = partyDAO.actualizar(party)

            for (aventurero in party.aventureros){
                aventureroDAO.actualizar(aventurero)
            }
            partyActualizada
        }

    }
}