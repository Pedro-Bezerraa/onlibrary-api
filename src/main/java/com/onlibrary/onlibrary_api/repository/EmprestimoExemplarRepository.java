package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.EmprestimoExemplar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmprestimoExemplarRepository extends JpaRepository<EmprestimoExemplar, UUID> {
}
