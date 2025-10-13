package vrs.rental_ms.config.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    public static final String PROCESS_RENTAL_QUEUE = "vrs-rental-ms.rental.process";
    public static final String FINISH_RENTAL_QUEUE = "vrs-rental-ms.rental.finish";

    @Bean
    public Queue processRentalQueue() {
        return new Queue(PROCESS_RENTAL_QUEUE, true);
    }

    @Bean
    public Queue finishRentalQueue() {
        return new Queue(FINISH_RENTAL_QUEUE, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}