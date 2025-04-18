package org.example.fishtank.controller.resolver;

import org.example.fishtank.model.entity.Access;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@GraphQlTest(AppUserResolver.class)
class AppUserResolverTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    AppUserRepository appUserRepository;

    List<AppUser> appUsers = new ArrayList<>();


    @BeforeEach
    void setpUp() {
        Access access = new Access();
        access.setName("Standard");

        AppUser appUser1 = new AppUser();
        appUser1.setId(1);
        appUser1.setName("Test Testsson");
        appUser1.setAccess(access);

        AppUser appUser2 = new AppUser();
        appUser2.setId(2);
        appUser2.setName("Tarzan Tsederqvist");
        appUser2.setAccess(access);

        appUsers.add(appUser1);
        appUsers.add(appUser2);
    }

    @Test
    void getAllAppUser() {
        when(appUserRepository.findAll()).thenReturn(appUsers);

        graphQlTester.document("""
                        query {
                                        getAllAppUser {
                                            id
                                            name
                                            access {
                                                name
                                            }
                                        }
                                    }
                        
                        """)
                .execute()
                .path("getAllAppUser[0].id").entity(Integer.class).isEqualTo(1)
                .path("getAllAppUser[0].name").entity(String.class).isEqualTo("Test Testsson")
                .path("getAllAppUser[0].access.name").entity(String.class).isEqualTo("Standard")
                .path("getAllAppUser[1].id").entity(Integer.class).isEqualTo(2)
                .path("getAllAppUser[1].name").entity(String.class).isEqualTo("Tarzan Tsederqvist")
                .path("getAllAppUser[1].access.name").entity(String.class).isEqualTo("Standard");
    }

    @Test
    void getAppUserByName() {
        AppUser testTestsson = appUsers.get(0);
        when(appUserRepository.findByName("Test Testsson")).thenReturn(Optional.ofNullable(testTestsson));

        graphQlTester.document("""
                query {
                    getAppUserByName(name:"Test Testsson") {
                        id 
                        name
                        access {
                            name
                            }
                        }
                }
                """)
                .execute()
                .path("getAppUserByName.id").entity(Integer.class).isEqualTo(1)
                .path("getAppUserByName.name").entity(String.class).isEqualTo("Test Testsson")
                .path("getAppUserByName.access.name").entity(String.class).isEqualTo("Standard");
    }
}