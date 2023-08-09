package uz.cluster.entity.references.model;


import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.reference.TaxesDao;
import uz.cluster.entity.Auditable;
import uz.cluster.enums.Status;
import uz.cluster.util.GlobalParams;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "taxes")
public class Taxes extends Auditable {

    @Hidden
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToOne
    @JoinColumn(name = "tax_type_id")
    private TaxType taxType;

    @Column(name = "tax_amount")
    private double taxAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVE'")
    private Status status = Status.ACTIVE;

    @Column(name = "cluster_id", columnDefinition = " real default 1 ")
    private int clusterId = GlobalParams.getCurrentClusterId();

    @Transient
    private int taxTypeId;

    public TaxesDao asDao(){
        TaxesDao taxesDao = new TaxesDao();
        taxesDao.setId(getId());
        taxesDao.setTaxTypeId(getTaxTypeId());
        taxesDao.setTaxType(getTaxType());
        taxesDao.setTaxAmount(getTaxAmount());
        taxesDao.setStatus(getStatus());
        return taxesDao;
    }
}
