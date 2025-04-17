/*package org.example.fishtank.repository;

import org.example.fishtank.model.entity.Access;
import org.example.fishtank.model.entity.AppUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
class AppUserRepositoryTest {

//    @Container
//    @ServiceConnection
//    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgisContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:15-3.3")
                    .asCompatibleSubstituteFor("postgres") // critical for compatibility
    );

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AccessRepository accessRepository;

    AppUser expectedAppUser;

    @BeforeEach
    void setUp() {

        Access access = new Access();
        access.setName("Standard");
        accessRepository.save(access);

        expectedAppUser = new AppUser();
        expectedAppUser.setName("name_1");
        expectedAppUser.setPasswordHash("password_1");
        expectedAppUser.setEmail("email@email.com");
        expectedAppUser.setAuthenticationCode("test_name_1");
        expectedAppUser.setAccess(access);

        appUserRepository.save(expectedAppUser);
    }

    @AfterEach
    void tearDown() {
        appUserRepository.deleteAll();
        accessRepository.deleteAll();
    }

    @Test
    @DisplayName("findAll should return a list with expectedAppUser")
    void findAllShouldReturnListOfAllAppUsers() {

        List<AppUser> appUsers = appUserRepository.findAll();

        assertThat(appUsers.size()).isEqualTo(1);
        assertThat(appUsers.getFirst().getName()).isEqualTo("name_1");
    }

    @Test
    @DisplayName("findById should return AppUser with matching id")
    void findByIdShouldReturnAppUserWhenMatch() {

        var actualAppUser = appUserRepository.findById(expectedAppUser.getId());

        assertThat(actualAppUser).isPresent();
        assertThat(actualAppUser.get().getId()).isEqualTo(expectedAppUser.getId());
    }

    @Test
    @DisplayName("findById should return empty Optional if no matching id")
    void findByIdShouldReturnEmptyOptionalWhenNoMatch() {

        var actualAppUser = appUserRepository.findById(expectedAppUser.getId());

        assertThat(actualAppUser).isPresent();
        assertThat(actualAppUser.get().getId()).isEqualTo(expectedAppUser.getId());
    }

    @Test
    @DisplayName("findByAuthenticationCode should return AppUser with matching authenticationCode")
    void findByAuthenticationCodeShouldReturnAppUserWhenMatch() {

        var actualAppUser = appUserRepository.findByAuthenticationCode("test_name_1");

        assertThat(actualAppUser).isPresent();
        assertThat(actualAppUser.get().getAuthenticationCode()).isEqualTo(expectedAppUser.getAuthenticationCode());
    }

    @Test
    @DisplayName("findByAuthenticationCode should return empty Optional if no matching authenticationCode")
    void findByAuthenticationCodeShouldReturnEmptyOptionalWhenNoMatch() {

        var actualAppUser = appUserRepository.findByAuthenticationCode("null");

        assertThat(actualAppUser).isEmpty();
    }

    @Test
    @DisplayName("findByName should return AppUser with matching name")
    void findByNameShouldReturnAppUserWhenMatch() {

        var actualAppUser = appUserRepository.findByName("name_1");

        assertThat(actualAppUser).isPresent();
        assertThat(actualAppUser.get().getName()).isEqualTo(expectedAppUser.getName());
    }

    @Test
    @DisplayName("findByName should return null if no matching name")
    void findByNameShouldReturnEmptyOptionalWhenNoMatch() {

        var actualAppUser = appUserRepository.findByName("null");

        assertThat(actualAppUser).isEmpty();
    }

}
 */
