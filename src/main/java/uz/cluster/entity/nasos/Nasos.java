package uz.cluster.entity.nasos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.nasos.NasosDao;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.Direction;
import uz.cluster.entity.references.model.Employee;
import uz.cluster.entity.references.model.TechniqueType;
import uz.cluster.enums.Status;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

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
    private long id;

    @Column(name = "date")
    private double time;

    @Column(name = "custumer_name")
    private String custumerName;

    @Column(name = "min_price")
    private double minPrice;

    @Column(name = "additional_price")
    private double additionalPrice;

    @Column(name = "price")
    private double price;

    public Nasos asDao(){
        Nasos nasos = new Nasos();
        nasos.setTime(getTime());
        nasos.setCustumerName(getCustumerName());
        nasos.setMinPrice(getMinPrice());
        nasos.setAdditionalPrice(getAdditionalPrice());
        nasos.setPrice(getPrice());
        return nasos;
    }
}
