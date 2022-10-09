package sample.data.jpa.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "appointments")
public class Appointment {

    public Appointment( Timestamp start, Timestamp end, String title) {
        this.start = start;
        this.end = end;
        this.title = title;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Timestamp start;
    private Timestamp end;
    private String title;

    @ManyToOne
    private Client client;
    @ManyToOne
    private Prof prof;

}
