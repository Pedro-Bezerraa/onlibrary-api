package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableGenero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwTableGeneroRepository extends JpaRepository<VwTableGenero, UUID> {
}
