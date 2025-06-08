package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.Biblioteca;
import com.onlibrary.onlibrary_api.model.entities.Exemplar;
import com.onlibrary.onlibrary_api.model.entities.Livro;
import com.onlibrary.onlibrary_api.model.enums.SituacaoExemplar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExemplarRepository extends JpaRepository<Exemplar, UUID> {
    List<Exemplar> findByLivroAndBibliotecaAndSituacao(Livro livro, Biblioteca biblioteca, SituacaoExemplar situacao);

}
