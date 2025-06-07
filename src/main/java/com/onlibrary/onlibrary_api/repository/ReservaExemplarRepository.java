package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.Exemplar;
import com.onlibrary.onlibrary_api.model.entities.Reserva;
import com.onlibrary.onlibrary_api.model.entities.ReservaExemplar;
import com.onlibrary.onlibrary_api.model.enums.SituacaoReserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservaExemplarRepository extends JpaRepository<ReservaExemplar, UUID> {
    boolean existsByExemplarAndReserva_Situacao(Exemplar exemplar, SituacaoReserva situacao);
}
