package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.*;
import com.onlibrary.onlibrary_api.model.enums.SituacaoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservaRepository extends JpaRepository<Reserva, UUID> {
    Optional<Reserva> findByUsuarioAndSituacao(Usuario usuario, SituacaoReserva situacao);

    @Query("""
    SELECT r FROM Reserva r
    WHERE r.livro.id = :livroId AND r.biblioteca.id = :bibliotecaId AND r.situacao = :situacao
    ORDER BY r.dataEmissao ASC
""")
    List<Reserva> findReservasPorLivroEBibliotecaComSituacao(
            @Param("livroId") UUID livroId,
            @Param("bibliotecaId") UUID bibliotecaId,
            @Param("situacao") SituacaoReserva situacao
    );

    @Query("SELECT COUNT(e) > 0 FROM Emprestimo e WHERE e.reserva.id = :reservaId AND e.situacao = 'PENDENTE'")
    boolean hasPendingEmprestimosByReservaId(@Param("reservaId") UUID reservaId);

    boolean existsByBibliotecaIdAndSituacao(UUID idBiblioteca, SituacaoReserva situacao);
}
