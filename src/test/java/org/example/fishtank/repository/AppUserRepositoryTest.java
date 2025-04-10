/*package org.example.fishtank.repository;

import org.example.fishtank.model.entity.Access;
import org.example.fishtank.model.entity.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
class AppUserRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    AccessRepository accessRepository;

    private Access standardAccess;
    private Access adminAccess;

    @BeforeEach
    void beforeEach() {
        standardAccess = new Access();
        standardAccess.setName("Standard");
        accessRepository.save(standardAccess);

        adminAccess = new Access();
        adminAccess.setName("Admin");
        accessRepository.save(adminAccess);

        AppUser user = new AppUser();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPasswordHash("hashedPassword");
        user.setAccess(standardAccess);
        appUserRepository.save(user);
    }

    @Test
    void TestFindByEmail() {

        AppUser foundUser = appUserRepository.findByEmail("john.doe@example.com").get();

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void TestUpdate() {
        AppUser user = new AppUser();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPasswordHash("originalHash");
        user.setAccess(standardAccess);
        AppUser savedUser = appUserRepository.save(user);

        String newName = "Jane Doe";
        String newPasswordHash = "newHashedPassword";
        String newEmail = "jane.doe@example.com";

        savedUser.setName(newName);
        savedUser.setPasswordHash(newPasswordHash);
        savedUser.setEmail(newEmail);
        savedUser.setAccess(adminAccess);
        appUserRepository.save(savedUser);

        AppUser updatedUser = appUserRepository.findByEmail(newEmail).get();
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo(newName);
        assertThat(updatedUser.getPasswordHash()).isEqualTo(newPasswordHash);
        assertThat(updatedUser.getEmail()).isEqualTo(newEmail);
        assertThat(updatedUser.getAccess().getName()).isEqualTo(adminAccess.getName());
    }

    @Test
    void TestFindByName() {
        AppUser foundUser = appUserRepository.findByName("John Doe").get();

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getName()).isEqualTo("John Doe");
    }
}
 */
