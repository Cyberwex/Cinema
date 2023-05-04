package com.aist.cinema.controller;

import com.aist.cinema.entity.User;
import com.aist.cinema.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<EntityModel<User>> userModels = users.stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).updateUser(user.getId(), user)).withRel("update"),
                        linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel("delete")))
                .collect(Collectors.toList());
        return CollectionModel.of(userModels,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel(),
                linkTo(methodOn(UserController.class).createUser(null)).withRel("create"));
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).updateUser(id, user)).withRel("update"),
                linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"));
    }

    @PostMapping("/users")
    public ResponseEntity<EntityModel<User>> createUser(@RequestBody User user) {
        User createdUser = userService.saveUser(user);
        EntityModel<User> userEntityModel = EntityModel.of(createdUser,
                linkTo(methodOn(UserController.class).getUserById(createdUser.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).updateUser(createdUser.getId(), createdUser)).withRel("update"),
                linkTo(methodOn(UserController.class).deleteUser(createdUser.getId())).withRel("delete"));
        return ResponseEntity.created(linkTo(methodOn(UserController.class).getUserById(createdUser.getId())).toUri()).body(userEntityModel);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<EntityModel<User>> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        EntityModel<User> userEntityModel = EntityModel.of(updatedUser,
                linkTo(methodOn(UserController.class).getUserById(updatedUser.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).updateUser(updatedUser.getId(), updatedUser)).withRel("update"),
                linkTo(methodOn(UserController.class).deleteUser(updatedUser.getId())).withRel("delete"));
        return ResponseEntity.ok(userEntityModel);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
