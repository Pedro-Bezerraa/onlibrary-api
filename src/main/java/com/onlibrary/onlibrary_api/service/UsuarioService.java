package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Usuario;
import com.onlibrary.onlibrary_api.repository.entities.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public String getUsuarioTipo(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        return (usuario.getTipo() != null) ? usuario.getTipo().name() : "COMUM";
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getUsuarioDependenciesForUpdate(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Map<String, Object> dependencies = new HashMap<>();
        dependencies.put("nome", usuario.getNome());
        dependencies.put("sobrenome", usuario.getSobrenome());
        dependencies.put("cpf", usuario.getCpf());
        dependencies.put("username", usuario.getUsername());
        dependencies.put("email", usuario.getEmail());

        Map<String, String> tipo = new HashMap<>();
        tipo.put("label", usuario.getTipo().toLower()); //
        tipo.put("value", usuario.getTipo().name());
        dependencies.put("tipo", tipo);

        Map<String, String> situacao = new HashMap<>();
        situacao.put("label", usuario.getSituacao().toLower()); //
        situacao.put("value", usuario.getSituacao().name());
        dependencies.put("situacao", situacao);

        return dependencies;
    }
}
