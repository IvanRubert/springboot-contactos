package com.example.contactos.repository;

import com.example.contactos.entity.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ContactoRepository extends JpaRepository<Contacto, Integer> {
    List<Contacto> findByTelefono(String telefono);
    Optional<Contacto> findFirstByEmail(String email);
}
