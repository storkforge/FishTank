package org.example.fishtank.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "eventjoining", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"event_id_fk", "appuser_id_fk"})
})
public class EventJoining {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id_fk", nullable = false)
    private Event eventId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "appuser_id_fk", nullable = false)
    private AppUser appUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Event getEventId() {
        return eventId;
    }

    public void setEventId(Event eventId) {
        this.eventId = eventId;
    }

    public AppUser getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(AppUser appUserId) {
        this.appUserId = appUserId;
    }
}
