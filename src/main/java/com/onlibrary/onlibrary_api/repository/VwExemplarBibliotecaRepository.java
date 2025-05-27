package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.views.VwExemplarBiblioteca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwExemplarBibliotecaRepository extends JpaRepository<VwExemplarBiblioteca, UUID> {
}
