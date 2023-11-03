package uz.cluster.dao.purchase;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentFilter {

    private int id;

    private String client;

    private int technicianId;

    private int orderId;

    private int mark;

    private int auto;

    private int amount;

    private String checkNumber;

    private String documentCode;

    private LocalDate beginDate;

    private LocalDate endDate;

    @JsonProperty
    private boolean isDebt;

    private int paymentTypeId;

}
