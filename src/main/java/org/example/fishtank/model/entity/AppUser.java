package org.example.fishtank.model.entity;

import jakarta.persistence.*;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appuser")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "password_hash", nullable = false, length = 60)
    private String passwordHash;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "appuser_access_id_fk" , nullable = false)
    private Access access;

    @OneToMany(mappedBy = "appUser", fetch = FetchType.LAZY)
    private List<Fish> fishes = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public List<Fish> getFishes() {
        return fishes;
    }

    public void setFishes(List<Fish> fishes) {
        this.fishes = fishes;
    }
}
