package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwTableCategoriaRepository extends JpaRepository<VwTableCategoria, UUID> {
}
