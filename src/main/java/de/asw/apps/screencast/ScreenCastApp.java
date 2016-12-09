package de.asw.apps.screencast;

import io.undertow.UndertowOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;

@EnableScheduling
@SpringBootApplication
public class ScreenCastApp {

    @Value("${server.port}")
    String serverPort;

    public static void main(String[] args) {

        System.setProperty("java.awt.headless", "false");

        SpringApplication.run(ScreenCastApp.class, args);
    }

    @Bean
    UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
        UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();
        factory.addBuilderCustomizers(
                builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));
        return factory;
    }


    @EventListener
    public void run(ApplicationReadyEvent are) throws Exception {

        String hostName = InetAddress.getLocalHost().getHostName();
        System.out.println("########################################################>");
        System.out.printf("####### ScreenCasting running under: http://%s:%s/%n", hostName, serverPort);
        System.out.println("########################################################>");
    }
}
