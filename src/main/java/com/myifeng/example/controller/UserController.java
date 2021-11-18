package com.myifeng.example.controller;

import com.myifeng.example.entity.User;
import com.myifeng.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserMapper mapper;

    @GetMapping
    public List<User> list(){
        return mapper.findAll();
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable String id){
        return mapper.findOne(id);
    }

    @PostMapping
    public int save(@RequestBody User user){
        return mapper.save(user);
    }

    @PutMapping("/{id}")
    public int update(@PathVariable String id, @RequestBody User user){
        return mapper.update(user);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable String id){
        return mapper.delete(id);
    }
}
