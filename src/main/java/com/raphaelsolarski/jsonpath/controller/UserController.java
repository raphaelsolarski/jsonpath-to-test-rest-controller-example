package com.raphaelsolarski.jsonpath.controller;


import com.google.common.collect.Lists;
import com.raphaelsolarski.jsonpath.model.Post;
import com.raphaelsolarski.jsonpath.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        User user = new User(id);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUsers() {
        List<Post> posts = Lists.newArrayList(new Post("post1"), new Post("post2"));

        User user1 = new User(1);
        user1.setPosts(posts);
        User user2 = new User(2);
        user2.setPosts(posts);
        User user3 = new User(3);
        user3.setPosts(posts);

        List<User> users = Lists.newArrayList(user1, user2, user3);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
