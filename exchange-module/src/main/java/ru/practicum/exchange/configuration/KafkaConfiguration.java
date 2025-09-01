package ru.practicum.exchange.configuration;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;
import ru.practicum.common.Event;
import ru.practicum.common.KafkaTopic;
import ru.practicum.common.dto.ExchangeRateUpdate;
import ru.practicum.exchange.service.RateService;

import java.util.UUID;

@EnableKafkaStreams
public class KafkaConfiguration {

    @Bean
    protected KStream<UUID, ExchangeRateUpdate> rateUpdateStream(StreamsBuilder streamsBuilder, RateService rateService) {
        var stream = streamsBuilder.stream(KafkaTopic.EXCHANGES.name(), Consumed.with(Serdes.UUID(), new JsonSerde<>(ExchangeRateUpdate.class)));
        stream.foreach((key, value) -> rateService.updateRates(value));
        return stream;
    }
}
