package org.example.fishtank.service;

import jakarta.transaction.Transactional;
import org.example.fishtank.model.entity.Fish;
import org.example.fishtank.repository.FishRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
public class FishService {


    FishRepository fishRepository;

    public FishService(FishRepository fishRepository) {
        this.fishRepository = fishRepository;
    }

    public Fish findById(Integer id) {
        return fishRepository.findById(id).orElseThrow(() -> new RuntimeException("Fish not found"));
    }

    public List<Fish> getAllFish(){
        return fishRepository.findAll();
    }

    public void save(Fish fish) {
        fishRepository.save(fish);
    }

    public void update(int id, Fish newFish) {
        Fish oldFish = fishRepository.findById(id).orElseThrow(() -> new RuntimeException("Fish not found"));
        oldFish.setName(newFish.getName());
        oldFish.setSpecies(newFish.getSpecies());
        oldFish.setAppUser(newFish.getAppUser());
        oldFish.setDescription(newFish.getDescription());
        oldFish.setWaterType(newFish.getWaterType());
        oldFish.setSex(newFish.getSex());
        fishRepository.save(oldFish);
    }

    public void delete(Fish fish) {
        fishRepository.delete(fish);
    }

}
