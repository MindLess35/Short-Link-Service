package com.shortlink.webapp.service;

import com.querydsl.core.types.Predicate;
import com.shortlink.webapp.dto.request.ChangePasswordDto;
import com.shortlink.webapp.dto.request.UserUpdateDto;
import com.shortlink.webapp.dto.response.AllUsersReadDto;
import com.shortlink.webapp.dto.response.UserReadDto;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.entity.enums.Role;
import com.shortlink.webapp.exception.InvalidPasswordException;
import com.shortlink.webapp.exception.UserNotExistsException;
import com.shortlink.webapp.mapper.UserReadDtoMapper;
import com.shortlink.webapp.mapper.UserUpdateDtoMapper;
import com.shortlink.webapp.repository.UserRepository;
import com.shortlink.webapp.util.QPredicates;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    private final UserUpdateDtoMapper userUpdateDtoMapper;
    private final UserReadDtoMapper userReadDtoMapper;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = "user", key = "#id")
    public UserReadDto findUserById(Long id) {
        return userRepository.findById(id)
                .map(userReadDtoMapper::toDto)
                .orElseThrow(() -> new UserNotExistsException(
                        "User with id %s does not exists".formatted(id)));
    }

    @Transactional
    @CachePut(value = "user", key = "#id")
    public UserReadDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        return userRepository.findById(id)
                .map(user -> userUpdateDtoMapper.updateEntity(userUpdateDto, user))
                .map(userRepository::save)
                .map(userReadDtoMapper::toDto)
                .orElseThrow(() -> new UserNotExistsException(
                        "User with id %s does not exists".formatted(id)));
    }

    @Transactional
    @CachePut(value = "user", key = "#id")
    public UserReadDto changeUser(Long id, Map<String, Object> fields) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(User.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, user.get(), value);
                }
            });

            return user
                    .map(userRepository::save)
                    .map(userReadDtoMapper::toDto)
                    .orElseThrow(() -> new UserNotExistsException(
                            "User with id %s does not exists".formatted(id)));
        } else
            throw new UserNotExistsException("User with id %s does not exists".formatted(id));
    }

    @Transactional
    @CacheEvict(value = "user", key = "#id")
    public void deleteUser(Long id) {
        userRepository.delete(userRepository.findById(id)
                .orElseThrow(() -> new UserNotExistsException(
                        "User with id %s does not exists".formatted(id))));
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

    @Transactional
    public void changePassword(Long id, ChangePasswordDto passwordDto) {
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmationPassword()))
            throw new InvalidPasswordException("the new password and its confirmation do not match");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotExistsException(
                        "User with id %s does not exists".formatted(id)));

        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), user.getPassword()))
            throw new InvalidPasswordException("wrong password");

        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);

    }

}


