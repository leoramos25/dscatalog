package com.leords.dscatalog.resources;

import com.leords.dscatalog.dto.CreateUserDTO;
import com.leords.dscatalog.dto.UpdateUserDTO;
import com.leords.dscatalog.dto.UserDTO;
import com.leords.dscatalog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/users")
public class UserResource {
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAllUsers(Pageable pageable) {
        var users = userService.findAllUsers(pageable);
        return ResponseEntity.ok().body(users);
    }
    
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable(value = "id") Long id) {
        var userDto = userService.findUserById(id);
        return ResponseEntity.ok().body(userDto);
    }
    
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO dto) {
        var createUserDto = userService.createUser(dto);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createUserDto);
    }
    
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "id") Long id, @Valid @RequestBody UpdateUserDTO dto) {
        var userDto = userService.updateUser(id, dto);
        return ResponseEntity.ok().body(userDto);
    }
    
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
