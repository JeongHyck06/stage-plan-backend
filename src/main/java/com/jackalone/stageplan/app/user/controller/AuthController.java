package com.jackalone.stageplan.app.user.controller;

import com.jackalone.stageplan.app.user.dto.UserDto;
import com.jackalone.stageplan.app.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "사용자 인증 관련 API")
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    public ResponseEntity<UserDto.Response> signUp(@Valid @RequestBody UserDto.SignUpRequest request) {
        UserDto.Response response = userService.signUp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    public ResponseEntity<UserDto.TokenResponse> signIn(@Valid @RequestBody UserDto.SignInRequest request) {
        UserDto.TokenResponse response = userService.signIn(request);
        return ResponseEntity.ok(response);
    }
}

