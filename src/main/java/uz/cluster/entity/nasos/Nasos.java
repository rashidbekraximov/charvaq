package uz.cluster.entity.nasos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.Direction;
import uz.cluster.entity.references.model.Employee;
import uz.cluster.entity.references.model.TechniqueType;
import uz.cluster.enums.Status;

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

    @Column(name = "auto")
    private String auto;

    @Column(name = "gas_price")
    private double gasPrice;

    @Column(name = "per_km_gas_cost")
    private double perKmGasCost;

    @Column(name = "ballon")
    private int ballon;

    @Column(name = "ballon_amount")
    private double ballonAmount;

    @Column(name = "per_km_ballon_cost")
    private double perKmBallonCost;

    @Column(name = "oil_price")
    private double oilPrice;

    @Column(name = "per_km_oil_cost")
    private double perKmOilCost;

    @Column(name = "amortization")
    private double amortization;

    @Column(name = "per_km_amortization_cost")
    private double perKmAmortization;

    @Column(name = "auto_number")
    private String autoNumber;

    @Column(name = "per_km_cost_amount")
    private double perKmCostAmount;
}
