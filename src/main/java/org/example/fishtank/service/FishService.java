package org.example.fishtank.service;

import jakarta.transaction.Transactional;
import org.example.fishtank.model.dto.fishDto.CreateFish;
import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.fishDto.UpdateFish;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.model.entity.Fish;
import org.example.fishtank.model.entity.Sex;
import org.example.fishtank.model.entity.WaterType;
import org.example.fishtank.model.mapper.FishMapper;
import org.example.fishtank.repository.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class FishService {

    private FishRepository fishRepository;
    private WaterTypeRepository waterTypeRepository;
    private SexRepository sexRepository;
    private AppUserRepository appUserRepository;

    public FishService(FishRepository fishRepository, WaterTypeRepository waterTypeRepository, SexRepository sexRepository, AppUserRepository appUserRepository) {
        this.fishRepository = fishRepository;
        this.waterTypeRepository = waterTypeRepository;
        this.sexRepository = sexRepository;
        this.appUserRepository = appUserRepository;
    }

    @Cacheable(value = "fish", key = "#id")
    public ResponseFish findById(Integer id) {
        return fishRepository.findById(id)
                .map(FishMapper::map)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fish not found"));
    }


    public List<ResponseFish> getFishByPost(List<ResponsePost> postList) {
        return postList.stream()
                .map(post -> fishRepository.findById(post.fishId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fish not found")))
                .map(FishMapper::map)
                .filter(Objects::nonNull)
                .toList();
    }

    @Cacheable("allFish")
    public List<ResponseFish> getAllFish() {
        return fishRepository.findAll()
                .stream()
                .map(FishMapper::map)
                .filter(Objects::nonNull)
                .toList();
    }

    @CacheEvict(value = {"allFish"}, allEntries = true)
    public void save(CreateFish createFish) {

        Optional<AppUser> appUser = appUserRepository.findByName(createFish.appUser());
        WaterType waterType = waterTypeRepository.findByName(createFish.waterType());
        Sex sex = sexRepository.findByName(createFish.sex());
        if (appUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        if (waterType == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Water Type not found");
        if (sex == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sex not found");
        Fish fish = FishMapper.map(createFish, waterType, sex, appUser.orElse(null));
        fishRepository.save(fish);
    }

    @CacheEvict(value = {"fish", "allFish"}, key = "#id", allEntries = true)
    public void update(int id, UpdateFish fish) {
        Fish oldFish = fishRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Fish not found"));
        FishMapper.map(fish, oldFish);
        fishRepository.update(oldFish.getName(), oldFish.getDescription(), oldFish.getId());
    }

    @CacheEvict(value = {"fish", "allFish"}, key = "#id", allEntries = true)
    public void delete(int id) {
        var fish = fishRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Fish not found"));
        fishRepository.delete(fish);
    }

}
