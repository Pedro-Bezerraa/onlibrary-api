package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableReserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwTableReservaRepository extends JpaRepository<VwTableReserva, UUID> {
}
