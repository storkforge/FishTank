package org.example.fishtank.model.mapper;

import org.example.fishtank.model.dto.appUserDto.ResponseAppUser;
import org.example.fishtank.model.dto.appUserDto.security.LoginAppUser;
import org.example.fishtank.model.entity.Access;
import org.example.fishtank.model.entity.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AppUserMapperTest {

    AppUser appUserTest;

    @BeforeEach
    void setUp() {
        Access access = new Access();
        access.setName("Standard");

        appUserTest = new AppUser();
        appUserTest.setId(1);
        appUserTest.setName("Test");
        appUserTest.setPasswordHash("TestPasswordHash");
        appUserTest.setEmail("Test@gmail.com");
        appUserTest.setAuthenticationCode("Autentication Code");
        appUserTest.setAccess(access);
    }

    @Test
    void mapToResponseAppUserShouldReturnResponseAppUserWithSameValues() {
        ResponseAppUser result = AppUserMapper.mapToResponseAppUser(appUserTest);

        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result.id()).isEqualTo(1),
                () -> assertThat(result.name()).isEqualTo("Test"),
                () -> assertThat(result.access()).isEqualTo("Standard")
        );
    }

    @Test
    void mapNullAppUserToResponseAppUser() {
        ResponseAppUser appUserNull = AppUserMapper.mapToResponseAppUser(null);
        assertNull(appUserNull);
    }

    @Test
    void mapToLoginAppUserShouldReturnLoginUppUserWithSameValues() {

        LoginAppUser result = AppUserMapper.mapToLoginAppUser(appUserTest);

        assertAll(
                () -> assertNotNull(result),
                () -> assertThat(result.id()).isEqualTo("1"),
                () -> assertThat(result.name()).isEqualTo("Test"),
                () -> assertThat(result.password()).isEqualTo("TestPasswordHash"),
                () -> assertThat(result.email()).isEqualTo("Test@gmail.com"),
                () -> assertThat(result.authenticationCode()).isEqualTo("Autentication Code"),
                () -> assertThat(result.access()).isEqualTo("Standard")
        );
    }

    @Test
    void mapNullAppUserToLogInAppUser() {
        LoginAppUser appUserNull = AppUserMapper.mapToLoginAppUser(null);
        assertNull(appUserNull);
    }
}