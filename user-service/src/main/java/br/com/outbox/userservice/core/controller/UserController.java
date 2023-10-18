package br.com.outbox.userservice.core.controller;

import br.com.outbox.userservice.core.dto.UserDTO;
import br.com.outbox.userservice.core.mapper.UserMapper;
import br.com.outbox.userservice.core.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/user")
public class UserController {

    private UserService userService;

    @PostMapping
    public UserDTO save(@RequestBody UserDTO userDTO) {
        var user = userService.create(userDTO);
        return UserMapper.toUserDto(user);
    }

    @PutMapping("{id}")
    public UserDTO update(@PathVariable Integer id,
                          @RequestBody UserDTO userDTO) {
        var user = userService.update(userDTO, id);
        return UserMapper.toUserDto(user);
    }

    @DeleteMapping("{id}")
    public UserDTO delete(@PathVariable Integer id) {
        var user = userService.delete(id);
        return UserMapper.toUserDto(user);
    }

    @GetMapping("{id}")
    public UserDTO findById(Integer id) {
        return UserMapper.toUserDto(userService.findById(id));
    }

    @GetMapping
    public List<UserDTO> findAll() {
        return userService
            .findAll()
            .stream()
            .map(UserMapper::toUserDto)
            .toList();
    }
}
