package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.views.VwTableEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwTableEmprestimoRepository extends JpaRepository<VwTableEmprestimo, UUID> {
}
