package vrs.rental_ms.queue;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public abstract class QueueProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(final String queueName, final Object message) {
        rabbitTemplate.convertAndSend(
                queueName,
                message);

        log.info("message sent to queue {}: {}", queueName, message);
    }

}
