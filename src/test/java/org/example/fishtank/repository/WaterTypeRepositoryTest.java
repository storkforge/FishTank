package org.example.fishtank.repository;

import org.example.fishtank.model.entity.WaterType;
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
class WaterTypeRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgisContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:15-3.3")
                    .asCompatibleSubstituteFor("postgres")
    );

    @Autowired
    WaterTypeRepository waterTypeRepository;

    @BeforeEach
    void setUp() {
        WaterType waterType = new WaterType();
        WaterType waterType1 = new WaterType();

        waterType.setName("Fresh Water");
        waterType1.setName("Salt Water");

        waterTypeRepository.save(waterType);
        waterTypeRepository.save(waterType1);
    }


    @Test
    void findByNameShouldReturnWaterTypeWithSameName() {
        String expectedName = "Fresh Water";

        String actualName = waterTypeRepository.findByName("Fresh Water").getName();

        assertThat(expectedName).isEqualTo(actualName);
    }

    @Test
    void findByNameShouldReturnNullIfWaterTypeNotExist() {
        WaterType expected = null;

        WaterType actual = waterTypeRepository.findByName("Test Water");

        assertThat(expected).isEqualTo(actual);
    }
}