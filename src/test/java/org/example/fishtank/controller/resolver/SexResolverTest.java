package org.example.fishtank.controller.resolver;

import org.example.fishtank.model.entity.Sex;
import org.example.fishtank.repository.SexRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;

@GraphQlTest(SexResolver.class)
class SexResolverTest {

    @Autowired
    GraphQlTester graphQlTester;

    @MockBean
    SexRepository sexRepository;

    List<Sex> sexList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Sex sex1 = new Sex();
        Sex sex2 = new Sex();
        sex1.setName("Male");
        sex2.setName("Female");

        sexList.add(sex1);
        sexList.add(sex2);
    }

    @Test
    void getAllSex() {
        when(sexRepository.findAll()).thenReturn(sexList);

        graphQlTester.document("""
                    query {
                        getAllSex {
                            name
                        }
                    }
                """)
                .execute()
                .path("getAllSex[0].name").entity(String.class).isEqualTo("Male")
                .path("getAllSex[1].name").entity(String.class).isEqualTo("Female");
    }

    @Test
    void getSexByName() {
        Sex male = sexList.get(0);
        when(sexRepository.findByName("Male")).thenReturn(male);

        graphQlTester.document("""
                    query {
                        getSexByName(name: "Male") {
                            name
                        }
                    }
                """)
                .execute()
                .path("getSexByName.name").entity(String.class).isEqualTo("Male");
    }
}