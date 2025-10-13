package vrs.rental_ms.queue;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import vrs.rental_ms.dto.rental.RentalFinishMessageDTO;
import vrs.rental_ms.dto.rental.RentalMessageDTO;

import static vrs.rental_ms.config.rabbit.RabbitMQConfiguration.FINISH_RENTAL_QUEUE;
import static vrs.rental_ms.config.rabbit.RabbitMQConfiguration.PROCESS_RENTAL_QUEUE;

@Slf4j
@Service
@AllArgsConstructor
public class RentalProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendToProcessRentalQueue(final RentalMessageDTO rentalMessageDTO) {
        sendMessage(PROCESS_RENTAL_QUEUE, rentalMessageDTO);
    }

    public void sendToFinishRentalQueue(final RentalFinishMessageDTO rentalFinishMessageDTO) {
        sendMessage(FINISH_RENTAL_QUEUE, rentalFinishMessageDTO);
    }

    private void sendMessage(final String queueName, final Object message) {
        rabbitTemplate.convertAndSend(
                queueName,
                message);

        log.info("message sent to queue {}: {}", queueName, message);
    }

}
