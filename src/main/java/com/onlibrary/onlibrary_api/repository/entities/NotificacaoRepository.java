package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.Biblioteca;
import com.onlibrary.onlibrary_api.model.entities.Notificacao;
import com.onlibrary.onlibrary_api.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificacaoRepository extends JpaRepository<Notificacao, UUID> {
    List<Notificacao> findByUsuarioOrderByDataEmissaoAsc(Usuario usuario);
    List<Notificacao> findByUsuarioAndBibliotecaOrderByDataEmissaoAsc(Usuario usuario, Biblioteca biblioteca);
}
