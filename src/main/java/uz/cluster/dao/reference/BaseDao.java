package uz.cluster.dao.reference;

import lombok.Getter;
import lombok.Setter;
import uz.cluster.enums.Status;

@Getter
@Setter
public class BaseDao {

    private long id;

    private Status status = Status.ACTIVE;

}
