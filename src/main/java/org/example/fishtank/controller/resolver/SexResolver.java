package org.example.fishtank.controller.resolver;

import org.example.fishtank.model.entity.Sex;
import org.example.fishtank.repository.SexRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SexResolver {

    SexRepository sexRepository;

    public SexResolver(SexRepository sexRepository) {
        this.sexRepository = sexRepository;
    }

    @QueryMapping
    public List<Sex> getAllSex() {
        return sexRepository.findAll();
    }

    @QueryMapping
    public Sex getSexByName(@Argument String name) {
        return sexRepository.findByName(name);
    }
}
