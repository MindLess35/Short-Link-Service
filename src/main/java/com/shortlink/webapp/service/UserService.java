package com.shortlink.webapp.service;

import com.querydsl.core.types.Predicate;
import com.shortlink.webapp.dto.request.ChangePasswordDto;
import com.shortlink.webapp.dto.request.UserCreateEditDto;
import com.shortlink.webapp.dto.response.AllUsersReadDto;
import com.shortlink.webapp.dto.response.UserReadDto;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.entity.enums.Role;
import com.shortlink.webapp.exception.InvalidPasswordException;
import com.shortlink.webapp.exception.UserNotExistsException;
import com.shortlink.webapp.mapper.UserCreateEditDtoMapper;
import com.shortlink.webapp.mapper.UserReadDtoMapper;
import com.shortlink.webapp.repository.UserRepository;
import com.shortlink.webapp.util.QPredicates;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import static com.shortlink.webapp.entity.QUser.user;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserCreateEditDtoMapper userCreateEditDtoMapper;
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
                if (field != null) {
                    field.setAccessible(true);
                }
                if (field != null) {
                    ReflectionUtils.setField(field, user.get(), value);
                }
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

    public Page<AllUsersReadDto> findAllUsersByPageableAndFilter(Pageable pageable,
                                                                 String username,
                                                                 String email,
                                                                 Role role) {

        Predicate predicate = QPredicates.builder()
                .add(username, user.username::containsIgnoreCase)
                .add(email, user.email::containsIgnoreCase)
                .add(role, user.role::eq)
                .buildAnd();

        return userRepository.findPageOfUsers(predicate, pageable);
    }


    public Page<Revision<Long, User>> getUserAuditing(Long id, Pageable pageable) {
        return userRepository.findRevisions(id, pageable);
    }

    public Revision<Long, User> getLastUserChange(Long id) {
        return userRepository.findLastChangeRevision(id)
                .orElseThrow(() -> new UserNotExistsException(
                        "User with id %s does not exists".formatted(id)));
    }
}


