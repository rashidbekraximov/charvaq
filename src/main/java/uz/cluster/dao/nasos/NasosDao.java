package uz.cluster.dao.nasos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.nasos.Nasos;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NasosDao extends BaseDao {
    private int hj;

    private LocalDate date;

    private double time;

    private String custumerName;

    private double minPrice;

    private double additionalPrice;

    private double price;


    public Nasos asDao(NasosDao nasosDao){
        Nasos nasos = new Nasos();
        nasos.setTime(nasosDao.getTime());
        nasos.setCustumerName(nasosDao.getCustumerName());
        nasos.setMinPrice(nasosDao.getMinPrice());
        nasos.setAdditionalPrice(nasosDao.getAdditionalPrice());
        nasos.setPrice(nasosDao.getPrice());
        return nasos;
    }






}
