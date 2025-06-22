package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableBiliotecaGenero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwTableBibliotecaGeneroRepository extends JpaRepository<VwTableBiliotecaGenero, UUID> {
}
