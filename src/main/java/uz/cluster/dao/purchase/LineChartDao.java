package uz.cluster.dao.purchase;

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
public class LineChartDao {

    private byte id;

    List<Double> monthValue = new ArrayList<>();

}
