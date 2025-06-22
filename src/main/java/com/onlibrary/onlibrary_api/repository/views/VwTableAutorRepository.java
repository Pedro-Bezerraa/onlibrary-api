package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableAutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwTableAutorRepository extends JpaRepository<VwTableAutor, UUID> {
}
