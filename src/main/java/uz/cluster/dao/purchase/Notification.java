package uz.cluster.dao.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    private int id;

    private int days;

    private String client;

    private String location;

    private double debtTotalValue;
}
