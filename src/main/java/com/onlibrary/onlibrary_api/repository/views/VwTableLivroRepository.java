package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableLivro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwTableLivroRepository extends JpaRepository<VwTableLivro, UUID> {
}
