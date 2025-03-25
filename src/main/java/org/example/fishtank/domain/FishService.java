package org.example.fishtank.domain;

import jakarta.transaction.Transactional;
import org.example.fishtank.domain.entity.Fish;
import org.example.fishtank.infrastructure.persistence.FishRepository;
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
        oldFish.setUserid(newFish.getUserid());
        oldFish.setDescription(newFish.getDescription());
        oldFish.setWatertypeid(newFish.getWatertypeid());
        oldFish.setSexid(newFish.getSexid());
        fishRepository.save(oldFish);
    }

    public void delete(Fish fish) {
        fishRepository.delete(fish);
    }

}
