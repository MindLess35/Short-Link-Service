package com.shortlink.webapp.service.impl.auth;

import com.shortlink.webapp.domain.entity.Token;
import com.shortlink.webapp.domain.entity.user.User;
import com.shortlink.webapp.domain.enums.MailType;
import com.shortlink.webapp.domain.enums.TokenType;
import com.shortlink.webapp.domain.exception.security.InvalidJwtException;
import com.shortlink.webapp.dto.security.JwtResponseDto;
import com.shortlink.webapp.dto.user.response.UserCreateDto;
import com.shortlink.webapp.dto.user.response.UserLoginDto;
import com.shortlink.webapp.mapper.user.UserCreateDtoMapper;
import com.shortlink.webapp.repository.jpa.user.TokenRepository;
import com.shortlink.webapp.repository.jpa.user.UserRepository;
import com.shortlink.webapp.service.interfaces.auth.AuthService;
import com.shortlink.webapp.service.interfaces.auth.JwtService;
import com.shortlink.webapp.service.interfaces.mail.MailSender;
import com.shortlink.webapp.service.interfaces.mail.MailVerificationService;
import com.shortlink.webapp.util.EmailUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserCreateDtoMapper userCreateDtoMapper;
    private final AuthenticationManager authenticationManager;
    private final MailSender mailSender;
    private final MailVerificationService mailVerificationService;

    @Override
    @Transactional
    public JwtResponseDto createUser(UserCreateDto userCreateDto) {
        // todo use jwt in header and return user as body
        User user = userCreateDtoMapper.toEntity(userCreateDto);
        User savedUser = userRepository.save(user);
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, accessToken);

        String token = mailVerificationService.createMailVerificationToken(savedUser);
        mailSender.sendEmail(MailType.REGISTRATION, savedUser,
                EmailUtil.VERIFICATION_URL + token);

        return new JwtResponseDto(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public JwtResponseDto login(UserLoginDto userLoginDto) {
        authenticationManager.authenticate( //todo use auth which this method return
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.getUsername(),
                        userLoginDto.getPassword()
                )
        );
        User user = userRepository.findByUsername(userLoginDto.getUsername())
                .orElseThrow();
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        return new JwtResponseDto(accessToken, refreshToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        tokenRepository.findAllValidTokenByUserId(user.getId())
                .forEach(token -> {
                    token.setExpired(true);
                    token.setRevoked(true);
                });
    }

    @Override
    @Transactional
    public JwtResponseDto refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);
        String refreshToken;
        String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidJwtException("JWT is invalid");
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);

                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                return new JwtResponseDto(accessToken, refreshToken);
            }
        }
        throw new InvalidJwtException("JWT is invalid");
    }

}

