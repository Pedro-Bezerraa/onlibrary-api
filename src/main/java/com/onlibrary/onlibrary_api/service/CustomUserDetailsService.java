package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.model.Usuario;
import com.onlibrary.onlibrary_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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

        return new User(
                usuario.getUsername(),
                usuario.getSenha(),
                getAuthority(usuario)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthority(Usuario usuario) {
        GrantedAuthority authority = new SimpleGrantedAuthority(usuario.getRole().name());
        return List.of(authority);
    }
}
