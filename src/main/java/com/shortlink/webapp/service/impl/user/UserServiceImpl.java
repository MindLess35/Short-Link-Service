package com.shortlink.webapp.service.impl.user;

import com.querydsl.core.types.Predicate;
import com.shortlink.webapp.dto.user.response.ChangePasswordDto;
import com.shortlink.webapp.dto.user.response.UserUpdateDto;
import com.shortlink.webapp.dto.user.response.AllUsersReadDto;
import com.shortlink.webapp.dto.user.response.UserReadDto;
import com.shortlink.webapp.domain.entity.user.User;
import com.shortlink.webapp.domain.enums.Role;
import com.shortlink.webapp.domain.exception.user.password.InvalidPasswordException;
import com.shortlink.webapp.domain.exception.base.ResourceNotFoundException;
import com.shortlink.webapp.domain.exception.user.password.PasswordConfirmationException;
import com.shortlink.webapp.mapper.user.UserReadDtoMapper;
import com.shortlink.webapp.mapper.user.UserUpdateDtoMapper;
import com.shortlink.webapp.repository.jpa.user.UserRepository;
import com.shortlink.webapp.service.interfaces.user.UserService;
import com.shortlink.webapp.util.QPredicates;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
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

import static com.shortlink.webapp.domain.entity.user.QUser.user;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@CacheConfig(cacheResolver = "userCacheResolver")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserUpdateDtoMapper userUpdateDtoMapper;
    private final UserReadDtoMapper userReadDtoMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Cacheable(key = "#id")
    public UserReadDto findUserById(Long id) {
        return userRepository.findById(id)
                .map(userReadDtoMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id %s does not exists".formatted(id)));
    }

    @Override
    @Transactional
    @CachePut(key = "#id")
    public UserReadDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        return userRepository.findById(id)
                .map(user -> userUpdateDtoMapper.updateEntity(userUpdateDto, user))
                .map(userRepository::save)
                .map(userReadDtoMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id %s does not exists".formatted(id)));
    }

    @Override
    @Transactional
    @CachePut(key = "#id")
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
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "User with id %s does not exists".formatted(id)));
        } else
            throw new ResourceNotFoundException("User with id %s does not exists".formatted(id));
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteUser(Long id) {
        userRepository.delete(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id %s does not exists".formatted(id))));
    }

    @Override
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

    @Override
    public Page<Revision<Long, User>> getUserAuditing(Long id, Pageable pageable) {
        return userRepository.findRevisions(id, pageable);
    }

    @Override
    public Revision<Long, User> getLastUserChange(Long id) {
        return userRepository.findLastChangeRevision(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id %s does not exists".formatted(id)));
    }

    @Override
    @Transactional
    public void changePassword(Long id, ChangePasswordDto passwordDto) {
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmationPassword()))
            throw new PasswordConfirmationException("The new password and its confirmation do not match");
//todo add normal message
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id %s does not exists".formatted(id)));

        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), user.getPassword()))
            throw new InvalidPasswordException("wrong password");

        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);

    }

}


