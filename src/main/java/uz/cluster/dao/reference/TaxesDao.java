package uz.cluster.dao.reference;

import lombok.Getter;
import lombok.Setter;
import uz.cluster.entity.references.model.TaxType;
import uz.cluster.entity.references.model.Taxes;
import uz.cluster.enums.Status;

@Getter
@Setter
public class TaxesDao extends BaseDao{

    private int taxTypeId;

    private TaxType taxType;

    private double taxAmount;

    private Status status;

    public Taxes copy(TaxesDao taxesDao){
        Taxes taxes = new Taxes();
        taxes.setId((int) taxesDao.getId());
        taxes.setTaxTypeId(taxesDao.getTaxTypeId());
        taxes.setStatus(taxesDao.getStatus());
        taxes.setTaxAmount(taxesDao.getTaxAmount());
        taxes.setTaxType(taxesDao.getTaxType());
        return taxes;
    }

}
