package com.qcm.hotel.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserBean create(@RequestBody UserBean user){
        return userService.create(user);
    }

    @GetMapping(path = {"/{id}"})
    public UserBean findOne(@PathVariable("id") int id){
        return userService.findOne(id);
    }

    @PutMapping(path = {"/{id}"})
    public UserBean update(@PathVariable("id") int id, @RequestBody UserBean user){
        user.setId(id);
        return userService.update(user);
    }

    @DeleteMapping(path ={"/{id}"})
    public UserBean delete(@PathVariable("id") int id) {
        return userService.delete(id);
    }

    @GetMapping
    public List<UserBean> findAll(){
        return userService.findAll();
    }
    @PostMapping("/dashboard")
    public UserBean login(@RequestBody UserBean user){
        return userService.create(user);
    }
}
