package uz.cluster.dao.lb;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.lb.LBPurchase;
import uz.cluster.entity.lb.Mixer;
import uz.cluster.enums.Status;

import javax.persistence.Column;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LBPurchaseDao  extends BaseDao {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String customer;

    private String location;

    private int mark;

    private double amount;

    private double sement;

    private double klines;

    private double sheben;

    private double pesok;

    private double dobavka;

    private double value;

    private double totalValue;

    private double nasos;

    private double givenValue;

    private double debtTotalValue;

    private byte xodka;

    private double hour;

    private int mixerId;

    private double antimaroz;

    private double km;

    private Mixer mixer;

    private Status status;

    private String description;

    public LBPurchase copy(LBPurchaseDao lbPurchaseDao){
        LBPurchase order = new LBPurchase();
        order.setId((int) lbPurchaseDao.getId());
        order.setCustomer(lbPurchaseDao.getCustomer());
        order.setLocation(lbPurchaseDao.getLocation());
        order.setDate(lbPurchaseDao.getDate());
        order.setMark(lbPurchaseDao.getMark());
        order.setAmount(lbPurchaseDao.getAmount());
        order.setPesok(lbPurchaseDao.getPesok());
        order.setSheben(lbPurchaseDao.getSheben());
        order.setKlines(lbPurchaseDao.getKlines());
        order.setDobavka(lbPurchaseDao.getDobavka());
        order.setSement(lbPurchaseDao.getSement());
        order.setValue(lbPurchaseDao.getValue());
        order.setTotalValue(lbPurchaseDao.getTotalValue());
        order.setNasos(lbPurchaseDao.getNasos());
        order.setGivenValue(lbPurchaseDao.getGivenValue());
        order.setDebtTotalValue(lbPurchaseDao.getDebtTotalValue());
        order.setXodka(lbPurchaseDao.getXodka());
        order.setKm(lbPurchaseDao.getKm());
        order.setHour(lbPurchaseDao.getHour());
        order.setMixer(lbPurchaseDao.getMixer());
        order.setMixerId(lbPurchaseDao.getMixerId());
        order.setAntimaroz(lbPurchaseDao.getAntimaroz());
        order.setStatus(lbPurchaseDao.getStatus());
        order.setDescription(lbPurchaseDao.getDescription());
        return order;
    }
}
