package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.model.entities.Usuario;
import com.onlibrary.onlibrary_api.repository.entities.UsuarioRepository;
import com.onlibrary.onlibrary_api.security.UsuarioDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String identificacao) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(identificacao)
                .or(() -> usuarioRepository.findByEmail(identificacao))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));

        if (usuario.getDeletado()) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }

        return new UsuarioDetails(usuario, getAuthority(usuario));
    }

    private Collection<? extends GrantedAuthority> getAuthority(Usuario usuario) {
        if (usuario.getTipo() == null) {
            return List.of(new SimpleGrantedAuthority("ROLE_COMUM"));
        }

        String role = "ROLE_" + usuario.getTipo().name();
        return List.of(new SimpleGrantedAuthority(role));
    }
}
