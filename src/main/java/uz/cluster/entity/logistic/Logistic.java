package uz.cluster.entity.logistic;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.entity.Auditable;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "logistic")
public class Logistic extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "document_id")
    private int documentId;

    @Column(name = "cost_id")
    private int costId;

    @Column(name = "is_purchase")
    private boolean isPurchase;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private Technician technician;

    @Column(name = "amount")
    private double amount;

}
