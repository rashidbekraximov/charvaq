package uz.cluster.entity.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.entity.Auditable;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "distribution")
public class Distribution {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @Column(name = "user_id")
    private byte userId;

    @Column(name = "percent")
    private double percent;
}