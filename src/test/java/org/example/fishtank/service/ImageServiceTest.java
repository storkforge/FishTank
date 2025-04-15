package org.example.fishtank.service;

import org.example.fishtank.repository.FishRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @InjectMocks
    ImageService imageService;

    @Mock
    FishRepository fishRepository;

    @Test
    void saveImage() {
    }

    @Test
    void getImagePath() {
    }
}