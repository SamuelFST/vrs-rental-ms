package vrs.rental_ms.queue;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import vrs.rental_ms.dto.rental.RentalFileMessageDTO;
import vrs.rental_ms.service.FileService;

import static vrs.rental_ms.config.rabbit.RabbitMQConfiguration.FILE_RENTAL_QUEUE;

@Slf4j
@Component
@AllArgsConstructor
public class FileConsumer {

    private final FileService fileService;

    @RabbitListener(queues = FILE_RENTAL_QUEUE)
    public void fileRentalQueueListener(RentalFileMessageDTO message) {
        log.info("message received in queue {}: {}", FILE_RENTAL_QUEUE, message);

        fileService.generateRentalFile(message);

        log.info("Rental file generated successfully for message: {}", message);
    }

}
