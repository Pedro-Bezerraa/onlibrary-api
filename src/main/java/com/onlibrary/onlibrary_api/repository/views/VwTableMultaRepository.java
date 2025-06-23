package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableMulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VwTableMultaRepository extends JpaRepository<VwTableMulta, UUID> {
    List<VwTableMulta> findByFkIdBiblioteca(UUID fkIdBiblioteca);
}
