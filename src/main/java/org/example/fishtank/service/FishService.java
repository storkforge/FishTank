package org.example.fishtank.service;

import jakarta.transaction.Transactional;
import org.example.fishtank.model.dto.fishDto.CreateFish;
import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.fishDto.UpdateFish;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.model.entity.Fish;
import org.example.fishtank.model.entity.Sex;
import org.example.fishtank.model.entity.WaterType;
import org.example.fishtank.model.mapper.FishMapper;
import org.example.fishtank.repository.FishRepository;
import org.example.fishtank.repository.SexRepository;
import org.example.fishtank.repository.UserRepository;
import org.example.fishtank.repository.WaterTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static org.example.fishtank.model.mapper.FishMapper.map;


@Service
@Transactional
public class FishService {


    FishRepository fishRepository;
    WaterTypeRepository waterTypeRepository;
    SexRepository sexRepository;
    UserRepository userRepository;

    public FishService(FishRepository fishRepository, WaterTypeRepository waterTypeRepository, SexRepository sexRepository, UserRepository userRepository) {
        this.fishRepository = fishRepository;
        this.waterTypeRepository = waterTypeRepository;
        this.sexRepository = sexRepository;
        this.userRepository = userRepository;
    }

    public ResponseFish findById(Integer id) {
        return fishRepository.findById(id)
                .map(FishMapper::map)
                .orElseThrow(() -> new RuntimeException("Fish not found"));
    }

    public List<ResponseFish> getAllFish(){
        return fishRepository.findAll()
                .stream()
                .map(FishMapper::map)
                .filter(Objects::nonNull)
                .toList();
    }

    public void save(CreateFish createFish) {
        AppUser appUser = userRepository.findByName(createFish.appUser());
        if (appUser == null) {
            throw new NullPointerException("User can´t be null");
        }

//        String appUser = SecurityContextHolder.getContext().getAuthentication().getName();

        WaterType waterType = waterTypeRepository.findByName(createFish.waterType());
        Sex sex = sexRepository.findByName(createFish.sex());

        Fish fish = FishMapper.map(createFish, waterType, sex, appUser);
        fishRepository.save(fish);
    }

    public void update(int id, UpdateFish fish) {
        Fish oldFish = fishRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Fish not found"));
        FishMapper.map(fish, oldFish);
        fishRepository.update(oldFish.getName(), oldFish.getDescription(), oldFish.getId());
    }

    public void delete(int id) {
        var fish = fishRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Fish not found"));
        fishRepository.delete(fish);
    }

}
