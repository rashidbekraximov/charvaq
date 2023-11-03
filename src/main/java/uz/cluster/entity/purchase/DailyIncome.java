package uz.cluster.entity.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.entity.Auditable;
import uz.cluster.enums.Status;

import javax.persistence.*;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited(withModifiedFlag = true)
@Table(name = "daily_income")
public class DailyIncome extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "product_id",columnDefinition = "real default 0")
    private int productId;

    @Column(name = "income")
    private double income;

    @Column(name = "spending")
    private double spending;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVE'")
    private Status status = Status.ACTIVE;
}