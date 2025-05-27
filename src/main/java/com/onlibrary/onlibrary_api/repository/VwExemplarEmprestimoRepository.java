package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.views.VwExemplarEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwExemplarEmprestimoRepository extends JpaRepository<VwExemplarEmprestimo, UUID> {
}
