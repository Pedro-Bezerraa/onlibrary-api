package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BibliotecaRepository extends JpaRepository<Biblioteca, UUID> {
}
