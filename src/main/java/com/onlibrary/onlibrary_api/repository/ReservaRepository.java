package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.Livro;
import com.onlibrary.onlibrary_api.model.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservaRepository extends JpaRepository<Reserva, UUID> {
    @Query("SELECT r FROM Reserva r " +
            "JOIN r.exemplares e " +
            "WHERE r.situacao = :situacao AND e.exemplar.livro = :livro " +
            "ORDER BY r.dataEmissao ASC")
    Optional<Reserva> findFirstBySituacaoAndLivroOrderByDataEmissaoAsc(
            @Param("situacao") String situacao,
            @Param("livro") Livro livro);

    @Query("SELECT r FROM Reserva r " +
            "WHERE r.situacao = :situacao AND EXISTS " +
            "(SELECT 1 FROM ReservaExemplar re WHERE re.reserva = r AND re.exemplar.livro = :livro) " +
            "ORDER BY r.dataEmissao ASC")
    List<Reserva> findBySituacaoAndLivroOrderByDataEmissaoAsc(
            @Param("situacao") String situacao,
            @Param("livro") Livro livro);

    @Query("SELECT r FROM Reserva r WHERE r.livro.id = :livroId AND r.situacao = 'PENDENTE' ORDER BY r.dataEmissao ASC")
    List<Reserva> findReservasPendentesPorLivro(@Param("livroId") UUID livroId);
}
