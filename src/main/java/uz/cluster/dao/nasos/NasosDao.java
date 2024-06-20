package uz.cluster.dao.nasos;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.nasos.Nasos;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NasosDao extends BaseDao {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String client;

    private double workHour;

    private double minPrice;

    private double additionalPrice;

    private double totalSumm;

    private String description;

    public Nasos asDao(NasosDao nasosDao){
        Nasos nasos = new Nasos();
        nasos.setId(nasosDao.getId());
        nasos.setDate(nasosDao.getDate());
        nasos.setClient(nasosDao.getClient());
        nasos.setMinPrice(nasosDao.getMinPrice());
        nasos.setWorkHour(nasosDao.getWorkHour());
        nasos.setAdditionalPrice(nasosDao.getAdditionalPrice());
        nasos.setTotalSumm(nasosDao.getTotalSumm());
        nasos.setDescription(nasosDao.getDescription());
        return nasos;
    }






}
