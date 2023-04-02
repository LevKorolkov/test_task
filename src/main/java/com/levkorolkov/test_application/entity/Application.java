package com.levkorolkov.test_application.entity;

import com.levkorolkov.test_application.entity.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column(nullable = false)
    private String text;
    @Column(updatable = false)
    private LocalDateTime dateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;
    public Application() {
    }

    @PrePersist
    protected void onCreate(){
        this.dateTime = LocalDateTime.now();
    }
}
