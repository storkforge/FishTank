package org.example.fishtank.model.entity;

import jakarta.persistence.*;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.beans.FeatureDescriptor;

import java.awt.*;
import java.io.Serializable;

@Entity
@Table(name = "post")
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "posttext", nullable = false, length = 100)
    private String posttext;

    @Column(name = "cityname")
    private String cityName;

    @Column
    private Point<G2D> coordinate;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "post_fish_id_fk", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Fish fishid;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Point<G2D> getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Point<G2D> coordinate) {
        this.coordinate = coordinate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return posttext;
    }

    public void setText(String text) {
        this.posttext = text;
    }

    public Fish getFishid() {
        return fishid;
    }

    public void setFishid(Fish fishid) {
        this.fishid = fishid;
    }

}
