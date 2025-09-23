package com.jackalone.stageplan.app.user.controller;

import com.jackalone.stageplan.app.user.dto.UserDto;
import com.jackalone.stageplan.app.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "사용자", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "프로필 조회", description = "현재 로그인한 사용자의 프로필을 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserDto.Response> getProfile(Authentication authentication) {
        String email = authentication.getName();
        UserDto.Response response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }
}

