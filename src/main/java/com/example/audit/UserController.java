package com.example.audit;

import com.example.audit.user.Actor;
import com.example.audit.user.User;
import com.example.audit.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/users")
    public ResponseEntity create(
            @RequestBody User user,
            Actor actor) {
        service.create(user, actor);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity update(
            @PathVariable("id") Long id,
            @RequestBody User user,
            Actor actor) {
        service.update(id, user, actor);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id,
                                 Actor actor) {
        service.delete(id, actor);
        return new ResponseEntity(HttpStatus.OK);
    }

}
