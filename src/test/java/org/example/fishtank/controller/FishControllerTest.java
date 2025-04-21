package org.example.fishtank.controller;

import org.example.fishtank.model.dto.appUserDto.ResponseAppUser;
import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.service.AppUserService;
import org.example.fishtank.service.CurrentUser;
import org.example.fishtank.service.FishService;
import org.example.fishtank.service.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(FishController.class)
@AutoConfigureMockMvc
class FishControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    FishService fishService;

    @MockitoBean
    ImageService imageService;

    @MockitoBean
    AppUserService appUserService;

    @Test
    @DisplayName("GET /my_fishes returns view with fish list")
    @WithMockUser
    void getMyFishesShouldReturnViewWithFishList() throws Exception {
        when(fishService.getMyFish()).thenReturn(List.of());

        mockMvc.perform(get("/my_fishes"))
                .andExpect(status().isOk())
                .andExpect(view().name("my_fishes"))
                .andExpect(model().attributeExists("fishList"));
    }

    @Test
    @DisplayName("GET /my_fishes_rough/{id} returns selected fish as JSON")
    @WithMockUser
    void getMyFishesThroughIdReturnsSelectedFishAsJson() throws Exception {
        int fishId = 1;

        ResponseFish mockFish = new ResponseFish(
                fishId,
                "Ville",
                "Betta splendens",
                "Fin fisk med fint hjärta",
                "Freshwater",
                "Male",
                "villeken",
                "betta.jpg"
        );

        when(fishService.findMyFishById(fishId)).thenReturn((mockFish));

        mockMvc.perform(get("/my_fishes_rough/{id}", fishId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(fishId))
                .andExpect(jsonPath("$.name").value("Ville"))
                .andExpect(jsonPath("$.species").value("Betta splendens"))
                .andExpect(jsonPath("$.description").value("Fin fisk med fint hjärta"))
                .andExpect(jsonPath("$.waterType").value("Freshwater"))
                .andExpect(jsonPath("$.sex").value("Male"))
                .andExpect(jsonPath("$.appUser").value("villeken"))
                .andExpect(jsonPath("$.image").value("betta.jpg"));

    }

    @Test
    @DisplayName("GET /my_fishes_rough returns list of fishes as JSON")
    @WithMockUser
    void getMyFishesRoughShouldReturnJsonList() throws Exception {
        List<ResponseFish> fishList = List.of(
                new ResponseFish(1, "Ville", "Gädda", "En cool fisk", "Saltwater", "Male", "user", "ville.jpg"),
                new ResponseFish(2, "Vilhelm", "Abborre", "En dum fisk", "Saltwater", "Female", "user", "vilhelm.jpg")
        );

        when(fishService.getMyFish()).thenReturn(fishList);

        mockMvc.perform(get("/my_fishes_rough"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fishList[0].name").value("Ville"))
                .andExpect(jsonPath("$.fishList[1].name").value("Vilhelm"));
    }

    @Test
    @DisplayName("GET /add_fish returns view with appuser in model")
    @WithMockUser
    void getAddFishShouldReturnViewWithAppuser() throws Exception {

        int userId = 1;
        var mockUser = new ResponseAppUser(userId, "testUser", "Standard");

        try (MockedStatic<CurrentUser> currentUserMock = Mockito.mockStatic(CurrentUser.class)) {
            currentUserMock.when(CurrentUser::getId).thenReturn(userId);

            when(appUserService.getResponseAppUserById(userId)).thenReturn(mockUser);

            mockMvc.perform(get("/add_fish"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("add_fish"))
                    .andExpect(model().attributeExists("appuser"))
                    .andExpect(model().attribute("appuser", mockUser));

        }

    }

    @Test
    @DisplayName("POST /add_fish should save fish and redirect to /my_fishes")
    @WithMockUser
    void addFishShouldSaveAndRedirect() throws Exception {
        int userId = 1;
        String username = "mockuser";
        String access = "Premium";
        String imageName = "savedImage.jpg";

        String name = "Ville";
        String species = "Betta splendens";
        String description = "Hej hopp";
        String waterType = "Freshwater";
        String sex = "Female";

        var fishImage = new MockMultipartFile("fishImage", "goldie.jpg", MediaType.IMAGE_JPEG_VALUE, "dummy".getBytes());
        var mockUser = new ResponseAppUser(userId, username, access);

        try (MockedStatic<CurrentUser> currentUserMock = Mockito.mockStatic(CurrentUser.class)) {
            currentUserMock.when(CurrentUser::getId).thenReturn(userId);

            when(appUserService.getResponseAppUserById(userId)).thenReturn(mockUser);
            when(imageService.saveImage(any())).thenReturn(imageName);
            when(fishService.getFishCountByAppUser(userId)).thenReturn(1);

            mockMvc.perform(multipart("/add_fish")
                            .file(fishImage)
                            .param("name", name)
                            .param("species", species)
                            .param("description", description)
                            .param("watertype", waterType)
                            .param("sex", sex)
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/my_fishes"));

        }

    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void addFishShouldUpgradeToPremiumWhenFishCountIsGreaterThanFive() throws Exception {
        int userId = 1;
        String username = "testuser";
        String access = "Standard";

        var mockUser = new ResponseAppUser(userId, username, access);
        var fishImage = new MockMultipartFile("fishImage", "fish.jpg", MediaType.IMAGE_JPEG_VALUE, "image-data".getBytes());

        try (MockedStatic<CurrentUser> currentUserMock = Mockito.mockStatic(CurrentUser.class)) {
            currentUserMock.when(CurrentUser::getId).thenReturn(userId);

            when(appUserService.getResponseAppUserById(userId)).thenReturn(mockUser);
            when(imageService.saveImage(any())).thenReturn("savedImage.jpg");
            when(fishService.getFishCountByAppUser(userId)).thenReturn(6);
            when(appUserService.loadUserByUsername(username))
                    .thenReturn(User.withUsername(username).password("password").roles("PREMIUM").build());

            mockMvc.perform(multipart("/add_fish")
                            .file(fishImage)
                            .param("name", "Ville")
                            .param("species", "Betta splendens")
                            .param("description", "Hej hopp")
                            .param("watertype", "Freshwater")
                            .param("sex", "Female")
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/my_fishes"));

            verify(appUserService).updateAppUserToAccessPremium(userId);
        }
    }


}