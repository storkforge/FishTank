package org.example.fishtank.repository;

import jakarta.persistence.EntityManager;
import org.example.fishtank.model.entity.Access;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.model.entity.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
class EventRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:15-3.3")
                    .asCompatibleSubstituteFor("postgres")
    );

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    AccessRepository accessRepository;

    @Autowired
    EntityManager entityManager;

    private AppUser savedUser;

    @BeforeEach
    void setUp() {
        Access access = new Access();
        access.setName("Standard");
        accessRepository.save(access);

        AppUser user = new AppUser();
        user.setName("eventUser");
        user.setEmail("eventUser@email.com");
        user.setPasswordHash("password123");
        user.setAuthenticationCode("code123");
        user.setAccess(access);

        savedUser = appUserRepository.save(user);

        Event event = new Event();
        event.setEventTitle("Original Title");
        event.setEventtext("Original Text");
        event.setCityName("Gothenburg");
        event.setEventDate(LocalDateTime.now().plusDays(1));
        event.setAppUserId(savedUser);

        eventRepository.save(event);
    }

    @Test
    void findAll_ShouldReturnOneEvent() {
        var result = eventRepository.findAll();
        assertThat(result).hasSize(1);
    }

    @Test
    void update_ShouldChangeEventText() {
        Event event = eventRepository.findAll().get(0);
        int eventId = event.getId();

        String newText = "Updated Event Description";
        eventRepository.update(newText, eventId);

        entityManager.flush();
        entityManager.clear();

        Event updated = eventRepository.findById(eventId).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getEventtext()).isEqualTo(newText);
    }

    @Test
    void findByAppUserId_ShouldReturnUserEvents() {
        List<Event> events = eventRepository.findByAppUserId(savedUser.getId());
        assertThat(events).hasSize(1);
        assertThat(events.get(0).getAppUserId().getId()).isEqualTo(savedUser.getId());
    }

    @Test
    void findEventByDateBetween_ShouldReturnFutureEvent() {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusDays(2);

        List<Event> results = eventRepository.findEventByDateBetween(from, to);
        assertThat(results).hasSize(1);
    }
}
