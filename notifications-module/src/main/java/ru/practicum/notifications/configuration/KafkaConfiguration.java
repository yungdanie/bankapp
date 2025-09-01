package ru.practicum.notifications.configuration;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;
import ru.practicum.common.Event;
import ru.practicum.common.KafkaTopic;
import ru.practicum.notifications.service.NotificationService;

import java.util.UUID;

@Configuration
@EnableKafkaStreams
public class KafkaConfiguration {

    @Bean
    public KStream<UUID, Event> notificationEventStream(StreamsBuilder builder, NotificationService notificationService) {
        var stream = builder.stream(KafkaTopic.NOTIFICATIONS.name(), Consumed.with(Serdes.UUID(), new JsonSerde<>(Event.class)));
        stream.foreach(notificationService::processNotification);
        return stream;
    }
}
