package com.example.contactos.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;
import com.example.contactos.repository.ContactoRepository;
import com.example.contactos.entity.Contacto;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.List;
import com.example.contactos.repository.ProvinciaRepository;
import com.example.contactos.entity.Provincia;

@Controller
public class ContactoController {

    private final ContactoRepository contactoRepositorio;
    private final ProvinciaRepository provinciaRepositorio;

    @ModelAttribute("provincias")
    public List<Provincia> provincias() {
        return provinciaRepositorio.findAll();
    }

    // Spring inyecta automáticamente el contactoRepositorio
    public ContactoController(ContactoRepository contactoRepositorio, ProvinciaRepository provinciaRepositorio) {
        this.contactoRepositorio = contactoRepositorio;
        this.provinciaRepositorio = provinciaRepositorio;
    }

    @GetMapping(value = { "/contacto", "/contacto/", "/contacto/{codigo:[0-9]+}" })
    public String ficha(@PathVariable(required = false) Integer codigo, Model model) {
        if (codigo == null) {
            return "redirect:/contacto/1";
        }

        Contacto contacto = contactoRepositorio.findById(codigo).orElse(null);
        model.addAttribute("contacto", contacto);
        return "ficha_contacto";
    }

    @GetMapping("/contacto/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("contacto", new Contacto());
        model.addAttribute("titulo", "Nuevo contacto");
        model.addAttribute("accion", "/contacto/nuevo");
        return "formulario_contacto";
    }

    @PostMapping("/contacto/nuevo")
    public String guardar(@Valid @ModelAttribute("contacto") Contacto contacto,
                        BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            model.addAttribute("titulo", "Nuevo contacto");
            model.addAttribute("accion", "/contacto/nuevo");
            return "formulario_contacto";
        }
        contactoRepositorio.save(contacto);
        return "redirect:/contacto/" + contacto.getId();
    }

    @GetMapping("/contacto/editar/{codigo:[0-9]+}")
    public String editar(@PathVariable Integer codigo, Model model) {
        Contacto contacto = contactoRepositorio.findById(codigo).orElse(null);
        if (contacto == null) {
            model.addAttribute("contacto", null);
            return "ficha_contacto";
        }
        model.addAttribute("contacto", contacto);
        model.addAttribute("titulo", "Modificar contacto");
        model.addAttribute("accion", "/contacto/editar/" + codigo);
        return "formulario_contacto";
    }

    @PostMapping("/contacto/editar/{codigo:[0-9]+}")
    public String actualizar(@PathVariable Integer codigo,
                            @Valid @ModelAttribute("contacto") Contacto datos,
                            BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            model.addAttribute("titulo", "Modificar contacto");
            model.addAttribute("accion", "/contacto/editar/" + codigo);
            return "formulario_contacto";
        }
        Contacto contacto = contactoRepositorio.findById(codigo).orElseThrow();
        // Copiamos solo los campos del formulario sobre el objeto existente
        contacto.setNombre(datos.getNombre());
        contacto.setTelefono(datos.getTelefono());
        contacto.setEmail(datos.getEmail());
        contacto.setProvincia(datos.getProvincia());
        contactoRepositorio.save(contacto);
        return "redirect:/contacto/" + codigo;
    }

    @GetMapping("/contacto/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        try {
            contactoRepositorio.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            // p. ej., el contacto tiene datos relacionados que impiden borrarlo
            return "redirect:/contacto/" + id;
        }
        return "redirect:/";
    }
}