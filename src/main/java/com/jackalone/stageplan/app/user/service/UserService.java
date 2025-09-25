package com.jackalone.stageplan.app.user.service;

import com.jackalone.stageplan.app.user.domain.User;
import com.jackalone.stageplan.app.user.dto.UserDto;
import com.jackalone.stageplan.app.user.repository.UserRepository;
import com.jackalone.stageplan.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserDto.Response signUp(UserDto.SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(User.Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        return convertToResponse(savedUser);
    }

    public UserDto.TokenResponse signIn(UserDto.SignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.generateToken(user.getEmail());

        UserDto.Response userResponse = convertToResponse(user);

        return UserDto.TokenResponse.builder()
                .accessToken(accessToken)
                .expiresIn(jwtTokenProvider.getExpiration())
                .user(userResponse)
                .build();
    }

    @Transactional(readOnly = true)
    public UserDto.Response getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return convertToResponse(user);
    }

    public UserDto.Response updateProfile(String email, UserDto.UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (request.getNickname() != null) user.setNickname(request.getNickname());
        if (request.getInstagramId() != null) user.setInstagramId(request.getInstagramId());
        if (request.getBandName() != null) user.setBandName(request.getBandName());
        if (request.getProfileImageUrl() != null) user.setProfileImageUrl(request.getProfileImageUrl());
        if (request.getRepresentativeVideoUrl() != null) user.setRepresentativeVideoUrl(request.getRepresentativeVideoUrl());
        if (request.getFavoriteGenres() != null) user.setFavoriteGenres(request.getFavoriteGenres());
        if (request.getBio() != null) user.setBio(request.getBio());

        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    @Transactional(readOnly = true)
    public UserDto.Response getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return convertToResponse(user);
    }

    public UserDto.TokenResponse refreshToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        String newAccessToken = jwtTokenProvider.generateToken(user.getEmail());
        UserDto.Response userResponse = convertToResponse(user);

        return UserDto.TokenResponse.builder()
                .accessToken(newAccessToken)
                .expiresIn(jwtTokenProvider.getExpiration())
                .user(userResponse)
                .build();
    }

    private UserDto.Response convertToResponse(User user) {
        return UserDto.Response.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .instagramId(user.getInstagramId())
                .bandName(user.getBandName())
                .profileImageUrl(user.getProfileImageUrl())
                .representativeVideoUrl(user.getRepresentativeVideoUrl())
                .favoriteGenres(user.getFavoriteGenres())
                .bio(user.getBio())
                .build();
    }
}
