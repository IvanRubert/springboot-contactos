package com.example.contactos.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.example.contactos.repository.UsuarioRepository;
import com.example.contactos.entity.Usuario;
import java.util.Set;
import java.util.HashSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repositorio;

    public UsuarioDetailsService(UsuarioRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario u = repositorio.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario " + email));

        // Todos los usuarios tienen al menos ROLE_USER
        Set<String> roles = new HashSet<>(u.getRoles());
        roles.add("ROLE_USER");

        Collection<GrantedAuthority> authorities = roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        return new CustomUserDetails(u.getEmail(), u.getPassword(), authorities, u.getNombre());
    }

    public static class CustomUserDetails extends org.springframework.security.core.userdetails.User {
        private final String nombre;

        public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String nombre) {
            super(username, password, authorities);
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }
    }
}