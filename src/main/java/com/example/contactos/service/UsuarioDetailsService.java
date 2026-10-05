package com.example.contactos.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.example.contactos.repository.UsuarioRepository;
import com.example.contactos.entity.Usuario;
import java.util.Set;
import java.util.HashSet;

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

        return org.springframework.security.core.userdetails.User
            .withUsername(u.getEmail())
            .password(u.getPassword())
            .authorities(roles.toArray(new String[0]))
            .build();
    }
}