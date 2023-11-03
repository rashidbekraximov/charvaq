package uz.cluster.entity.lb;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import uz.cluster.dao.lb.LBPurchaseDao;
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
@Table(name = "lb_purchase")
public class LBPurchase extends Auditable {

    @Id
    @GeneratedValue(generator = "lb_purchase_sq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "lb_purchase_sq", sequenceName = "lb_purchase_sq", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "mixer_id")
    private Mixer mixer;

    @Column(name = "customer")
    private String customer;

    @Column(name = "location")
    private String location;

    @Column(name = "mark")
    private int mark;

    @Column(name = "amount")
    private double amount;

    @Column(name = "sement")
    private double sement;

    @Column(name = "klines")
    private double klines;

    @Column(name = "sheben")
    private double sheben;

    @Column(name = "pesok")
    private double pesok;

    @Column(name = "dobavka")
    private double dobavka;

    @Column(name = "antimaroz")
    private double antimaroz;

    @Column(name = "value")
    private double value;

    @Column(name = "total_value")
    private double totalValue;

    @Column(name = "nasos")
    private double nasos;

    @Column(name = "hour")
    private double hour;

    @Column(name = "km")
    private double km;

    @Column(name = "given_value")
    private double givenValue;

    @Column(name = "debt_total_value")
    private double debtTotalValue;

    @Column(name = "xodka",columnDefinition = "real default 0")
    private byte xodka;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVE'")
    private Status status = Status.ACTIVE;

    @Column(name = "description")
    private String description;

    @Transient
    private int mixerId;

    public LBPurchaseDao asDao() {
        LBPurchaseDao lbPurchaseDao = new LBPurchaseDao();
        lbPurchaseDao.setId(getId());
        lbPurchaseDao.setDate(getDate());
        lbPurchaseDao.setCustomer(getCustomer());
        lbPurchaseDao.setLocation(getLocation());
        lbPurchaseDao.setMark(getMark());
        lbPurchaseDao.setAmount(getAmount());
        lbPurchaseDao.setSement(getSement());
        lbPurchaseDao.setKlines(getKlines());
        lbPurchaseDao.setSheben(getSheben());
        lbPurchaseDao.setPesok(getPesok());
        lbPurchaseDao.setDobavka(getDobavka());
        lbPurchaseDao.setValue(getValue());
        lbPurchaseDao.setTotalValue(getTotalValue());
        lbPurchaseDao.setNasos(getNasos());
        lbPurchaseDao.setGivenValue(getGivenValue());
        lbPurchaseDao.setDebtTotalValue(getDebtTotalValue());
        lbPurchaseDao.setXodka(getXodka());
        lbPurchaseDao.setKm(getKm());
        lbPurchaseDao.setMixer(getMixer());
        lbPurchaseDao.setHour(getHour());
        lbPurchaseDao.setMixerId(getMixerId());
        lbPurchaseDao.setStatus(getStatus());
        lbPurchaseDao.setAntimaroz(getAntimaroz());
        lbPurchaseDao.setDescription(getDescription());
        return lbPurchaseDao;
    }
}
