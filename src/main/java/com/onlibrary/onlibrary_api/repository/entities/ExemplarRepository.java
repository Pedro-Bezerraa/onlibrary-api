package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.dto.exemplar.ExemplarStatusDTO;
import com.onlibrary.onlibrary_api.model.entities.Biblioteca;
import com.onlibrary.onlibrary_api.model.entities.Exemplar;
import com.onlibrary.onlibrary_api.model.entities.Livro;
import com.onlibrary.onlibrary_api.model.enums.SituacaoExemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ExemplarRepository extends JpaRepository<Exemplar, UUID> {
    List<Exemplar> findByLivroAndBibliotecaAndSituacao(Livro livro, Biblioteca biblioteca, SituacaoExemplar situacao);
    long countByBibliotecaIdAndDeletadoFalse(UUID bibliotecaId);
    List<Exemplar> findByBibliotecaIdAndSituacaoInAndDeletadoFalse(UUID bibliotecaId, List<SituacaoExemplar> situacoes);

    @Query("SELECT new com.onlibrary.onlibrary_api.dto.exemplar.ExemplarStatusDTO(e.id, e.situacao) FROM Exemplar e WHERE e.biblioteca.id = :bibliotecaId AND e.livro.id = :livroId AND e.deletado = false")
    List<ExemplarStatusDTO> findExemplaresStatusByBibliotecaIdAndLivroId(@Param("bibliotecaId") UUID bibliotecaId, @Param("livroId") UUID livroId);
}
