package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableBibliotecaAutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VwTableBibliotecaAutorRepository extends JpaRepository<VwTableBibliotecaAutor, UUID> {
    List<VwTableBibliotecaAutor> findByFkIdBiblioteca(UUID fkIdBiblioteca);
}
