package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableBibliotecaAutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwTableBibliotecaAutorRepository extends JpaRepository<VwTableBibliotecaAutor, UUID> {
}
