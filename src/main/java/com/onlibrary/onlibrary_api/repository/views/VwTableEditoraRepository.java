package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableEditora;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwTableEditoraRepository extends JpaRepository<VwTableEditora, UUID> {
}
