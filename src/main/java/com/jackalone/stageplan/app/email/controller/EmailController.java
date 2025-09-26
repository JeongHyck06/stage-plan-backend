package com.jackalone.stageplan.app.email.controller;

import com.jackalone.stageplan.app.email.dto.EmailResponse;
import com.jackalone.stageplan.app.email.dto.EmailSendRequest;
import com.jackalone.stageplan.app.email.dto.EmailVerificationRequest;
import com.jackalone.stageplan.app.email.service.EmailService;
import com.jackalone.stageplan.app.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "이메일 인증", description = "이메일 인증 관련 API")
public class EmailController {

    private final EmailService emailService;
    private final UserRepository userRepository;

    @PostMapping("/send-verification")
    @Operation(summary = "이메일 인증 코드 발송", description = "사용자 이메일로 인증 코드를 발송합니다.")
    public ResponseEntity<EmailResponse> sendVerificationEmail(@Valid @RequestBody EmailSendRequest request) {
        try {
            // 사용자가 존재하는지 확인
            if (!userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(EmailResponse.failure("해당 이메일로 등록된 사용자가 없습니다."));
            }

            // 이미 인증된 이메일인지 확인
            if (emailService.isEmailVerified(request.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(EmailResponse.failure("이미 인증된 이메일입니다."));
            }

            emailService.sendVerificationEmail(request.getEmail());

            return ResponseEntity.ok(EmailResponse.success(
                    "인증 코드가 발송되었습니다. 이메일을 확인해주세요.",
                    request.getEmail()
            ));

        } catch (Exception e) {
            log.error("이메일 발송 중 오류 발생: {}", request.getEmail(), e);
            return ResponseEntity.internalServerError()
                    .body(EmailResponse.failure("이메일 발송 중 오류가 발생했습니다."));
        }
    }

    @PostMapping("/verify")
    @Operation(summary = "이메일 인증 코드 확인", description = "사용자가 입력한 인증 코드를 확인합니다.")
    public ResponseEntity<EmailResponse> verifyEmail(@Valid @RequestBody EmailVerificationRequest request) {
        try {
            boolean isVerified = emailService.verifyEmail(request.getEmail(), request.getVerificationCode());

            if (isVerified) {
                return ResponseEntity.ok(EmailResponse.success(
                        "이메일 인증이 완료되었습니다.",
                        request.getEmail()
                ));
            } else {
                return ResponseEntity.badRequest()
                        .body(EmailResponse.failure("인증 코드가 올바르지 않거나 만료되었습니다."));
            }

        } catch (Exception e) {
            log.error("이메일 인증 중 오류 발생: {}", request.getEmail(), e);
            return ResponseEntity.internalServerError()
                    .body(EmailResponse.failure("이메일 인증 중 오류가 발생했습니다."));
        }
    }

    @GetMapping("/verification-status/{email}")
    @Operation(summary = "이메일 인증 상태 확인", description = "특정 이메일의 인증 상태를 확인합니다.")
    public ResponseEntity<EmailResponse> getVerificationStatus(@PathVariable String email) {
        try {
            boolean isVerified = emailService.isEmailVerified(email);

            if (isVerified) {
                return ResponseEntity.ok(EmailResponse.success(
                        "이메일이 인증되었습니다.",
                        email
                ));
            } else {
                return ResponseEntity.ok(EmailResponse.failure(
                        "이메일이 인증되지 않았습니다."
                ));
            }

        } catch (Exception e) {
            log.error("이메일 인증 상태 확인 중 오류 발생: {}", email, e);
            return ResponseEntity.internalServerError()
                    .body(EmailResponse.failure("인증 상태 확인 중 오류가 발생했습니다."));
        }
    }
}