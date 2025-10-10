package vrs.rental_ms.queue;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import vrs.rental_ms.dto.rental.RentalMessageDTO;
import vrs.rental_ms.service.RentalService;

import static vrs.rental_ms.config.rabbit.RabbitMQConfiguration.PROCESS_RENTAL_QUEUE;

@Slf4j
@Component
@AllArgsConstructor
public class RentalConsumer {

    private final RentalService rentalService;

    @RabbitListener(queues = PROCESS_RENTAL_QUEUE)
    public void processRentalQueueListener(RentalMessageDTO message) {
        log.info("message received in queue {}: {}", PROCESS_RENTAL_QUEUE, message);

        try {
            rentalService.processRental(message);
        } catch (Exception ex) {
            log.error("Exception occurred when processing rental {}: {}", message.getRentalId(), ex.getMessage(), ex);
        }
    }

}
