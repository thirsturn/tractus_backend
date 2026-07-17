package com.tractus.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity // Marks this class as a JPA entity (a table in the database)
@Table(name = "spaces") // Specifies the exact name of the database table
@Data // Lombok: Generates getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // Lombok: Generates a no-arguments constructor (required by JPA)
@AllArgsConstructor // Lombok: Generates a constructor with all arguments
public class Space {

    @Id // Marks this field as the Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID (1, 2, 3...)
    private Long id;

    @Column(nullable = false, unique = true) // This column cannot be null and must be unique
    private String name;

    @Column(columnDefinition = "TEXT") // Uses TEXT type in SQL for longer strings
    private String description;

    // A Space can have MANY Threads (1-to-N relationship)
    // "mappedBy = 'space'" means the 'space' field in the Thread class owns the Foreign Key
    // "cascade = CascadeType.ALL" means if a Space is deleted, all its Threads are also deleted
    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Thread> threads;
}
