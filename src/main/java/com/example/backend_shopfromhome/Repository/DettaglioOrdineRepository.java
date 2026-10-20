
package com.example.backend_shopfromhome.Repository;

import com.example.backend_shopfromhome.Model.DettaglioOrdine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DettaglioOrdineRepository extends JpaRepository<DettaglioOrdine, Long> {

    List<DettaglioOrdine> findByOrdine_Id(Long ordineId);

}
