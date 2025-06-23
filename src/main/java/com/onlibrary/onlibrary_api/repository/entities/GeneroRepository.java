package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GeneroRepository extends JpaRepository<Genero, UUID> {
    boolean existsByNome(String nome);
    long countByDeletadoFalse();
    List<Genero> findByDeletadoFalse();
}
