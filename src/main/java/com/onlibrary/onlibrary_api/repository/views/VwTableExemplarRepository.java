package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableExemplar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VwTableExemplarRepository extends JpaRepository<VwTableExemplar, UUID> {
    List<VwTableExemplar> findByFkIdBiblioteca(UUID fkIdBiblioteca);
}
