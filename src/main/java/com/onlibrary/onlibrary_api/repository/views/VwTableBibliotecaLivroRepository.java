package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableBibliotecaLivro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwTableBibliotecaLivroRepository extends JpaRepository<VwTableBibliotecaLivro, UUID> {
}
