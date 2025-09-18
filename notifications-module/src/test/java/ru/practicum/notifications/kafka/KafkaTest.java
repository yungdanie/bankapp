package ru.practicum.notifications.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.practicum.common.Event;
import ru.practicum.notifications.service.NotificationService;

import java.time.Duration;
import java.util.UUID;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(topics = "NOTIFICATIONS")
class KafkaTest {

    @Autowired
    EmbeddedKafkaBroker embeddedKafka;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    void consumerTest() {
        var props = KafkaTestUtils.producerProps(embeddedKafka);
        var uuid = UUID.randomUUID();

        try (var producer = new KafkaProducer<UUID, Event>(
                props, new UUIDSerializer(), new JsonSerializer<>())) {
            producer.send(new ProducerRecord<>("NOTIFICATIONS", uuid, Event.WITHDRAW));
            producer.flush();
        }

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() ->
                verify(notificationService, atLeastOnce())
                        .processNotification(any(UUID.class), any(Event.class)));
    }
}
