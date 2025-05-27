package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.Editora;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EditoraRepository extends JpaRepository<Editora, UUID> {
}
