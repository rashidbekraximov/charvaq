package uz.cluster.entity.nasos;

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
@Audited(withModifiedFlag = true)
@Table(name = "nasos")
public class Nasos extends Auditable {

    @Id
    @GeneratedValue(generator = "nasos_sq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "nasos_sq", sequenceName = "nasos_sq", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

}
