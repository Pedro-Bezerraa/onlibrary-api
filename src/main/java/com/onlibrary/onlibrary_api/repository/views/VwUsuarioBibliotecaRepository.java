package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwUsuarioBiblioteca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwUsuarioBibliotecaRepository extends JpaRepository<VwUsuarioBiblioteca, UUID> {
}
