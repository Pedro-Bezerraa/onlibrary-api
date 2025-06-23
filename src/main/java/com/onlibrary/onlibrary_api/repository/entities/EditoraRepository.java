package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.Editora;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EditoraRepository extends JpaRepository<Editora, UUID> {
    boolean existsByNome(String nome);
    long countByDeletadoFalse();
    List<Editora> findByDeletadoFalse();
}
