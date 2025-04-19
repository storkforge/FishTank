package org.example.fishtank.controller.resolver;

import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.repository.AppUserRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AppUserResolver {

    AppUserRepository appUserRepository;

    public AppUserResolver(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @QueryMapping
    public List<AppUser> getAllAppUser() {
        return appUserRepository.findAll();
    }

    @QueryMapping
    public AppUser getAppUserByName(@Argument String name) {
        return  appUserRepository.findByName(name).orElse(null);
    }
}
