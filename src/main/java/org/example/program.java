package org.example;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;

public class program {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("acks", "1");
        properties.setProperty("retries", "10");

        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");

       KafkaProducer<String, Customer> kafkaProducer = new KafkaProducer<String, Customer>(properties);
        String topic = "customer-avro";

        Random r = new Random();

        Customer customer = Customer.newBuilder()
                .setFirstName("Nick")
                .setLastName("Peachey")
                .setAge(r.nextInt())
                .setHeight(r.nextFloat())
                .setWeight(r.nextFloat())
                .setAutomatedEmail(r.nextBoolean())
                .build();

        ProducerRecord<String, Customer> producerRecord = new ProducerRecord<String, Customer>(topic, customer);

        kafkaProducer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e == null) {
                    System.out.println("Success");
                    System.out.println(recordMetadata.toString());
                } else {

                }
            }
        });

        kafkaProducer.flush();
        kafkaProducer.close();
    }
}
