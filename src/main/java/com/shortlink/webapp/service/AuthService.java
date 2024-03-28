package com.shortlink.webapp.service;

import com.shortlink.webapp.dto.request.UserCreateDto;
import com.shortlink.webapp.dto.request.UserLoginDto;
import com.shortlink.webapp.dto.response.JwtResponseDto;
import com.shortlink.webapp.entity.Token;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.entity.enums.MailType;
import com.shortlink.webapp.entity.enums.TokenType;
import com.shortlink.webapp.exception.InvalidJwtException;
import com.shortlink.webapp.mapper.UserCreateDtoMapper;
import com.shortlink.webapp.repository.TokenRepository;
import com.shortlink.webapp.repository.UserRepository;
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
public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserCreateDtoMapper userCreateDtoMapper;
    private final AuthenticationManager authenticationManager;
    private final MailSender mailSender;
    private final MailVerificationService mailVerificationService;

    @Transactional
    public JwtResponseDto createUser(UserCreateDto userCreateDto) {
        User user = userCreateDtoMapper.toEntity(userCreateDto);
        User savedUser = userRepository.save(user);
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, accessToken);

        String token = mailVerificationService.createMailVerificationToken(savedUser);
        mailSender.sendEmail(MailType.REGISTRATION, savedUser,
                EmailUtil.VERIFICATION_URL + token);

        return new JwtResponseDto(accessToken, refreshToken);

//        var user = User.builder()
//                .firstname(request.getFirstname())
//                .lastname(request.getLastname())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(request.getRole())
//                .build();
//        var savedUser = repository.save(user);
//        var jwtToken = jwtService.generateToken(user);
//        var refreshToken = jwtService.generateRefreshToken(user);
//        saveUserToken(savedUser, jwtToken);
//        return AuthenticationResponse.builder()
//                .accessToken(jwtToken)
//                .refreshToken(refreshToken)
//                .build();
    }


    @Transactional
    public JwtResponseDto login(UserLoginDto userLoginDto) {
        authenticationManager.authenticate(
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
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//        var user = repository.findByEmail(request.getEmail())
//                .orElseThrow();
//        var jwtToken = jwtService.generateToken(user);
//        var refreshToken = jwtService.generateRefreshToken(user);
//        revokeAllUserTokens(user);
//        saveUserToken(user, jwtToken);
//        return AuthenticationResponse.builder()
//                .accessToken(jwtToken)
//                .refreshToken(refreshToken)
//                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    //    private void saveUserToken(User user, String jwtToken) {
//        var token = Token.builder()
//                .user(user)
//                .token(jwtToken)
//                .tokenType(TokenType.BEARER)
//                .expired(false)
//                .revoked(false)
//                .build();
//        tokenRepository.save(token);
//    }
//
//    private void revokeAllUserTokens(User user) {
//        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
//        if (validUserTokens.isEmpty())
//            return;
//        validUserTokens.forEach(token -> {
//            token.setExpired(true);
//            token.setRevoked(true);
//        });
//        tokenRepository.saveAll(validUserTokens);
//    }
//    @Transactional
//    private void revokeAllUserTokens(User user) {
//        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUserId(user.getId());
//        if (validUserTokens.isEmpty())
//            return;
//        validUserTokens.forEach(token -> {
//            token.setExpired(true);
//            token.setRevoked(true);
//        });
//        tokenRepository.saveAll(validUserTokens);
//    }
    private void revokeAllUserTokens(User user) {
        tokenRepository.findAllValidTokenByUserId(user.getId())
                .forEach(token -> {
                    token.setExpired(true);
                    token.setRevoked(true);
                });
    }

    @Transactional
    public JwtResponseDto refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);
        String refreshToken;
        String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidJwtException("jwt is invalid");
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
        throw new InvalidJwtException("jwt is invalid");
    }

}


