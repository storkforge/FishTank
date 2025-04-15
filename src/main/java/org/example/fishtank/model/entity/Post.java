package org.example.fishtank.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.beans.FeatureDescriptor;
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

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "post_fish_id_fk", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Fish fishid;

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
