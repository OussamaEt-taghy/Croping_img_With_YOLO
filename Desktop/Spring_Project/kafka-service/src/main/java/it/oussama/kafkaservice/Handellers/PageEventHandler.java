package it.oussama.kafkaservice.Handellers;

import it.oussama.kafkaservice.Events.PageEvent;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class PageEventHandler {
    @Bean
    public Consumer<PageEvent> pageEventConsumer(){
        return (input)->{
            System.out.println("************");
            System.out.println(input.toString());
            System.out.println("************");
        };
    }
    @Bean
    public Supplier<PageEvent> pageEventSupplier(){
        return ()->{
            return new PageEvent(
                    Math.random()>0.5?"P1":"P2",
                    Math.random()>0.5?"U1":"U2",
                    new Date(),
                    10+new Random().nextInt(10000)
            );
        };
    }
    @Bean
    public Function<KStream<String, PageEvent>, KStream<String, Long>> kStream() {
        // La fonction prend en entrée un flux KStream avec une clé de type String et une valeur de type PageEvent
        // Elle retourne un flux KStream avec une clé de type String et une valeur de type Long
        return (input) ->
                // Filtrer les événements dont la durée est supérieure à 100
                input.filter((k, v) -> v.duration() > 100)
                        // Transformer chaque événement filtré pour renvoyer un flux avec :
                        // - La clé : le nom de la page (v.name())
                        // - La valeur : la durée de l'événement (v.duration())
                        .map((k, v) -> new KeyValue<>(v.name(), v.duration()));
    }


}
