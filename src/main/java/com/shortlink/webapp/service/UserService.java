package com.shortlink.webapp.service;

import com.shortlink.webapp.dto.request.ChangePasswordDto;
import com.shortlink.webapp.dto.request.UserCreateEditDto;
import com.shortlink.webapp.dto.response.AllUsersReadDto;
import com.shortlink.webapp.dto.response.UserReadDto;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.exception.InvalidPasswordException;
import com.shortlink.webapp.exception.UserNotExistsException;
import com.shortlink.webapp.mapper.AllUsersReadMapper;
import com.shortlink.webapp.mapper.UserCreateEditDtoMapper;
import com.shortlink.webapp.mapper.UserReadDtoMapper;
import com.shortlink.webapp.repository.LinkRepository;
import com.shortlink.webapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final LinkRepository linkRepository;
    private final UserCreateEditDtoMapper userCreateEditDtoMapper;
    private final AllUsersReadMapper allUsersReadMapper;
    private final UserReadDtoMapper userReadDtoMapper;
    private final PasswordEncoder passwordEncoder;

    public UserReadDto findUserById(Long id) {
        return userRepository.findById(id)
                .map(userReadDtoMapper)
                .orElseThrow(() -> new UserNotExistsException(
                        "user with id [%s] does not exists".formatted(id)));
    }

//    @Transactional
//    public UserReadDto createUser(UserCreateEditDto userCreateEditDto) {
//        return Optional.of(userCreateEditDto)
//                .map(userCreateEditDtoMapper)
//                .map(userRepository::save)
//                .map(userReadDtoMapper)
//                .orElseThrow();
//    }
    @Transactional
    public UserReadDto updateUser(Long id, UserCreateEditDto userCreateEditDto) {
        return userRepository.findById(id)
                .map(user -> userCreateEditDtoMapper.updateEntity(userCreateEditDto, user))
                .map(userRepository::saveAndFlush)
                .map(userReadDtoMapper)
                .orElseThrow(() -> new UserNotExistsException(
                        "user with id [%s] does not exists".formatted(id)));
    }

    //    public Product updateProductByFields(int id, Map<String, Object> fields) {
//        Optional<Product> existingProduct = repository.findById(id);
//
//        if (existingProduct.isPresent()) {
//            fields.forEach((key, value) -> {
//                Field field = ReflectionUtils.findField(Product.class, key);
//                field.setAccessible(true);
//                ReflectionUtils.setField(field, existingProduct.get(), value);
//            });
//            return repository.save(existingProduct.get());
//        }
//        return null;
//    }
    @Transactional
    public UserReadDto changeUser(Long id, Map<String, Object> fields) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(User.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, user.get(), value);
            });
            return userReadDtoMapper.apply(userRepository.save(user.get()));
        } else
            throw new UserNotExistsException("user with id [%s] does not exists".formatted(id));
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.delete(userRepository.findById(id)
                .orElseThrow(() -> new UserNotExistsException(
                        "user with id [%s] does not exists".formatted(id))));
    }

    @Transactional
    public void changePassword(ChangePasswordDto passwordDto, User user) {
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmationPassword()))
            throw new InvalidPasswordException("the new password and its confirmation do not match");

        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), user.getPassword()))
            throw new InvalidPasswordException("wrong password");

        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);

    }

    public List<AllUsersReadDto> findAllUsers() {
        return userRepository.findAll()//TODO
                .stream()
                .map(allUsersReadMapper::toDto)
                .toList();
    }


}


