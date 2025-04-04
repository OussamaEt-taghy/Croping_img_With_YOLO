package it.oussama.kafkaservice.Events;

import java.util.Date;

public record PageEvent(String name, String user, Date date, long duration) {
}
