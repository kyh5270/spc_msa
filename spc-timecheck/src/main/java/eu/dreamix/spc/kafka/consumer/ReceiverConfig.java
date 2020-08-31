package eu.dreamix.spc.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import eu.dreamix.spc.entity.RandomDateMessage;
import eu.dreamix.spc.entity.TwitterMessage;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class ReceiverConfig {

//  @Value("${spring.kafka.bootstrap-servers}")
    @Value("localhost:9092")
    private String bootstrapServers;

//    @Value("${spring.kafka.consumer.group-id}")
//    @Value("console-consumer-58511")
    @Value("console-consumer-16341")
    private String consumerGroupId;


    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        
        return props;
    }

    @Bean
    public ConsumerFactory<String, RandomDateMessage> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(),
                new JsonDeserializer<>(RandomDateMessage.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RandomDateMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RandomDateMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        
        factory.setConsumerFactory(consumerFactory());
        
        //cousumer processing thread count(not meaning consumer count)
        factory.setConcurrency(3);

        return factory;
    }

    @Bean
    public Receiver receiver() {
        return new Receiver();
    }
}
