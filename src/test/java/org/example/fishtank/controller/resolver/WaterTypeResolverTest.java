package org.example.fishtank.controller.resolver;

import org.example.fishtank.model.entity.WaterType;
import org.example.fishtank.repository.WaterTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;

@GraphQlTest(WaterTypeResolver.class)
class WaterTypeResolverTest {

    @Autowired
    GraphQlTester graphQlTester;

    @MockBean
    WaterTypeRepository waterTypeRepository;
    ;

    List<WaterType> waterTypeList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        WaterType waterType1 = new WaterType();
        WaterType waterType2 = new WaterType();
        waterType1.setName("Fresh Water");
        waterType2.setName("Salt Water");

        waterTypeList.add(waterType1);
        waterTypeList.add(waterType2);
    }

    @Test
    void getAllWaterType() {
        when(waterTypeRepository
                .findAll()).thenReturn(waterTypeList);

        graphQlTester.document("""
                            query {
                                getAllWaterType {
                                    name
                                }
                            }
                        """)
                .execute()
                .path("getAllWaterType[0].name").entity(String.class).isEqualTo("Fresh Water")
                .path("getAllWaterType[1].name").entity(String.class).isEqualTo("Salt Water");
    }

    @Test
    void getWaterTypeByName() {
        WaterType freshWater = waterTypeList.get(0);
        when(waterTypeRepository
                .findByName("Fresh Water")).thenReturn(freshWater);

        graphQlTester.document("""
                            query {
                                getWaterTypeByName(name: "Fresh Water") {
                                    name
                                }
                            }
                        """)
                .execute()
                .path("getWaterTypeByName.name").entity(String.class).isEqualTo("Fresh Water");
    }
}