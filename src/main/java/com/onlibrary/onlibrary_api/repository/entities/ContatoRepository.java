package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContatoRepository extends JpaRepository<Contato, UUID> {
    List<Contato> findAllByDeletadoFalseOrderByDataEmissaoAsc();
}
