package uz.cluster.dao.reference;

import lombok.Getter;
import lombok.Setter;
import uz.cluster.entity.references.model.*;
import uz.cluster.enums.Status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class EmployeeDao extends BaseDao{

    private String name;

    private int positionId;

    private Position position;

    private Direction direction;

    private double perHourWageAmount;

    List<PercentageDao> percentages = new ArrayList<>();

    private Status status;

    public Employee copy(EmployeeDao employeeDao){
        Employee employee = new Employee();
        employee.setId((int) employeeDao.getId());
        employee.setName(employeeDao.getName());
        employee.setPosition(employeeDao.getPosition());
        employee.setDirection(employeeDao.getDirection());
        employee.setPositionId(employeeDao.getPositionId());
        employee.setPerHourWageAmount(employeeDao.getPerHourWageAmount());
        employee.setStatus(employeeDao.getStatus());
        for (PercentageDao percentageDao : employeeDao.getPercentages()){
            employee.getPercentages().add(percentageDao.copy(percentageDao));
        }
        return employee;
    }

}

