package uz.cluster.entity.produce;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.produce.ReadyProductDao;
import uz.cluster.dao.purchase.RemainderDao;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.references.model.Unit;
import uz.cluster.enums.Status;

import javax.persistence.*;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "ready_product")
public class ReadyProduct extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "amount")
    private double amount;

    @Column(name = "cost_amount",columnDefinition = "real default 0")
    private double costAmount;

    @Column(name = "all_cost_amount",columnDefinition = "real default 0")
    private double allCostAmount;

    @Transient
    private int productTypeId;

    public ReadyProductDao asDao(){
        ReadyProductDao remainderDao = new ReadyProductDao();
        remainderDao.setId(getId());
        remainderDao.setProductType(getProductType());
        remainderDao.setProductTypeId(getProductTypeId());
        remainderDao.setAmount(getAmount());
        remainderDao.setCostAmount(getCostAmount());
        remainderDao.setDate(getDate());
        return remainderDao;
    }
}
