package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.views.VwTabelaPerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwTabelaPerfilUsuarioRepository extends JpaRepository<VwTabelaPerfilUsuario, UUID> {
}
