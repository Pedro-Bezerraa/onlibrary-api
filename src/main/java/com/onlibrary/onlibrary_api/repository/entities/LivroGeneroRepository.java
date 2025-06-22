package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.LivroGenero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivroGeneroRepository extends JpaRepository<LivroGenero, UUID> {
    void deleteByLivroId(UUID livroId);

}
