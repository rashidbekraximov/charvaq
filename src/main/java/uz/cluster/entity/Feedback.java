package uz.cluster.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "star")
    private byte star;

}
