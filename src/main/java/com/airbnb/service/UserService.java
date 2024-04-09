package com.airbnb.service;


import com.airbnb.dto.PropertyUserDto;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.PropertyUserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private PropertyUserRepository userRepository;

    public UserService(PropertyUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PropertyUser addUser(PropertyUserDto propertyUserDto){
        PropertyUser user = new PropertyUser();
        user.setFirstName(propertyUserDto.getFirstName());
        user.setLastName(propertyUserDto.getLastName());
        user.setUsername(propertyUserDto.getUsername());
        user.setEmail(propertyUserDto.getEmail());
//        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setPassword(BCrypt.hashpw(propertyUserDto.getPassword(), BCrypt.gensalt(10)));
        user.setUserRole(propertyUserDto.getUserRole());
        PropertyUser savedUser = userRepository.save(user);
        return savedUser;
    }
}
