package com.example.springbootcamelkafka.route;

import com.example.springbootcamelkafka.gen.Response;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class StatusRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        try (JaxbDataFormat jaxb = new JaxbDataFormat("com.example.springbootcamelkafka.gen")) {

            from("direct:statusRoute")
                    .process(exchange -> {
                        Response response = new Response();
                        response.setStatus(exchange.getIn().getHeader("Status", String.class));
                        response.setMessage(exchange.getIn().getBody(String.class));
                        exchange.getMessage().setBody(response, Response.class);
                    })
                    .marshal(jaxb)
                    .setHeader(KafkaConstants.KEY, simple("Camel"))
                    .to("kafka:status_topic?brokers={{kafka.broker1.host}}")
                    .to("micrometer:timer:simple.timer?action=stop");
        }
    }
}
