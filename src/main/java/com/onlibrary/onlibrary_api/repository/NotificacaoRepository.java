package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificacaoRepository extends JpaRepository<Notificacao, UUID> {
}
