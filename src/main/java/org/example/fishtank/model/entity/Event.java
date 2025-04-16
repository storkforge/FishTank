package org.example.fishtank.model.entity;

import jakarta.persistence.*;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event")
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "eventtext", nullable = false, length = 100)
    private String eventtext;

    @Column(name = "cityname")
    private String cityname;

    @Column(name = "eventdate")
    private LocalDateTime eventdate;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "event_appuser_id_fk", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AppUser appUserId;

    @OneToMany(mappedBy = "eventId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventJoining> eventJoinings = new ArrayList<>();

    public LocalDateTime getEventDate() {
        return eventdate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventdate = eventDate;
    }

    public List<EventJoining> getEventJoinings() {
        return eventJoinings;
    }

    public void setEventJoinings(List<EventJoining> eventJoinings) {
        this.eventJoinings = eventJoinings;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventtext() {
        return eventtext;
    }

    public void setEventtext(String eventtext) {
        this.eventtext = eventtext;
    }

    public String getCityName() {
        return cityname;
    }

    public void setCityName(String cityname) {
        this.cityname = cityname;
    }

    public AppUser getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(AppUser appUserId) {
        this.appUserId = appUserId;
    }
}
