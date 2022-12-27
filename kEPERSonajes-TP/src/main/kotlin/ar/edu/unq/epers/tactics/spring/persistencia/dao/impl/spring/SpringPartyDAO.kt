package ar.edu.unq.epers.tactics.spring.persistencia.dao.impl.spring

import ar.edu.unq.epers.tactics.spring.modelo.Party
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface SpringPartyDAO : PagingAndSortingRepository<Party, Long> {

    @Query(nativeQuery = true, value = "SELECT p.*, IFNULL(sum(3 * a.nivel + 2 * attr.fuerza + (attr.destreza / 2) + attr.destreza + attr.inteligencia), 0) as poder " +
            "FROM party as p " +
            "LEFT JOIN aventurero as a on a.party_id = p.id " +
            "LEFT JOIN atributos as attr on attr.id = a.atributos_id " +
            "group by p.id "
    )
    fun findAllByOrderByPoderDeLaParty(pag: Pageable): List<Party>


    @Query(nativeQuery = true, value = "SELECT pa.*, SUM(CASE WHEN pe.resultado = '1' THEN 1 ELSE 0 END) as derrotas  FROM epers_tactics.party as pa " +
            "LEFT JOIN pelea as pe on pe.party_id = pa.id " +
            "GROUP BY pa.id " )
    fun findAllByOrderByDerrotas(pag: Pageable): List<Party>


    @Query(nativeQuery = true, value = "SELECT pa.*, SUM(CASE WHEN pe.resultado = '0' THEN 1 ELSE 0 END) as victorias  FROM epers_tactics.party as pa " +
            "LEFT JOIN pelea as pe on pe.party_id = pa.id " +
            "GROUP BY pa.id ")
    fun findAllByOrderByVictorias(pag: Pageable): List<Party>

}
