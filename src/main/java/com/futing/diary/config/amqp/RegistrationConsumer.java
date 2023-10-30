package com.futing.diary.config.amqp;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futing.diary.config.amqp.dto.RegisterMessageDTO;
import com.futing.diary.service.EmailSenderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class RegistrationConsumer {

  private final EmailSenderService mailSender;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @RabbitListener(
    queues = {RabbitMQConfig.QUEUE_REGISTRATION_SUCCESS})
  public void onRegistrationEvent(Message message) throws JsonProcessingException {

    String messageBody = new String(message.getBody());

    log.info("Registration event message received: messageBody={} ", messageBody);

    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    RegisterMessageDTO messageDTO = objectMapper.readValue(messageBody, RegisterMessageDTO.class);

    mailSender.sendRegisterSuccessEmail(messageDTO.getEmail(), messageDTO.getUsername(), messageDTO.getUserId());

  }
}
