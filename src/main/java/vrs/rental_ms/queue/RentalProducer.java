package vrs.rental_ms.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import vrs.rental_ms.dto.rental.RentalFinishMessageDTO;
import vrs.rental_ms.dto.rental.RentalMessageDTO;

import static vrs.rental_ms.config.rabbit.RabbitMQConfiguration.FINISH_RENTAL_QUEUE;
import static vrs.rental_ms.config.rabbit.RabbitMQConfiguration.PROCESS_RENTAL_QUEUE;

@Slf4j
@Service
public class RentalProducer {

    private final QueueProducer queueProducer;

    public RentalProducer(RabbitTemplate rabbitTemplate) {
        queueProducer = new QueueProducer(rabbitTemplate);
    }

    public void sendToProcessRentalQueue(final RentalMessageDTO rentalMessageDTO) {
        queueProducer.sendMessage(PROCESS_RENTAL_QUEUE, rentalMessageDTO);
    }

    public void sendToFinishRentalQueue(final RentalFinishMessageDTO rentalFinishMessageDTO) {
        queueProducer.sendMessage(FINISH_RENTAL_QUEUE, rentalFinishMessageDTO);
    }

}
