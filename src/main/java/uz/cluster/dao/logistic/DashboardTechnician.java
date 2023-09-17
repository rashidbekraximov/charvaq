package uz.cluster.dao.logistic;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardTechnician {

    private int id;

    private String name;

    private String technique;

    List<DashboardLogistic> dashboardLogistics = new ArrayList<>();

}
