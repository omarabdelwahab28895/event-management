package com.example.event_management.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDate date;
    private String location;
    private int seatsAvailable;

    @ManyToOne
    private User createdBy;
}
