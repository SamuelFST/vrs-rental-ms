package vrs.rental_ms.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import vrs.rental_ms.dto.rental.RentalFileMessageDTO;

import static vrs.rental_ms.config.rabbit.RabbitMQConfiguration.FILE_RENTAL_QUEUE;

@Slf4j
@Service
public class FileProducer extends QueueProducer {

    public FileProducer(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    public void sendToGenerateRentalFile(final RentalFileMessageDTO rentalFileMessageDTO) {
        sendMessage(FILE_RENTAL_QUEUE, rentalFileMessageDTO);
    }

}
