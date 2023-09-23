package uz.cluster.dao.logistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.logistic.BringDrobilkaProduct;
import uz.cluster.entity.logistic.Drobilka;
import uz.cluster.entity.logistic.Technician;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.references.model.Unit;
import uz.cluster.enums.MCHJ;
import uz.cluster.enums.Status;

import javax.persistence.Transient;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BringDrobilkaProductDao extends BaseDao {

    private ProductType productType;

    private Unit unit;

    private Technician technician;

    private LocalDate date;

    private double amount;

    private double km;

    private double income;

    private int productTypeId;

    private int unitId;

    private int technicianId;

    private MCHJ mchj;

    public BringDrobilkaProduct copy(BringDrobilkaProductDao bringDrobilkaProductDao){
        BringDrobilkaProduct bringDrobilkaProduct = new BringDrobilkaProduct();
        bringDrobilkaProduct.setId((int) bringDrobilkaProductDao.getId());
        bringDrobilkaProduct.setUnit(bringDrobilkaProductDao.getUnit());
        bringDrobilkaProduct.setUnitId(bringDrobilkaProductDao.getUnitId());
        bringDrobilkaProduct.setProductType(bringDrobilkaProductDao.getProductType());
        bringDrobilkaProduct.setProductTypeId(bringDrobilkaProductDao.getProductTypeId());
        bringDrobilkaProduct.setTechnicianId(bringDrobilkaProductDao.getTechnicianId());
        bringDrobilkaProduct.setTechnician(bringDrobilkaProductDao.getTechnician());
        bringDrobilkaProduct.setAmount(bringDrobilkaProductDao.getAmount());
        bringDrobilkaProduct.setIncome(bringDrobilkaProductDao.getIncome());
        bringDrobilkaProduct.setKm(bringDrobilkaProductDao.getKm());
        bringDrobilkaProduct.setDate(bringDrobilkaProductDao.getDate());
        bringDrobilkaProduct.setMchj(bringDrobilkaProductDao.getMchj());
        return bringDrobilkaProduct;
    }
}

