package com.leords.dscatalog.services;

import com.leords.dscatalog.dto.CreateUserDTO;
import com.leords.dscatalog.dto.RoleDTO;
import com.leords.dscatalog.dto.UpdateUserDTO;
import com.leords.dscatalog.dto.UserDTO;
import com.leords.dscatalog.entities.User;
import com.leords.dscatalog.repositories.RoleRepository;
import com.leords.dscatalog.repositories.UserRepository;
import com.leords.dscatalog.services.exceptions.DatabaseException;
import com.leords.dscatalog.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class UserService implements UserDetailsService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Transactional(readOnly = true)
    public Page<UserDTO> findAllUsers(Pageable pageable) {
        var users = userRepository.findAll(pageable);
        return users.map(UserDTO::new);
    }
    
    @Transactional(readOnly = true)
    public UserDTO findUserById(Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new UserDTO(user);
    }
    
    @Transactional
    public UserDTO createUser(CreateUserDTO dto) {
        var entity = new User();
        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = userRepository.save(entity);
        return new UserDTO(entity);
    }
    
    @Transactional
    public UserDTO updateUser(Long id, UpdateUserDTO dto) {
        try {
            var entity = userRepository.getOne(id);
            copyDtoToEntity(dto, entity);
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
            entity = userRepository.save(entity);
            return new UserDTO(entity);
        } catch (EntityNotFoundException error) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }
    
    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException error) {
            throw new ResourceNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException error) {
            throw new DatabaseException("Integrity violation");
        }
    }
    
    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        
        entity.getRoles().clear();
        for (RoleDTO roleDto : dto.getRoles()) {
            var role = roleRepository.getOne(roleDto.getId());
            entity.getRoles().add(role);
        }
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email);
        
        if (user == null) {
            logger.error("User not found " + email);
            throw new UsernameNotFoundException("Email not found");
        }
        
        logger.info("User found " + email);
        return user;
    }
}
