package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.contato.ContatoRequestDTO;
import com.onlibrary.onlibrary_api.dto.contato.ContatoResponseDTO;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Contato;
import com.onlibrary.onlibrary_api.model.entities.Usuario;
import com.onlibrary.onlibrary_api.repository.entities.ContatoRepository;
import com.onlibrary.onlibrary_api.repository.entities.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContatoService {
    private final ContatoRepository contatoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public void marcarComoConcluido(UUID contatoId) {
        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de suporte não encontrado."));

        contato.setConcluido(true);
        contatoRepository.save(contato);
    }

    @Transactional
    public ContatoResponseDTO criarContato(ContatoRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Contato contato = new Contato();
        contato.setUsuario(usuario);
        contato.setDataEmissao(OffsetDateTime.now());
        contato.setConteudo(dto.conteudo());

        Contato savedContato = contatoRepository.save(contato);

        return new ContatoResponseDTO(
                savedContato.getId(),
                savedContato.getDataEmissao(),
                savedContato.getUsuario().getId(),
                savedContato.getConteudo()
        );
    }

    @Transactional
    public void marcarComoLido(UUID contatoId) {
        Contato contato = contatoRepository.findById(contatoId)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de suporte não encontrado."));

        contato.setMarcadoLido(true);
        contatoRepository.save(contato);
    }
}