package com.example.InventoryService.KafkaConfig;

import com.example.InventoryService.DTO.OrderCanceledEvent;
import com.example.InventoryService.DTO.OrderCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    /* ---------------- ORDER CANCELLED ---------------- */

    @Bean
    public ConsumerFactory<String, OrderCanceledEvent> orderCancelledConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(),
                new StringDeserializer(),
                jsonDeserializer(OrderCanceledEvent.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCanceledEvent> orderCancelledKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderCanceledEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderCancelledConsumerFactory());
        return factory;
    }

    /* ---------------- ORDER COMPLETED ---------------- */

    @Bean
    public ConsumerFactory<String, OrderCompletedEvent> orderCompletedConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(),
                new StringDeserializer(),
                jsonDeserializer(OrderCompletedEvent.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCompletedEvent> orderCompletedKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderCompletedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderCompletedConsumerFactory());
        return factory;
    }

    /* ---------------- JSON DESERIALIZER ---------------- */

    private <T> JsonDeserializer<T> jsonDeserializer(Class<T> clazz) {
        JsonDeserializer<T> deserializer = new JsonDeserializer<>(clazz);
        deserializer.addTrustedPackages("com.example.*"); // allow your package
        deserializer.setUseTypeHeaders(false); // rely on default type
        return deserializer;
    }
}
