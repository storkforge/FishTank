package org.example.fishtank.controller.resolver;

import org.example.fishtank.model.entity.Access;
import org.example.fishtank.repository.AccessRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class AccessResolver {

    AccessRepository accessRepository;

    public AccessResolver(AccessRepository accessRepository) {
        this.accessRepository = accessRepository;
    }

    @QueryMapping
    public List<Access> getAllAccess() {
        return accessRepository.findAll();
    }

    @QueryMapping
    public Access getAccessByName(@Argument String name) {
        return accessRepository.findByName(name).orElse(null);
    }
}
