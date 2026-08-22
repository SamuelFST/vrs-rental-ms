package vrs.rental_ms.queue;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class QueueProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(final String queueName, final Object message) {
        rabbitTemplate.convertAndSend(
                queueName,
                message);

        log.info("message sent to queue {}: {}", queueName, message);
    }

}
