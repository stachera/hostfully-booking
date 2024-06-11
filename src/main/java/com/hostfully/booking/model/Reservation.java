package com.hostfully.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime dateStart;

    @NotNull
    private LocalDateTime dateEnd;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReservationType type;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    public void updateDates(LocalDateTime dateStart, LocalDateTime dateEnd) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }
}
