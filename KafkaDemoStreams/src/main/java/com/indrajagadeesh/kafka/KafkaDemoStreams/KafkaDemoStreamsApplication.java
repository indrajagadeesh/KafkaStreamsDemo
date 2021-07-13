package com.indrajagadeesh.kafka.KafkaDemoStreams;

import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;


@SpringBootApplication
public class KafkaDemoStreamsApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaDemoStreamsApplication.class, args);
	}

}
