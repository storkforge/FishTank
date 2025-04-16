package org.example.fishtank.service;

import org.example.fishtank.exception.custom.ResourceNotFoundException;
import org.example.fishtank.exception.message.Message;
import org.example.fishtank.model.dto.appUserDto.CreateAppUser;
import org.example.fishtank.model.dto.appUserDto.ResponseAppUser;
import org.example.fishtank.model.dto.appUserDto.UpdateAppUser;
import org.example.fishtank.model.dto.appUserDto.security.LoginAppUser;
import org.example.fishtank.model.entity.Access;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.model.mapper.AppUserMapper;
import org.example.fishtank.repository.AccessRepository;
import org.example.fishtank.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @InjectMocks
    AppUserService appUserService;

    @Mock
    AppUserRepository appUserRepository;

    @Mock
    AccessRepository accessRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    AppUser expectedAppUser;
    Access expectedAccess;

    @BeforeEach
    void setUp() {

        expectedAccess = new Access();
        expectedAccess.setName("Standard");

        expectedAppUser = new AppUser();
        expectedAppUser.setId(1);
        expectedAppUser.setName("name_1");
        expectedAppUser.setPasswordHash("password_1");
        expectedAppUser.setEmail("email@email.com");
        expectedAppUser.setAuthenticationCode("test_name_1");
        expectedAppUser.setAccess(expectedAccess);
    }

    @Test
    @DisplayName("getLoginAppUserByAuthenticationCode should return LoginAppUser when match")
    void getLoginAppUserByAuthenticationCodeShouldReturnLoginAppUserWhenMatch() {

        when(appUserRepository.findByAuthenticationCode(expectedAppUser.getAuthenticationCode())).thenReturn(Optional.of(expectedAppUser));

        LoginAppUser expectedLoginAppUser = AppUserMapper.mapToLoginAppUser(expectedAppUser);
        LoginAppUser actualLoginAppUser = appUserService.getLoginAppUserByAuthenticationCode(expectedAppUser.getAuthenticationCode());

        assertThat(actualLoginAppUser).isEqualTo(expectedLoginAppUser);
    }

    @Test
    @DisplayName("getLoginAppUserByAuthenticationCode should throw ResourceNotFoundException when no match")
    void getLoginAppUserByAuthenticationCodeShouldThrowResourceNotFoundExceptionWhenNoMatch() {

        when(appUserRepository.findByAuthenticationCode("null")).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> appUserService.getLoginAppUserByAuthenticationCode("null"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(Message.APP_USER.notFound());
    }

    @Test
    @DisplayName("getResponseAppUserById should return ResponseAppUser when match")
    void getResponseAppUserByIdShouldReturnResponseAppUserWhenMatch() {

        when(appUserRepository.findById(expectedAppUser.getId())).thenReturn(Optional.of(expectedAppUser));

        ResponseAppUser expectedResponseAppUser = AppUserMapper.mapToResponseAppUser(expectedAppUser);
        ResponseAppUser actualResponseAppUser = appUserService.getResponseAppUserById(expectedAppUser.getId());

        assertThat(actualResponseAppUser).isEqualTo(expectedResponseAppUser);
    }

    @Test
    @DisplayName("getResponseAppUserById should throw ResourceNotFoundException when no match")
    void getResponseAppUserByIdShouldThrowResourceNotFoundExceptionWhenNoMatch() {

        when(appUserRepository.findById(0)).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> appUserService.getResponseAppUserById(0))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(Message.APP_USER.notFound());
    }

    @Test
    @DisplayName("getAllAppUsersAsResponseList should return List of all AppUsers as ResponseAppUsers")
    void getAllAppUsersAsResponseListShouldReturnAListOfAllAppUsers() {

        when(appUserRepository.findAll()).thenReturn(List.of(expectedAppUser));

        ResponseAppUser expectedResponseAppUser = AppUserMapper.mapToResponseAppUser(expectedAppUser);

        assertThat(appUserService.getAllAppUsersAsResponseList())
                .hasSize(1)
                .containsExactly(expectedResponseAppUser);
    }

    @Test
    @DisplayName("getAllAppUsersAsResponseList should return empty list when no AppUsers")
    void getAllAppUsersAsResponseListShouldReturnEmptyListWhenNoAppUsers() {

        when(appUserRepository.findAll()).thenReturn(List.of());

        assertThat(appUserService.getAllAppUsersAsResponseList()).isEmpty();
    }

    @Test
    @DisplayName("call to save should call AppUserRepository.save()")
    void callToSaveShouldCallAppUserRepositorySave() {

        CreateAppUser createAppUser = new CreateAppUser(
                expectedAppUser.getName(),
                expectedAppUser.getPasswordHash(),
                expectedAppUser.getEmail(),
                expectedAppUser.getAuthenticationCode(),
                expectedAppUser.getAccess().getName()
        );

        when(appUserRepository.findByAuthenticationCode(createAppUser.authenticationCode())).thenReturn(Optional.empty());
        when(accessRepository.findByName(createAppUser.access())).thenReturn(Optional.of(expectedAccess));
        when(passwordEncoder.encode(createAppUser.password())).thenReturn(createAppUser.password());
        when(appUserRepository.save(any(AppUser.class))).thenReturn(expectedAppUser);

        appUserService.save(createAppUser);

        verify(appUserRepository).save(any(AppUser.class));
    }

    @Test
    @DisplayName("call to updateCurrentUser should set the current users values to passed non null values")
    void updateCurrentUserShouldSetCurrentUserValuesToPassedNonNullValues() {

        UpdateAppUser updateAppUser = new UpdateAppUser(
                "UpdateAppUser",
                null,
                "UpdateAppUserPassword"
        );

        String expectedEmail = expectedAppUser.getEmail();
        Authentication authentication = setSecurityContextHolderAndGetAuthentication();
        when(authentication.getName()).thenReturn(expectedAppUser.getId().toString());
        when(appUserRepository.findById(expectedAppUser.getId())).thenReturn(Optional.of(expectedAppUser));
        when(passwordEncoder.encode(updateAppUser.password())).thenReturn(updateAppUser.password());

        appUserService.updateCurrentUser(updateAppUser);

        assertThat(expectedAppUser.getName()).isEqualTo(updateAppUser.name());
        assertThat(expectedAppUser.getEmail()).isEqualTo(expectedEmail);
        assertThat(expectedAppUser.getPasswordHash()).isEqualTo(updateAppUser.password());
    }

    @Test
    void updateCurrentUserAccessShouldSetCurrentUserAccessToPassedAccess() {

        String accessString = "TestAccess";

        Access testAccess = new Access();
        testAccess.setName(accessString);

        Authentication authentication = setSecurityContextHolderAndGetAuthentication();
        when(authentication.getName()).thenReturn(expectedAppUser.getId().toString());
        when(appUserRepository.findById(expectedAppUser.getId())).thenReturn(Optional.of(expectedAppUser));
        when(accessRepository.findByName(anyString())).thenReturn(Optional.of(testAccess));

        appUserService.updateCurrentUserAccess(accessString);

        assertThat(expectedAppUser.getAccess()).isEqualTo(testAccess);
    }

    /**
     * Sets the SecurityContextHolder when called.
     * Used for "mocking" CurrentUser like:
     * Authentication authentication = setSecurityContextHolderAndGetAuthentication();
     * when(authentication.getName()).thenReturn("1");
     * where "1" represents the id of the CurrentUser.
     * @return Authentication
     */
    private Authentication setSecurityContextHolderAndGetAuthentication() {

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }
}
