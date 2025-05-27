package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.views.VwTabelaUsuarioBiblioteca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwTabelaUsuarioBibliotecaRepository extends JpaRepository<VwTabelaUsuarioBiblioteca, UUID> {
}
