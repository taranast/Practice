package com.example.springbootcamelkafka.route;

import jakarta.xml.bind.UnmarshalException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RequestRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        try (DataFormat jaxb = new JaxbDataFormat("com.example.springbootcamelkafka.gen")) {

            onException(UnmarshalException.class)
                    .handled(true)
                    .log("Request Route Error: " + exceptionMessage())
                    .setHeader("Status", simple("Error"))
                    .setBody(exceptionMessage())
                    .to("micrometer:counter:simple.errors.count")
                    .to("direct:statusRoute");

            // Kafka Consumer Requests Topic
            from("{{kafka.broker1.camel-request-topic-path}}")
                    .routeId("Requests Route")
                    .to("micrometer:timer:simple.timer?action=start")
                    .to("micrometer:counter:simple.request.count")
                    .log("Message received from Kafka1 : ${body}")
                    .log("    on the topic ${headers[kafka.TOPIC]}")
                    .log("    on the partition ${headers[kafka.PARTITION]}")
                    .log("    with the offset ${headers[kafka.OFFSET]}")
                    .log("    with the key ${headers[kafka.KEY]}")
                    .unmarshal(jaxb)
                    .to("direct:requestProcessingRoute");
        }
    }
}
