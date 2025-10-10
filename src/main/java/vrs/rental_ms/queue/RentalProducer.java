package vrs.rental_ms.queue;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import vrs.rental_ms.dto.rental.RentalMessageDTO;

import static vrs.rental_ms.config.rabbit.RabbitMQConfiguration.PROCESS_RENTAL_QUEUE;

@Slf4j
@Service
@AllArgsConstructor
public class RentalProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendToProcessRentalQueue(RentalMessageDTO rentalMessageDTO) {
        rabbitTemplate.convertAndSend(
                PROCESS_RENTAL_QUEUE,
                rentalMessageDTO);

        log.info("message sent to queue {}: {}", PROCESS_RENTAL_QUEUE, rentalMessageDTO);
    }

}
