package org.example.fishtank.controller.resolver;

import org.example.fishtank.model.entity.Access;
import org.example.fishtank.repository.AccessRepository;
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

@GraphQlTest(AccessResolver.class)
class AccessResolverTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private AccessRepository accessRepository;

    List<Access> accessList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Access access1 = new Access();
        access1.setName("Standard");

        Access access2 = new Access();
        access2.setName("Premium");

        accessList.add(access1);
        accessList.add(access2);
    }

    @Test
    void shouldReturnAllAccess() {
        when(accessRepository.findAll()).thenReturn(accessList);

        graphQlTester.document("""
                            query {
                                getAllAccess {
                                    name
                                }
                            }
                        """)
                .execute()
                .path("getAllAccess[0].name").entity(String.class).isEqualTo("Standard")
                .path("getAllAccess[1].name").entity(String.class).isEqualTo("Premium");
    }

    @Test
    void shouldReturnAccessByName() {
        Access standardAccess = accessList.get(0);
        when(accessRepository.findByName("Standard")).thenReturn(Optional.ofNullable(standardAccess));

        graphQlTester.document("""
                            query {
                                getAccessByName(name:"Standard") {
                                    name
                                }
                            }
                        """)
                .execute()
                .path("getAccessByName.name").entity(String.class).isEqualTo("Standard");
    }
}