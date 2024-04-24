package com.shortlink.webapp.service.interfaces.user;

import com.shortlink.webapp.domain.entity.user.User;
import com.shortlink.webapp.domain.enums.Role;
import com.shortlink.webapp.dto.user.response.AllUsersReadDto;
import com.shortlink.webapp.dto.user.response.ChangePasswordDto;
import com.shortlink.webapp.dto.user.response.UserReadDto;
import com.shortlink.webapp.dto.user.response.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.Map;

public interface UserService {

    UserReadDto findUserById(Long id);

    UserReadDto updateUser(Long id, UserUpdateDto userUpdateDto);

    UserReadDto changeUser(Long id, Map<String, Object> fields);

    void deleteUser(Long id);

    Page<AllUsersReadDto> findAllUsersByPageableAndFilter(Pageable pageable,
                                                          String username,
                                                          String email,
                                                          Role role);

    Page<Revision<Long, User>> getUserAuditing(Long id, Pageable pageable);

    Revision<Long, User> getLastUserChange(Long id);

    void changePassword(Long id, ChangePasswordDto passwordDto);
}
