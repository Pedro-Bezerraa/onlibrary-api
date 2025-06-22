package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwReservaEmprestimoExemplar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwReservaEmprestimoExemplarRepository extends JpaRepository<VwReservaEmprestimoExemplar, UUID> {
}
