package com.example.event_management.services;

import com.example.event_management.models.Booking;
import com.example.event_management.models.BookingRequest;
import com.example.event_management.models.Event;
import com.example.event_management.models.User;
import com.example.event_management.repositories.BookingRepository;
import com.example.event_management.repositories.EventRepository;
import com.example.event_management.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    /**
     * Crea una prenotazione per un evento
     */
    public Booking createBooking(BookingRequest request) {
        // Trova utente e evento nel database
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        // Controlla se ci sono posti disponibili
        if (event.getSeatsAvailable() <= 0) {
            throw new RuntimeException("Posti esauriti per questo evento!");
        }

        // Crea e salva la prenotazione
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setEvent(event);
        bookingRepository.save(booking);

        // Aggiorna il numero di posti disponibili
        event.setSeatsAvailable(event.getSeatsAvailable() - 1);
        eventRepository.save(event);

        return booking;
    }
}
