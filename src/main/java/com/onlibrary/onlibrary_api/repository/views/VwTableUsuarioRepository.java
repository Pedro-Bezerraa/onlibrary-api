package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwTableUsuarioRepository extends JpaRepository<VwTableUsuario, UUID> {
}
