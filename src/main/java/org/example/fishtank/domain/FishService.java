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
        return fishRepository.findById(id).orElse(null);
    }

    public List<Fish> getAllFish(){
        return fishRepository.findAll();
    }

}
