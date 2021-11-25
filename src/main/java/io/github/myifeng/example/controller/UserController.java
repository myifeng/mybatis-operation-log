package io.github.myifeng.example.controller;

import io.github.myifeng.example.entity.User;
import io.github.myifeng.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    final UserMapper mapper;

    public UserController(UserMapper mapper) {
        this.mapper = mapper;
    }

    @GetMapping
    public List<User> list(User user){
        return mapper.findAll(user);
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable String id){
        return mapper.findOne(id);
    }

    @PostMapping
    public User save(@RequestBody User user){
        mapper.save(user);
        return user;
    }

    @PutMapping("/{id}")
    public int update(@PathVariable String id, @RequestBody User user){
        user.setId(id);
        return mapper.update(user);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable String id){
        return mapper.delete(id);
    }
}
