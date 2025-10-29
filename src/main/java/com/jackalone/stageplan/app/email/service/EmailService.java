package com.jackalone.stageplan.app.email.service;

import com.jackalone.stageplan.app.email.domain.EmailVerification;
import com.jackalone.stageplan.app.email.repository.EmailVerificationRepository;
import com.jackalone.stageplan.app.user.domain.User;
import com.jackalone.stageplan.app.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;
    private final EmailTemplateService emailTemplateService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base-url:http://localhost:3000}")
    private String baseUrl;

    @Transactional
    public void sendVerificationEmail(String email) {
        // 최근 발송 이력 확인 (1분 이내 중복 발송 방지)
        Optional<EmailVerification> recentVerification = emailVerificationRepository
                .findByEmailAndCreatedAtAfter(email, LocalDateTime.now().minusMinutes(1));
        
        if (recentVerification.isPresent()) {
            log.warn("이메일 발송 요청이 너무 빈번합니다: {}", email);
            throw new RuntimeException("이메일 발송 요청이 너무 빈번합니다. 1분 후에 다시 시도해주세요.");
        }

        // 기존 인증 코드가 있으면 삭제
        emailVerificationRepository.deleteByEmail(email);

        // 새로운 인증 코드 생성
        String verificationCode = generateVerificationCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(10);

        // 인증 정보 저장
        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .verificationCode(verificationCode)
                .expiresAt(expiresAt)
                .isVerified(false)
                .build();

        emailVerificationRepository.save(emailVerification);

        // 메일 발송 (비동기)
        sendEmailAsync(email, verificationCode);

        log.info("이메일 인증 코드 생성 및 발송 요청 완료: {}", email);
    }

    @Async
    private void sendEmailAsync(String email, String verificationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject(emailTemplateService.createVerificationEmailSubject());
            helper.setText(emailTemplateService.createVerificationEmailTemplate(verificationCode, baseUrl), true);

            mailSender.send(message);

            log.info("이메일 발송 완료: {}", email);

        } catch (MessagingException e) {
            log.error("이메일 발송 실패: {}", email, e);
            throw new RuntimeException("이메일 발송에 실패했습니다.", e);
        }
    }

    @Transactional
    public boolean verifyEmail(String email, String verificationCode) {
        Optional<EmailVerification> verificationOpt = emailVerificationRepository
                .findByEmailAndVerificationCode(email, verificationCode);

        if (verificationOpt.isEmpty()) {
            log.warn("잘못된 인증 코드: email={}, code={}", email, verificationCode);
            return false;
        }

        EmailVerification verification = verificationOpt.get();

        // 만료 시간 확인
        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("만료된 인증 코드: email={}", email);
            emailVerificationRepository.delete(verification);
            return false;
        }

        // 이미 인증된 코드인지 확인
        if (verification.getIsVerified()) {
            log.warn("이미 사용된 인증 코드: email={}", email);
            return false;
        }

        // 인증 완료 처리
        verification.setIsVerified(true);
        emailVerificationRepository.save(verification);

        // 사용자 이메일 인증 상태 업데이트
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setEmailVerified(true);
            userRepository.save(user);
            log.info("이메일 인증 완료: {}", email);
        }

        return true;
    }

    @Transactional(readOnly = true)
    public boolean isEmailVerified(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.map(User::getEmailVerified).orElse(false);
    }

    private String generateVerificationCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(secureRandom.nextInt(10));
        }
        return code.toString();
    }

    @Scheduled(fixedRate = 300000) // 5분마다 실행
    @Transactional
    public void cleanupExpiredVerifications() {
        emailVerificationRepository.deleteExpiredVerifications(LocalDateTime.now());
        log.debug("만료된 이메일 인증 코드 정리 완료");
    }
}