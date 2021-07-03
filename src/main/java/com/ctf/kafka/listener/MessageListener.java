package com.ctf.kafka.listener;

import com.ctf.kafka.processor.MessageProcessor;
import ctf.avro.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {

    private final MessageProcessor messageProcessor;

    @KafkaListener(topics = "${kafka.consumer.topic}")
    public void listen(ConsumerRecord<String, Message> consumerRecord, final Acknowledgment acknowledgment) {

        final var recordStringMeta = String.format("%s-%d-%d",
                consumerRecord.topic(),
                consumerRecord.partition(),
                consumerRecord.offset());

        log.info("Received Message: Meta:{}, Value: {}", recordStringMeta, consumerRecord.value());
        this.messageProcessor.processMessage(consumerRecord);
        acknowledgment.acknowledge();
    }

}
