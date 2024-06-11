package com.hostfully.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Min(1)
    private Integer numberOfGuests;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private Owner owner;

    public boolean canBeBooked(Integer numberOfGuestsRequested) {
        return numberOfGuestsRequested <= numberOfGuests;
    }
}
