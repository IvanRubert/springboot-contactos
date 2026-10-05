package com.example.contactos.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.example.contactos.entity.Provincia;
import com.example.contactos.repository.ProvinciaRepository;

@Component
public class StringToProvinciaConverter implements Converter<String, Provincia> {

    private final ProvinciaRepository provinciaRepositorio;

    public StringToProvinciaConverter(ProvinciaRepository provinciaRepositorio) {
        this.provinciaRepositorio = provinciaRepositorio;
    }

    @Override
    public Provincia convert(String id) {
        if (id == null || id.isBlank()) return null;
        return provinciaRepositorio.findById(Integer.parseInt(id)).orElse(null);
    }
}
