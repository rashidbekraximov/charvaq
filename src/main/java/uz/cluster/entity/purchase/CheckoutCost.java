package uz.cluster.entity.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.produce.CostDao;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.CostType;
import uz.cluster.entity.references.model.Direction;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "checkout_cost")
public class CheckoutCost extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "cost_type_id")
    private CostType costType;

    @Column(name = "cost")
    private String costName;

    @Column(name = "amount")
    private double amount;

    @Column(name = "product_id",columnDefinition = "real default 0")
    private byte productId;

    @Column(name = "description",columnDefinition = "varchar(100) default ' '")
    private String description;

    @Transient
    private int costTypeId;
}
