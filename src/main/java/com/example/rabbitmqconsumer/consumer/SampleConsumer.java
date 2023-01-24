package com.example.rabbitmqconsumer.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SampleConsumer {
    private final SampleConfig sampleConfig;
    public SampleConsumer(SampleConfig sampleConfig) throws IOException {
        this.sampleConfig = sampleConfig;
        consume();
    }
    void consume() throws IOException {
        boolean autoAck = false;
        Channel channel = sampleConfig.channel();
        Queue sampleQueue = sampleConfig.queue();
        channel.basicConsume(sampleQueue.getName(), autoAck, "myConsumerTag",
                new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String routingKey = envelope.getRoutingKey();
                        String contentType = properties.getContentType();
                        long deliveryTag = envelope.getDeliveryTag();
                        System.out.println(">>>> routingKey : " + routingKey +", contentType : " + contentType +", deliveryTag: " + deliveryTag);
                        channel.basicAck(deliveryTag, false);
                    }
                });
    }
}
