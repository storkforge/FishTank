package org.example.fishtank.service;

import jakarta.transaction.Transactional;
import org.example.fishtank.model.dto.fishDto.CreateFish;
import org.example.fishtank.model.dto.fishDto.ResponseFish;
import org.example.fishtank.model.dto.fishDto.UpdateFish;
import org.example.fishtank.model.entity.Fish;
import org.example.fishtank.model.mapper.FishMapper;
import org.example.fishtank.repository.FishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static org.example.fishtank.model.mapper.FishMapper.map;


@Service
@Transactional
public class FishService {


    FishRepository fishRepository;

    public FishService(FishRepository fishRepository) {
        this.fishRepository = fishRepository;
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

    public void save(CreateFish fish) {
        var newFish = map(fish);
        fishRepository.save(newFish);

    }

    public void update(int id, UpdateFish fish) {
        Fish oldFish = fishRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Fish not found"));
        FishMapper.map(fish, oldFish);
        fishRepository.update(oldFish);
    }

    public void delete(int id) {
        var fish = fishRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Fish not found"));
        fishRepository.delete(fish);
    }

}
