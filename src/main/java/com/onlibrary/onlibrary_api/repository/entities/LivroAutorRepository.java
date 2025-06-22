package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.LivroAutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivroAutorRepository extends JpaRepository<LivroAutor, UUID> {
    void deleteByLivroId(UUID livroId);

}
