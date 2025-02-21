package com.example.event_management.models;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BookingRequest {
    private Long userId;
    private Long eventId;
}
