package org.example.fishtank.service;

import org.example.fishtank.model.dto.fishDto.CreateFish;
import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.fishDto.UpdateFish;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.entity.*;
import org.example.fishtank.model.mapper.FishMapper;
import org.example.fishtank.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FishServiceTest {

    @InjectMocks
    FishService fishService;

    @Mock
    FishRepository fishRepository;
    @Mock
    SexRepository sexRepository;
    @Mock
    WaterTypeRepository waterTypeRepository;
    @Mock
    AppUserRepository appUserRepository;

    Fish fishTest = new Fish();
    WaterType waterTypeTest = new WaterType();
    Sex sexTest = new Sex();
    AppUser appUserTest = new AppUser();
    String imageTest = "testImage.png";

    Integer userID = 1;

    private MockedStatic<CurrentUser> mockedStatic;


    @BeforeEach
    void setUp() {
        fishTest.setId(1);
        fishTest.setName("testFishName");
        fishTest.setSpecies("testFishSpecies");
        fishTest.setDescription("testFishDescription");
        fishTest.setWaterType(waterTypeTest);
        fishTest.setAppUser(appUserTest);
        fishTest.setSex(sexTest);
        fishTest.setImage(imageTest);

        waterTypeTest.setId(1);
        waterTypeTest.setName("Salt Water");

        appUserTest.setId(1);
        appUserTest.setName("testAppUser");

        sexTest.setId(1);
        sexTest.setName("testSex");


        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        mockedStatic = mockStatic(CurrentUser.class);
        mockedStatic.when(CurrentUser::getId).thenReturn(userID);
    }
    @AfterEach
            void tearDown() {
        mockedStatic.close();
    }

    @Test
    @DisplayName("FindById returns correct responseFish with correct values")
    void findByIdReturnsCorrectResponseFishWithCorrectValues() {
        ResponseFish responseFish = new ResponseFish(
                1,
                "testFishName",
                "testFishSpecies",
                "testFishDescription",
                "Salt Water",
                "testSex",
                "testAppUser",
                "testImage.png");

        when(fishRepository.findById(1)).thenReturn(Optional.of(fishTest));
        assertEquals(responseFish, fishService.findById(1));
    }

    @Test
    @DisplayName("NotFound is thrown when findById can not find the fish")
    void notFoundIsThrownWhenFindByIdCanNotFindTheFish() {
        when(fishRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                fishService.findById(1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Fish not found", exception.getReason());
    }

    @Test
    @DisplayName("FindMyFishById finds my fish")
    void findMyFishByIdFindsMyFish() {
        Integer fishID = 1;
        when(fishRepository.findById(fishID)).thenReturn(Optional.of(fishTest));
        ResponseFish responseFish = fishService.findMyFishById(fishID);
        assertNotNull(responseFish);
        assertEquals(fishID, responseFish.id());
    }


    @Test
    @DisplayName("Get Fish by post returns two list with matching index of fish and post")
    void getFishByPost() {

        ResponsePost post1 = new ResponsePost(1, "testText", 1);
        ResponsePost post2 = new ResponsePost(2, "testText2", 2);

        Fish fish2 = new Fish();
        fish2.setId(2);
        fish2.setName("testFishName2");
        fish2.setSpecies("testFishSpecies2");
        fish2.setDescription("testFishDescription2");
        fish2.setWaterType(waterTypeTest);
        fish2.setAppUser(appUserTest);
        fish2.setSex(sexTest);
        fish2.setImage(imageTest);
        ResponseFish responseFish1 = FishMapper.map(fishTest);
        ResponseFish responseFish2 = FishMapper.map(fish2);

        when(fishRepository.findById(1)).thenReturn(Optional.of(fishTest));
        when(fishRepository.findById(2)).thenReturn(Optional.of(fish2));

        List<ResponseFish> result = fishService.getFishByPost(List.of(post1, post2));

        assertEquals(2, result.size());
        assertEquals(responseFish1, result.get(0));
        assertEquals(responseFish2, result.get(1));
    }

    @Test
    @DisplayName("GetMyFish returns my fish")
    void getMyFishReturnsMyFish(){

        java.util.List<Fish> fishes = new ArrayList<>();
        Fish fishTest2 = new Fish();
        fishTest2.setId(2);
        fishTest2.setName("testFishName");
        fishTest2.setSpecies("testFishSpecies");
        fishTest2.setDescription("testFishDescription");
        fishTest2.setWaterType(waterTypeTest);
        fishTest2.setAppUser(appUserTest);
        fishTest2.setSex(sexTest);
        fishTest2.setImage(imageTest);
        fishes.add(fishTest);
        fishes.add(fishTest2);

        List<ResponseFish> responseFishes = new ArrayList<>();
        ResponseFish responseFish = new ResponseFish(
                1,
                "testFishName",
                "testFishSpecies",
                "testFishDescription",
                "Salt Water",
                "testSex",
                "testAppUser",
                "testImage.png");
        ResponseFish responseFish2 = new ResponseFish(2,
                "testFishName",
                "testFishSpecies",
                "testFishDescription",
                "Salt Water",
                "testSex",
                "testAppUser",
                "testImage.png");

        responseFishes.add(responseFish);
        responseFishes.add(responseFish2);


        when(fishRepository.findByAppUserId(appUserTest.getId())).thenReturn(fishes);

        assertEquals(responseFishes, fishService.getMyFish());
    }

    @Test
    @DisplayName("Get all Fish returns list of response fishes")
    void getAllFishReturnsListOfResponseFishes() {
        java.util.List<Fish> fishes = new ArrayList<>();
        Fish fishTest2 = new Fish();
        fishTest2.setId(2);
        fishTest2.setName("testFishName");
        fishTest2.setSpecies("testFishSpecies");
        fishTest2.setDescription("testFishDescription");
        fishTest2.setWaterType(waterTypeTest);
        fishTest2.setAppUser(appUserTest);
        fishTest2.setSex(sexTest);
        fishTest2.setImage(imageTest);

        List<ResponseFish> responseFishes = new ArrayList<>();
        ResponseFish responseFish = new ResponseFish(
                1,
                "testFishName",
                "testFishSpecies",
                "testFishDescription",
                "Salt Water",
                "testSex",
                "testAppUser",
                "testImage.png");
        ResponseFish responseFish2 = new ResponseFish(2,
                "testFishName",
                "testFishSpecies",
                "testFishDescription",
                "Salt Water",
                "testSex",
                "testAppUser",
                "testImage.png");

        responseFishes.add(responseFish);
        responseFishes.add(responseFish2);

        fishes.add(fishTest);
        fishes.add(fishTest2);

        when(fishRepository.findAll()).thenReturn(fishes);
        assertEquals(responseFishes, fishService.getAllFish());
    }

    @Test
    @DisplayName("save adds a new fish with correct values")
    void saveAddsANewFishWithCorrectValues() {
        CreateFish createFish = new CreateFish(
                "testFishName",
                "testFishSpecies",
                "testFishDescription",
                "Salt Water",
                "testSex",
                "testAppUser",
                "testImage.png");
        Fish fish = FishMapper.map(createFish, waterTypeTest, sexTest, appUserTest);
        when(appUserRepository.findByName(appUserTest.getName())).thenReturn(Optional.ofNullable(appUserTest));
        when(sexRepository.findByName(sexTest.getName())).thenReturn(sexTest);
        when(waterTypeRepository.findByName(waterTypeTest.getName())).thenReturn(waterTypeTest);

        fishService.save(createFish);
        ArgumentCaptor<Fish> fishCaptor = ArgumentCaptor.forClass(Fish.class);
        verify(fishRepository, times(1)).save(fishCaptor.capture());
        Fish capturedFish = fishCaptor.getValue();

        assertEquals(fish.getId(), capturedFish.getId());
        assertEquals(fish.getName(), capturedFish.getName());
        assertEquals(fish.getSpecies(), capturedFish.getSpecies());
        assertEquals(fish.getDescription(), capturedFish.getDescription());
        assertEquals(fish.getWaterType(), capturedFish.getWaterType());
        assertEquals(fish.getAppUser(), capturedFish.getAppUser());
        assertEquals(fish.getImage(), capturedFish.getImage());
    }

    @Test
    @DisplayName("save throws NotFound when appUserRep can not find user")
    void saveThrowsNotFoundWhenAppUserRepCanNotFindUser() {
        CreateFish createFish1 = new CreateFish(
                "testFishName",
                "testFishSpecies",
                "testFishDescription",
                "Salt Water",
                "testSex",
                "testAppUser",
                "testImage.png");

        when(appUserRepository.findByName(appUserTest.getName())).thenReturn(Optional.empty());
        when(sexRepository.findByName(sexTest.getName())).thenReturn(sexTest);
        when(waterTypeRepository.findByName(createFish1.waterType())).thenReturn(waterTypeTest);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                fishService.save(createFish1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }

    @Test
    @DisplayName("save throws NotFound when WaterTypeRep can not find Water type")
    void saveThrowsNotFoundWhenWaterTypeRepCanNotFindWaterTyp() {
        CreateFish createFish1 = new CreateFish(
                "testFishName",
                "testFishSpecies",
                "testFishDescription",
                "Salt Water",
                "testSex",
                "testAppUser",
                "testImage.png");

        when(appUserRepository.findByName(appUserTest.getName())).thenReturn(Optional.ofNullable(appUserTest));
        when(sexRepository.findByName(sexTest.getName())).thenReturn(sexTest);
        when(waterTypeRepository.findByName(createFish1.waterType())).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                fishService.save(createFish1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Water Type not found", exception.getReason());
    }

    @Test
    @DisplayName("save throws NotFound when sexRep can not find Sex")
    void saveThrowsNotFoundWhenSexRepCanNotFindSex() {
        CreateFish createFish1 = new CreateFish(
                "testFishName",
                "testFishSpecies",
                "testFishDescription",
                "Salt Water",
                "testSex",
                "testAppUser",
                "testImage.png");

        when(appUserRepository.findByName(appUserTest.getName())).thenReturn(Optional.ofNullable(appUserTest));
        when(sexRepository.findByName(sexTest.getName())).thenReturn(null);
        when(waterTypeRepository.findByName(createFish1.waterType())).thenReturn(waterTypeTest);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                fishService.save(createFish1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Sex not found", exception.getReason());
    }

    @Test
    @DisplayName("update Updates fish and rep.update is only called once")
    void updateUpdatesFishAndRepUpdateIsOnlyCalledOnce() {

        UpdateFish updateFish = new UpdateFish("updatedFish", "updatedDescription");
        when(fishRepository.findById(1)).thenReturn(Optional.of(fishTest));

        fishService.update(fishTest.getId(), updateFish);

        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> descriptionCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(fishRepository, times(1))
                .update(nameCaptor.capture(), descriptionCaptor.capture(), idCaptor.capture());

        assertEquals("updatedFish", nameCaptor.getValue());
        assertEquals("updatedDescription", descriptionCaptor.getValue());
        assertEquals(1, idCaptor.getValue());
    }

    @Test
    @DisplayName("update throws NotFound when fishRep can not find Fish")
    void updateThrowsNotFoundWhenFishRepCanNotFindFish() {
        UpdateFish updateFish = new UpdateFish("updateTest", "updateDescription");
        when(fishRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                fishService.update(1, updateFish));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Fish not found", exception.getReason());
    }

    @Test
    @DisplayName("delete removes the fish when it exists")
    void deleteRemovesTheFishWhenItExists() {
        when(fishRepository.findById(1)).thenReturn(Optional.of(fishTest));
        fishService.delete(fishTest.getId());
        verify(fishRepository, times(1)).delete(fishTest);
    }


    @Test
    @DisplayName("delete throws NotFound when fishRep can not find Fish")
    void deleteThrowsNotFoundWhenFishRepCanNotFindFish() {
        when(fishRepository.findById(1)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                fishService.delete(1));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Fish not found", exception.getReason());

    }
}
