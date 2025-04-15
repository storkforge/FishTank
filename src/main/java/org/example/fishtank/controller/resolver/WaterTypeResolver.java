package org.example.fishtank.controller.resolver;

import org.example.fishtank.model.entity.WaterType;
import org.example.fishtank.repository.WaterTypeRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class WaterTypeResolver {

    WaterTypeRepository waterTypeRepository;

    public WaterTypeResolver(WaterTypeRepository waterTypeRepository) {
        this.waterTypeRepository = waterTypeRepository;
    }

    @QueryMapping
    public List<WaterType> getAllWaterType() {
        return waterTypeRepository.findAll();
    }

    @QueryMapping
    public WaterType getWaterTypeByName(@Argument String name) {
        return waterTypeRepository.findByName(name);
    }
}
