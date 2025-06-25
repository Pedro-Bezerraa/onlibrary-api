package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.contato.SuporteResponseDTO;
import com.onlibrary.onlibrary_api.dto.notificacao.NotificacaoResponseDTO;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Biblioteca;
import com.onlibrary.onlibrary_api.model.entities.Notificacao;
import com.onlibrary.onlibrary_api.model.entities.Usuario;
import com.onlibrary.onlibrary_api.model.enums.TipoUsuario;
import com.onlibrary.onlibrary_api.repository.entities.BibliotecaRepository;
import com.onlibrary.onlibrary_api.repository.entities.ContatoRepository;
import com.onlibrary.onlibrary_api.repository.entities.NotificacaoRepository;
import com.onlibrary.onlibrary_api.repository.entities.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacaoService {
    private final NotificacaoRepository notificacaoRepository;
    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;
    private final BibliotecaRepository bibliotecaRepository;
    private final ContatoRepository contatoRepository;

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

    @Transactional(readOnly = true)
    public List<?> getNotificacoes(UUID usuarioId, UUID bibliotecaId, String tipo) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        return switch (tipo.toLowerCase()) {
            case "comum" -> notificacaoRepository.findByUsuarioAndBibliotecaIsNullOrderByDataEmissaoAsc(usuario)
                    .stream()
                    .map(NotificacaoResponseDTO::new)
                    .collect(Collectors.toList());
            case "biblioteca" -> {
                if (bibliotecaId == null) {
                    throw new BusinessException("O 'bibliotecaId' é obrigatório para o tipo 'biblioteca'.");
                }
                Biblioteca biblioteca = bibliotecaRepository.findById(bibliotecaId)
                        .orElseThrow(() -> new ResourceNotFoundException("Biblioteca não encontrada."));
                yield notificacaoRepository.findByUsuarioAndBibliotecaOrderByDataEmissaoAsc(usuario, biblioteca)
                        .stream()
                        .map(NotificacaoResponseDTO::new)
                        .collect(Collectors.toList());
            }
            case "admin" -> contatoRepository.findAllByOrderByDataEmissaoAsc()
                    .stream()
                    .map(SuporteResponseDTO::new)
                    .collect(Collectors.toList());
            default -> throw new BusinessException("Tipo de notificação inválido: " + tipo);
        };
    }

    @Transactional
    public void marcarComoLida(UUID notificacaoId) {
        Notificacao notificacao = notificacaoRepository.findById(notificacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Notificação não encontrada."));

        notificacao.setMarcadoLido(true);
        notificacaoRepository.save(notificacao);
    }
}
