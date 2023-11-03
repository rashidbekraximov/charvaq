package uz.cluster.dao.purchase;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.enums.Status;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyIncomeDao extends BaseDao {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private double value;

    private Status status;

}
