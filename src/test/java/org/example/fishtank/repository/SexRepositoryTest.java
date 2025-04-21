package org.example.fishtank.repository;

import org.example.fishtank.model.entity.Sex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
class SexRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgisContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:15-3.3")
                    .asCompatibleSubstituteFor("postgres")
    );

    @Autowired
    SexRepository sexRepository;

    @BeforeEach
    void setUp() {
        Sex textSex = new Sex();
        Sex textSex2 = new Sex();

        textSex.setName("Male");
        textSex2.setName("Female");

        sexRepository.save(textSex);
        sexRepository.save(textSex2);
    }

    @Test
    void findByNameShouldReturnSexWithSameName() {
        String expectedName = "Male";

        String actualName = sexRepository.findByName("Male").getName();

        assertThat(expectedName).isEqualTo(actualName);
    }

    @Test
    void findByNameShouldReturnNullIfSexNotExist() {
        Sex expected = null;

        Sex actual = sexRepository.findByName("Test");

        assertThat(expected).isEqualTo(actual);
    }
}