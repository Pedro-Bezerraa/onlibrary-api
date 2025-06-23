package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableBibliotecaCategoria;
import com.onlibrary.onlibrary_api.model.views.VwTableBibliotecaEditora;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VwTableBibliotecaCategoriaRepository extends JpaRepository<VwTableBibliotecaCategoria, UUID> {
    List<VwTableBibliotecaCategoria> findByFkIdBiblioteca(UUID fkIdBiblioteca);
}
