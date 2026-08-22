package vrs.rental_ms.config.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import vrs.rental_ms.config.properties.RabbitRetryProperties;
import vrs.rental_ms.exception.BadRequestException;
import vrs.rental_ms.exception.NotFoundException;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(RabbitRetryProperties.class)
public class RabbitMQConfiguration {

    public static final String PROCESS_RENTAL_QUEUE = "vrs-rental-ms.rental.process";
    public static final String PROCESS_RENTAL_DLQ = "vrs-rental-ms.rental.process.dlq";

    public static final String FINISH_RENTAL_QUEUE = "vrs-rental-ms.rental.finish";
    public static final String FINISH_RENTAL_DLQ = "vrs-rental-ms.rental.finish.dlq";

    public static final String FILE_RENTAL_QUEUE = "vrs-rental-ms.file.rental";
    public static final String FILE_RENTAL_DLQ = "vrs-rental-ms.file.rental.dlq";

    @Bean
    public Queue processRentalQueue() {
        return QueueBuilder
                .durable(PROCESS_RENTAL_QUEUE)
                .deadLetterExchange("")
                .deadLetterRoutingKey(PROCESS_RENTAL_DLQ)
                .build();
    }

    @Bean
    public Queue processRentalDlq() {
        return QueueBuilder
                .durable(PROCESS_RENTAL_DLQ)
                .build();
    }

    @Bean
    public Queue finishRentalQueue() {
        return QueueBuilder
                .durable(FINISH_RENTAL_QUEUE)
                .deadLetterExchange("")
                .deadLetterRoutingKey(FINISH_RENTAL_DLQ)
                .build();
    }

    @Bean
    public Queue finishRentalDlq() {
        return QueueBuilder
                .durable(FINISH_RENTAL_DLQ)
                .build();
    }

    @Bean
    public Queue fileRentalQueue() {
        return QueueBuilder
                .durable(FILE_RENTAL_QUEUE)
                .deadLetterExchange("")
                .deadLetterRoutingKey(FILE_RENTAL_DLQ)
                .build();
    }

    @Bean
    public Queue fileRentalDlq() {
        return QueueBuilder
                .durable(FILE_RENTAL_DLQ)
                .build();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory connectionFactory,
            RabbitRetryProperties properties
    ) {
        var factory = new SimpleRabbitListenerContainerFactory();

        configurer.configure(factory, connectionFactory);
        factory.setRetryTemplate(retryTemplate(properties));

        return factory;
    }

    private RetryTemplate retryTemplate(RabbitRetryProperties properties) {
        var retryTemplate = new RetryTemplate();

        var retryPolicy = new SimpleRetryPolicy(
                properties.maxRetries(),
                Map.of(
                        BadRequestException.class, false,
                        NotFoundException.class, false
                ),
                true
        );

        retryTemplate.setRetryPolicy(retryPolicy);

        var backOffPolicy = new ExponentialBackOffPolicy();

        backOffPolicy.setInitialInterval(properties.initialInterval().toMillis());
        backOffPolicy.setMultiplier(properties.multiplier());
        backOffPolicy.setMaxInterval(properties.maxInterval().toMillis());

        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }

}