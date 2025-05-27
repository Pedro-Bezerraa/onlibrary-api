package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.Exemplar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExemplarRepository extends JpaRepository<Exemplar, UUID> {
}
