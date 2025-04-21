package org.example.fishtank.model.mapper;

import org.example.fishtank.model.dto.postDto.CreatePost;
import org.example.fishtank.model.dto.postDto.ResponsePost;
import org.example.fishtank.model.dto.postDto.UpdatePost;
import org.example.fishtank.model.entity.Fish;
import org.example.fishtank.model.entity.Post;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.builder.DSL;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostMapperTest {

    @Test
    @DisplayName("Map Post to ResponsePost should return same ")
    void mapPostToResponsePostShouldReturnSame() {
        Fish fish = new Fish();
        fish.setId(1);

        Point<G2D> coordinate = DSL.point(
                CoordinateReferenceSystems.WGS84, DSL.g(12.34, 56.78));

        Post post = new Post();
        post.setId(1);
        post.setText("Test text");
        post.setCityName("Test city");
        post.setCoordinate(coordinate);
        post.setFishid(fish);

        ResponsePost responsePost = PostMapper.map(post);

        assertNotNull(responsePost);
        assertEquals(1, responsePost.id());
        assertEquals("Test text", responsePost.text());
        assertEquals("Test city", responsePost.cityName());
        assertEquals(12.34, responsePost.longitude());
        assertEquals(56.78, responsePost.latitude());
        assertEquals(1, responsePost.fishId());
    }

    @Test
    @DisplayName("Map Post to ResponsePost without coordinates ")
    void mapPostToResponsePostWithoutCoordinatesShouldReturnSame() {
        Fish fish = new Fish();
        fish.setId(1);
        Post post = new Post();
        post.setId(1);
        post.setText("Test text");
        post.setCityName("Test city");
        post.setFishid(fish);

        ResponsePost responsePost = PostMapper.map(post);

        assertNotNull(responsePost);
        assertEquals(1, responsePost.id());
        assertEquals("Test text", responsePost.text());
        assertEquals("Test city", responsePost.cityName());
        assertNull(responsePost.longitude());
        assertNull(responsePost.latitude());
        assertEquals(1, responsePost.fishId());
    }

    @Test
    @DisplayName("Map CreatePost to post should be same")
    void mapCreatePostToPostShouldBeSame() {
        Fish fish = new Fish();
        fish.setId(1);

        CreatePost createPost = new CreatePost("new text", "New city", 1);
        Point<G2D> point = null;
        Post post = PostMapper.map(createPost, fish, null);

        assertNotNull(post);
        assertEquals("new text", post.getText());
        assertEquals("New city", post.getCityName());
        assertEquals(1, post.getFishid().getId());
    }

    @Test
    @DisplayName("Map UpdatePost to Post should update post")
    void mapUpdatePostToPostShouldUpdatePost() {
        Post oldPost = new Post();
        oldPost.setId(1);
        oldPost.setText("Test text");
        oldPost.setCityName("Test city");

        UpdatePost updatePost = new UpdatePost("Updated text", "Updated city");

        PostMapper.map(updatePost, oldPost);

        assertEquals("Updated text", oldPost.getText());
        assertEquals("Updated city", oldPost.getCityName());
    }

    @Test
    @DisplayName("Map UpdatePost to Post should update only text")
    void mapUpdatePostToPostShouldUpdateOnlyText() {
        Post oldPost = new Post();
        oldPost.setId(1);
        oldPost.setText("Test text");
        oldPost.setCityName("Test city");

        UpdatePost updatePost = new UpdatePost("Updated text", null);

        PostMapper.map(updatePost, oldPost);

        assertEquals("Updated text", oldPost.getText());
        assertEquals("Test city", oldPost.getCityName());
    }
    @Test
    @DisplayName("Map UpdatePost to Post should update only city")
    void mapUpdatePostToPostShouldUpdateOnlyCity() {
        Post oldPost = new Post();
        oldPost.setId(1);
        oldPost.setText("Test text");
        oldPost.setCityName("Test city");

        UpdatePost updatePost = new UpdatePost(null, "Updated city");

        PostMapper.map(updatePost, oldPost);

        assertEquals("Test text", oldPost.getText());
        assertEquals("Updated city", oldPost.getCityName());
    }



    @Test
    @DisplayName("Map Null Post to ResponsePost")
    void mapNullPostToResponsePost() {
        ResponsePost responsePost = PostMapper.map(null);
        assertNull(responsePost);
    }

    @Test
    @DisplayName("Map Null CreatePost to Post")
    void mapNullCreatePostToPost() {
        Point<G2D> point = null;
        Post post = PostMapper.map(null, new Fish(), null);
        assertNull(post);
    }
}
