package uz.cluster.entity.lb;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.envers.Audited;
import uz.cluster.dao.lb.LBPriceDao;
import uz.cluster.dao.purchase.PriceDao;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.references.model.Unit;
import uz.cluster.enums.MCHJ;
import uz.cluster.enums.Status;

import javax.persistence.*;
import java.time.LocalDate;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "lb_cost")
public class LBCost extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @Column(name = "purchase_id")
    private Long purchaseId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "product_cost_amount")
    private double productCostAmount;

    @Column(name = "auto_cost_amount")
    private double autoCostAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "mchj", columnDefinition = "varchar(200) default 'LEADER_BETON_1'")
    private MCHJ mchj;
}

