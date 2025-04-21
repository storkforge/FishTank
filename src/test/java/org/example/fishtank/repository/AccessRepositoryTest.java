package org.example.fishtank.repository;

import org.example.fishtank.model.entity.Access;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import java.util.NoSuchElementException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@Testcontainers
class AccessRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgisContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:15-3.3")
                    .asCompatibleSubstituteFor("postgres")
    );

    @Autowired
    AccessRepository accessRepository;

    @BeforeEach
    void setUp() {
        Access testAccess = new Access();
        Access testAccess2 = new Access();

        testAccess.setName("Standard");
        testAccess2.setName("Premium");

        accessRepository.save(testAccess);
        accessRepository.save(testAccess2);
    }

    @Test
    void findByNameShouldReturnAccessWithSameName() {
        String expectedName = "Standard";

        String actualName = accessRepository.findByName("Standard").get().getName();

        assertThat(expectedName).isEqualTo(actualName);
    }


    @Test
    void findByNameShouldThrowExceptionIfNoAccessExist() {
        assertThatThrownBy(() -> accessRepository.findByName("Test").get())
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No value present");
    }
}