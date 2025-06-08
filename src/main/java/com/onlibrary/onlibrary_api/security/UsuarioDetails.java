package com.onlibrary.onlibrary_api.security;

import com.onlibrary.onlibrary_api.model.entities.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class UsuarioDetails extends Usuario implements UserDetails {

    private final Collection<? extends GrantedAuthority> authorities;

    public UsuarioDetails(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
        super(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getUsername());
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return super.getSenha();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
