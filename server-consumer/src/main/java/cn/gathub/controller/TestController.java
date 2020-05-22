package cn.gathub.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import cn.gathub.domain.User;
import cn.gathub.service.UserService;

@RestController
public class TestController {

  private final UserService userService;

  public TestController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("user/{id}")
  public User getUser(@PathVariable Long id) {
    return userService.get(id);
  }

  @GetMapping("user")
  public List<User> getUsers() {
    return userService.get();
  }

  @PostMapping("user")
  public void addUser() {
    User user = new User(1L, "HongHui", "123456");
    userService.add(user);
  }

  @PutMapping("user")
  public void updateUser() {
    User user = new User(1L, "Tom", "123456");
    userService.update(user);
  }

  @DeleteMapping("user/{id}")
  public void deleteUser(@PathVariable Long id) {
    userService.delete(id);
  }
}
