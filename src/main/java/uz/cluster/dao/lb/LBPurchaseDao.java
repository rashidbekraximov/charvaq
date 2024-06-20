package uz.cluster.dao.lb;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.lb.LBPurchase;
import uz.cluster.entity.lb.Mixer;
import uz.cluster.entity.lb.MixerUse;
import uz.cluster.enums.MCHJ;
import uz.cluster.enums.Status;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    private double prostoy;

    private double debtTotalValue;

    private double hour;

    private double antimaroz;

    private double km;

    private Status status;

    private MCHJ mchj;

    private String description;

    private List<MixerUse> mixerUses = new ArrayList<>();

    public LBPurchase copy(LBPurchaseDao lbPurchaseDao){
        LBPurchase order = new LBPurchase();
        order.setId(lbPurchaseDao.getId());
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
        order.setKm(lbPurchaseDao.getKm());
        order.setHour(lbPurchaseDao.getHour());
        order.setAntimaroz(lbPurchaseDao.getAntimaroz());
        order.setStatus(lbPurchaseDao.getStatus());
        order.setDescription(lbPurchaseDao.getDescription());
        order.setMchj(lbPurchaseDao.getMchj());
        order.setMixerUses(lbPurchaseDao.getMixerUses());
        order.setProstoy(lbPurchaseDao.getProstoy());
        return order;
    }
}
