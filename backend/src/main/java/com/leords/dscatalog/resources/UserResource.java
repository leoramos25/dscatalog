package com.leords.dscatalog.resources;

import com.leords.dscatalog.dto.CreateUserDTO;
import com.leords.dscatalog.dto.UserDTO;
import com.leords.dscatalog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/users")
public class UserResource {
    @Autowired
    private UserService service;
    
    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAllUsers(Pageable pageable) {
        var users = service.findAllUsers(pageable);
        return ResponseEntity.ok().body(users);
    }
    
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable(value = "id") Long id) {
        var userDto = service.findUserById(id);
        return ResponseEntity.ok().body(userDto);
    }
    
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserDTO dto) {
        var createUserDto = service.createUser(dto);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createUserDto);
    }
    
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "id") Long id, @RequestBody CreateUserDTO dto) {
        var userDto = service.updateUser(id, dto);
        return ResponseEntity.ok().body(userDto);
    }
    
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
