package com.example.contactos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.example.contactos.entity.Usuario;
import com.example.contactos.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
public class RegistrationController {

    private final UsuarioRepository repositorio;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UsuarioRepository repositorio, PasswordEncoder passwordEncoder) {
        this.repositorio = repositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String formulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping("/register")
    public String registrar(@Valid @ModelAttribute("usuario") Usuario usuario,
                            BindingResult resultado) {

        if (repositorio.existsByEmail(usuario.getEmail())) {
            resultado.rejectValue("email", "duplicado", "Ya existe una cuenta con ese email");
        }
        if (resultado.hasErrors()) {
            return "register";
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        repositorio.save(usuario);
        return "redirect:/login";
    }
}