package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.Exemplar;
import com.onlibrary.onlibrary_api.model.entities.ReservaExemplar;
import com.onlibrary.onlibrary_api.model.enums.SituacaoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReservaExemplarRepository extends JpaRepository<ReservaExemplar, UUID> {
    @Query("""
    SELECT re FROM ReservaExemplar re
    JOIN FETCH re.exemplar e
    WHERE re.reserva.id = :reservaId
""")
    List<ReservaExemplar> findByReservaIdComExemplar(@Param("reservaId") UUID reservaId);

}
