package com.futing.diary.config.amqp;

import org.apache.logging.log4j.util.Strings;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  // Declare queues
  protected static final String QUEUE_REGISTRATION_SUCCESS = "registration.success.q";
  // Declare exchanges
  protected static final String EXCHANGE_REGISTRATION_SUCCESS = "registration.success";

  // ===================================================================================================================
  // QUEUES
  // ===================================================================================================================
  @Bean
  Queue registrationSuccess() {
    return QueueBuilder.durable(QUEUE_REGISTRATION_SUCCESS).build();
  }

  // ===================================================================================================================
  // EXCHANGES
  // ===================================================================================================================

  @Bean
  TopicExchange registrationExchange() {
    return new TopicExchange(EXCHANGE_REGISTRATION_SUCCESS);
  }

  // ===================================================================================================================
  // BINDINGS
  // ===================================================================================================================
  @Bean
  Binding registrationSuccessBinding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue)
      .to(exchange)
      .with(Strings.EMPTY);
  }

  // ===================================================================================================================
  // PUBLISHERS
  // ===================================================================================================================
  @Bean
  public RegistrationPublisher geoFenceEventPublisher(RabbitTemplate rabbitTemplate) {
    return new RegistrationPublisher(rabbitTemplate);
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setBeforePublishPostProcessors(message -> {
      MessageProperties messageProperties = message.getMessageProperties();
      messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

      return message;
    });
    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    return rabbitTemplate;
  }
}
