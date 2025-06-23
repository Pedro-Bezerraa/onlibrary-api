package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwBibliotecaReservaExemplar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VwBibliotecaReservaExemplarRepository extends JpaRepository<VwBibliotecaReservaExemplar, UUID> {
    List<VwBibliotecaReservaExemplar> findByFkIdLivro(UUID fkIdLivro);

}
