package com.vivaeventos.notification.infrastructure.messaging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RabbitMQConfig - Pruebas unitarias")
class RabbitMQConfigTest {

    private final RabbitMQConfig config = new RabbitMQConfig();

    @Test
    @DisplayName("Debe crear el exchange correctamente")
    void shouldCreateExchange() {
        TopicExchange exchange = config.vivaeventosExchange();

        assertThat(exchange).isNotNull();
        assertThat(exchange.getName()).isEqualTo("vivaeventos.events");
        assertThat(exchange.isDurable()).isTrue();
    }

    @Test
    @DisplayName("Debe crear la cola correctamente")
    void shouldCreateQueue() {
        Queue queue = config.eventoCanceladoQueue();

        assertThat(queue).isNotNull();
        assertThat(queue.getName()).isEqualTo("evento.cancelado");
        assertThat(queue.isDurable()).isTrue();
    }

    @Test
    @DisplayName("Debe crear el binding correctamente")
    void shouldCreateBinding() {
        Queue queue = config.eventoCanceladoQueue();
        TopicExchange exchange = config.vivaeventosExchange();
        Binding binding = config.eventoCanceladoBinding(queue, exchange);

        assertThat(binding).isNotNull();
        assertThat(binding.getDestination()).isEqualTo("evento.cancelado");
        assertThat(binding.getExchange()).isEqualTo("vivaeventos.events");
        assertThat(binding.getRoutingKey()).isEqualTo("evento.cancelado");
    }
}