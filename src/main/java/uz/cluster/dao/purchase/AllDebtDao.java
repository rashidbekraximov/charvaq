package uz.cluster.dao.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.enums.MCHJ;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllDebtDao {

    private int key;

    private long id;

    private LocalDate date;

    private String client;

    private double totalValue;

    private double debtTotalValue;

    private MCHJ mchj;
}
