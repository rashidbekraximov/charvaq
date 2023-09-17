package uz.cluster.dao.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.entity.references.model.Position;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tabel {

    private int id;

    private String name;

    private Position position;

    List<Integer> hourDays = new ArrayList<>();

}
