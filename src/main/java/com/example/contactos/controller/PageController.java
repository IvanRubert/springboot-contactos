package com.example.contactos.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import com.example.contactos.repository.ContactoRepository;
import org.springframework.ui.Model;

@Controller
public class PageController {
    private final ContactoRepository repositorio;

    public PageController(ContactoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping("/page")
    public Map<String, String> index() {
        return Map.of(
            "message", "Welcome to your new controller!",
            "path", "src/main/java/com/example/contactos/controller/PageController.java"
        );
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("contactos", repositorio.findAll());
        return "inicio";
    }
}
