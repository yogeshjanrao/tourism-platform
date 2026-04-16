package com.microservices.auth.service.impl;

import com.microservices.auth.dto.response.ResponseDTO;
import com.microservices.auth.entity.User;
import com.microservices.auth.exception.AuthServiceException;
import com.microservices.auth.repository.UserRepository;
import com.microservices.auth.service.TwoFactorAuthService;
import com.microservices.auth.util.MailSenderUtil;
import com.microservices.auth.util.OtpUtil;
import com.microservices.auth.util.ThymeleafUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.Duration;

@Service
public class TwoFactorAuthServiceImpl implements TwoFactorAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private ThymeleafUtil thymeleafUtil;

    @Autowired
    private MailSenderUtil mailSenderUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public ResponseDTO sendEmailOtp(String email) {

        if(email.isEmpty()) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "Email cannot be empty or null");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new AuthServiceException(HttpStatus.BAD_REQUEST, "User not found for this email " + email));

        // 🚫 Check block
        if (redisTemplate.hasKey("OTP:BLOCK:" + email)) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "Too many attempts. Try later.");
        }

        // ⏳ Check cooldown
        if (redisTemplate.hasKey("OTP:COOLDOWN:" + email)) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "Please wait before requesting another OTP");
        }

        // 🔢 Check request count
        Long count = redisTemplate.opsForValue()
                .increment("OTP:COUNT:" + email);

        if (count == 1) {
            redisTemplate.expire("OTP:COUNT:" + email, Duration.ofMinutes(15));
        }

        if (count > 3) {
            // 🚫 Block user
            redisTemplate.opsForValue()
                    .set("OTP:BLOCK:" + email, "BLOCKED", Duration.ofMinutes(15));
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "Too many OTP requests. You are blocked for 15 minutes.");
        }

        // 🔁 Reuse OTP if exists (optional optimization)
        String existingOtp = redisTemplate.opsForValue()
                .get("OTP:" + email);

        if (existingOtp != null) {
           throw new AuthServiceException(HttpStatus.BAD_REQUEST, "OTP already sent. Please check your email/SMS.");
        }

        // 🔢 Generate OTP
        String otp = otpUtil.generateOtp();

        // Store OTP (15 min)
        redisTemplate.opsForValue()
                .set("OTP:" + email, otp, Duration.ofMinutes(15));

        // ⏳ Cooldown (60 sec)
        redisTemplate.opsForValue()
                .set("OTP:COOLDOWN:" + email, "WAIT", Duration.ofSeconds(60));

        //Generate Thymeleaf Template
        Context context = new Context();
        context.setVariable("name", user.getFirstName()
                .concat(" ")
                .concat(user.getLastName()));
        context.setVariable("otp", otp);
        context.setVariable("appName", "Tourism Maharashtra");

        String template = thymeleafUtil.process("otp-email", context);

        //Send email
        mailSenderUtil.sendMail(user.getEmail(), "Otp Verification Mail", template);

        return new ResponseDTO(HttpStatus.OK.value(), null, "Otp Verification Mail Sent Successfully to email " + user.getEmail() + " otp valid for next 15 minutes");
    }

    @Override
    public ResponseDTO verifyEmailOtp(String email, String otp) {

        // 🔹 Basic validation
        if (email == null || email.isBlank()) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "Email cannot be empty");
        }

        if (otp == null || otp.isBlank()) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "OTP cannot be empty");
        }

        // 🔹 Fetch user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthServiceException(
                        HttpStatus.NOT_FOUND,
                        "User not found for email: " + email
                ));

        final String OTP_KEY = "OTP:" + email;
        final String ATTEMPT_KEY = "OTP:ATTEMPT:" + email;
        final String BLOCK_KEY = "OTP:BLOCK:" + email;

        // 🚫 Check if user is blocked
        if (Boolean.TRUE.equals(redisTemplate.hasKey(BLOCK_KEY))) {
            throw new AuthServiceException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Too many invalid attempts. Try again later."
            );
        }

        // 🔍 Get OTP from Redis
        String redisOtp = redisTemplate.opsForValue().get(OTP_KEY);

        // ⏳ OTP expired or not found
        if (redisOtp == null) {
            throw new AuthServiceException(
                    HttpStatus.BAD_REQUEST,
                    "OTP expired or not found"
            );
        }

        // ❌ Invalid OTP handling
        if (!redisOtp.equals(otp)) {

            Long attempts = redisTemplate.opsForValue()
                    .increment(ATTEMPT_KEY);

            // set expiry for attempts (5 min window)
            if (attempts != null && attempts == 1) {
                redisTemplate.expire(ATTEMPT_KEY, Duration.ofMinutes(5));
            }

            // 🚫 Block after 3 attempts
            if (attempts != null && attempts >= 3) {
                redisTemplate.opsForValue().set(
                        BLOCK_KEY,
                        "BLOCKED",
                        Duration.ofMinutes(10)
                );
            }

            throw new AuthServiceException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid OTP"
            );
        }

        // ✅ SUCCESS CASE

        // Mark verified
        user.setIsEmailVerified(true);
        userRepository.save(user);

        // 🧹 Clean Redis keys
        redisTemplate.delete(OTP_KEY);
        redisTemplate.delete(ATTEMPT_KEY);

        return new ResponseDTO(
                HttpStatus.OK.value(),
                null,
                "OTP verified successfully"
        );
    }

    @Override
    public String sendContactOtp(String contact) {
        return "";
    }

    @Override
    public String verifyContactOtp(String contact, String otp) {
        return "";
    }


}
