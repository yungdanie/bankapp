package ru.practicum.exchangegenerator.kafka;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.common.KafkaTopic;
import ru.practicum.common.dto.ExchangeRateUpdate;
import ru.practicum.exchangegenerator.service.ExchangeGeneratorService;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(topics = "EXCHANGES")
class KafkaTest {

    private final ExchangeGeneratorService exchangeGeneratorService;

    private final EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    public KafkaTest(
            ExchangeGeneratorService exchangeGeneratorService,
            EmbeddedKafkaBroker embeddedKafkaBroker
    ) {
        this.exchangeGeneratorService = exchangeGeneratorService;
        this.embeddedKafkaBroker = embeddedKafkaBroker;
    }

    @Test
    void testExchangeGenerator() {
        try (var consumerForTest = new DefaultKafkaConsumerFactory<>(
                KafkaTestUtils.consumerProps("test", "true", embeddedKafkaBroker),
                new StringDeserializer(),
                new JsonDeserializer<>(ExchangeRateUpdate.class)
        ).createConsumer()) {
            consumerForTest.subscribe(List.of(KafkaTopic.EXCHANGES.name()));

            exchangeGeneratorService.generateNewRates();

            var newExchangeRates = KafkaTestUtils.getRecords(consumerForTest);

            Assertions.assertThat(newExchangeRates).isNotNull();

            newExchangeRates.forEach(newExchangeRate -> {
                Assertions.assertThat(newExchangeRate.value()).isNotNull();
                Assertions.assertThat(newExchangeRate.value().fromCurrencyCode()).isNotNull();
                Assertions.assertThat(newExchangeRate.value().toCurrencyCode()).isNotNull();
                Assertions.assertThat(newExchangeRate.value().rate()).isNotNull();
            });
        }
    }
}
