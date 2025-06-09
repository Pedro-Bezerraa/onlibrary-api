package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.model.entities.Notificacao;
import com.onlibrary.onlibrary_api.model.entities.Usuario;
import com.onlibrary.onlibrary_api.model.enums.TipoUsuario;
import com.onlibrary.onlibrary_api.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final EmailService emailService;

    public void notificarUsuario(Usuario usuario, String titulo, String conteudo, TipoUsuario tipo) {
        Notificacao notificacao = Notificacao.builder()
                .dataEmissao(LocalDate.now())
                .titulo(titulo)
                .conteudo(conteudo)
                .usuario(usuario)
                .marcadoLido(false)
                .tipo(tipo)
                .build();

        notificacaoRepository.save(notificacao);

        if (usuario.getEmail() != null && !usuario.getEmail().isBlank()) {
            emailService.enviarEmail(usuario.getEmail(), titulo, conteudo);
        }
    }
}
