package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.Multa;
import com.onlibrary.onlibrary_api.model.enums.SituacaoMulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface MultaRepository extends JpaRepository<Multa, UUID> {
    @Query("SELECT COUNT(m) > 0 FROM Multa m WHERE m.id = :multaId AND m.situacao = 'PENDENTE' AND m.deletado = FALSE")
    boolean existsPendingMultaById(@Param("multaId") UUID multaId);

    boolean existsByBibliotecaIdAndSituacao(UUID idBiblioteca, SituacaoMulta situacao);
    boolean existsByUsuarioIdAndBibliotecaIdAndSituacao(UUID usuarioId, UUID bibliotecaId, SituacaoMulta situacao);

    long countByBibliotecaIdAndDeletadoFalse(UUID bibliotecaId);

    boolean existsByBibliotecaIdAndSituacaoAndDataVencimentoBefore(UUID bibliotecaId, SituacaoMulta situacao, LocalDate data);
}
