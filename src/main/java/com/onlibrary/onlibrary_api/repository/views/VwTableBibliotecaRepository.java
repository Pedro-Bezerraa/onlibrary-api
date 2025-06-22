package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableBiblioteca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwTableBibliotecaRepository extends JpaRepository<VwTableBiblioteca, UUID> {
}
