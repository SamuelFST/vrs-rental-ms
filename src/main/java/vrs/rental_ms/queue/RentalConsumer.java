package vrs.rental_ms.queue;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import vrs.rental_ms.dto.rental.RentalFinishMessageDTO;
import vrs.rental_ms.dto.rental.RentalMessageDTO;
import vrs.rental_ms.service.RentalService;

import static vrs.rental_ms.config.rabbit.RabbitMQConfiguration.FINISH_RENTAL_QUEUE;
import static vrs.rental_ms.config.rabbit.RabbitMQConfiguration.PROCESS_RENTAL_QUEUE;

@Slf4j
@Component
@AllArgsConstructor
public class RentalConsumer {

    private final RentalService rentalService;

    @RabbitListener(queues = PROCESS_RENTAL_QUEUE)
    public void processRentalQueueListener(RentalMessageDTO message) {
        logMessage(PROCESS_RENTAL_QUEUE, message);
        rentalService.processRental(message);
    }

    @RabbitListener(queues = FINISH_RENTAL_QUEUE)
    public void processRentalFinishQueueListener(RentalFinishMessageDTO message) {
        logMessage(FINISH_RENTAL_QUEUE, message);
        rentalService.processRentalFinish(message);
    }

    private void logMessage(final String queueName, final Object message) {
        log.info("message received in queue {}: {}", queueName, message);
    }

}
