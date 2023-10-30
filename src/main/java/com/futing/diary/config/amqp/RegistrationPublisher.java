package com.futing.diary.config.amqp;

import com.futing.diary.config.amqp.dto.RegisterMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.futing.diary.config.amqp.RabbitMQConfig.EXCHANGE_REGISTRATION_SUCCESS;

@Component
@RequiredArgsConstructor
public class RegistrationPublisher {

  private final RabbitTemplate rabbitTemplate;

  public void publishSuccessRegistration(RegisterMessageDTO messageDTO) {

    rabbitTemplate.convertAndSend(EXCHANGE_REGISTRATION_SUCCESS, "", messageDTO);
  }
}
