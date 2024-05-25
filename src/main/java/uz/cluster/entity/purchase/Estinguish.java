package uz.cluster.entity.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.purchase.AllDebtDao;
import uz.cluster.entity.Auditable;
import uz.cluster.entity.references.model.PaymentType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "estinguish")
public class Estinguish extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "debt_total_value")
    private double debtTotalValue;

    @Column(name = "paid_value")
    private double paidValue;

    @Column(name = "remainder_debt_value")
    private double remainderDebtValue;

    @ManyToOne
    @JoinColumn(name = "payment_type_id")
    private PaymentType paymentType;

    @Transient
    List<AllDebtDao> allDebts = new ArrayList<>();

    @Transient
    private int paymentTypeId;

}
