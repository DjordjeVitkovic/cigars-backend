package com.futing.diary.service;

import com.futing.diary.exception.NotFoundException;
import com.futing.diary.model.Token;
import com.futing.diary.model.User;
import com.futing.diary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.futing.diary.util.Constants.ERROR_USER_NOT_EXIST;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSenderService {

  @Value("${spring.mail.username}")
  private String emailAddress;

  private final JavaMailSender javaMailSender;
  private final TokenService tokenService;
  private final UserRepository userRepository;

  public void sendRegisterSuccessEmail(String email, String username, Integer userId) {

    Token token = tokenService.generateVerificationToken(userId);

    sendUserToken(email, username, token);
  }

  public void resendVerificationToken(String email) {
    User user = getUser(email);
    sendUserToken(email, user.getUsername(), tokenService.generateVerificationToken(user.getUserId()));
  }

  public void sendResetPasswordLink(String email) {
    User user = getUser(email);
    sendUserToken(email, user.getUsername(), tokenService.generateResetPasswordToken(user.getUserId()));
  }

  private void sendUserToken(String email, String username, Token token) {
    SimpleMailMessage message = createSimpleMailMessage(email, username, token);

    javaMailSender.send(message);
    log.info("{} token generated and sent for user {}.", token.getTokenType(), username);
  }

  private SimpleMailMessage createSimpleMailMessage(String email, String username, Token token) {
    String mailText = "";
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(emailAddress);
    message.setTo(email);
    switch (token.getTokenType()) {
      case VERIFICATION -> mailText = createVerificationEmailMessage(username, token.getToken());
      case RESET_PASSWORD -> mailText = createResetPasswordEmailMessage(username, token.getToken());
    }
    message.setText(mailText);
    message.setSubject(username);
    return message;
  }

  private User getUser(String email) {
    return userRepository.findByUsernameOrEmail(email)
      .orElseThrow(() -> new NotFoundException(ERROR_USER_NOT_EXIST));
  }

  private String createVerificationEmailMessage(String username, String token) {
    String verificationLink = "http://localhost:8080/api/v1/auth/verify/" + token;
    return "Dear " + username + ",\n\n"
      + "Thank you for registering with MyCigarDiary. Please click the following link to confirm your registration:\n"
      + verificationLink;
  }

  private String createResetPasswordEmailMessage(String username, String token) {
    String resetPasswordLink = "http://localhost:8080/api/v1/auth/reset-password/" + token;

    return "Dear " + username + ",\n\n"
      + "We received a request to reset your password for MyCigarDiary account. "
      + "Please click the following link to reset your password:\n"
      + resetPasswordLink + "\n\n"
      + "If you did not initiate this request, please ignore this email. "
      + "Your account's security is important to us.\n\n"
      + "Thank you for using MyCigarDiary.";
  }

}
