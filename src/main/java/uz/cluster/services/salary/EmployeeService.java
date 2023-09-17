package uz.cluster.services.salary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.reference.EmployeeDao;
import uz.cluster.entity.references.model.*;
import uz.cluster.enums.Status;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.references.*;
import uz.cluster.types.HtmlOption;
import uz.cluster.util.LanguageManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final PositionRepository positionRepository;

    private final DirectionRepository directionRepository;

    private final PercentageRepository percentageRepository;

    @CheckPermission(form = FormEnum.EMPLOYEE, permission = Action.CAN_VIEW)
    public List<EmployeeDao> getEmployeeList() {
        return employeeRepository.findAll().stream().map(Employee::asDao).collect(Collectors.toList());
    }

    public EmployeeDao getById(int id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isEmpty()) {
            return null;
        } else {
            optionalEmployee.get().setPercentages(percentageRepository.findAllByEmployeeId(id));
            return optionalEmployee.get().asDao();
        }
    }

    public Map<Integer,String> getEmployeeForSelect(){
        Map<Integer,String> htmlOption = new HashMap<>();
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            htmlOption.put(employee.getId(), employee.getName());
        }
        return htmlOption;
    }


    @CheckPermission(form = FormEnum.EMPLOYEE, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(EmployeeDao employeeDao) {
        Employee employee = employeeDao.copy(employeeDao);

        if (employee.getPositionId() == 0) {
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }
        Optional<Position> optionalPosition = positionRepository.findById(employee.getPositionId());
        optionalPosition.ifPresent(employee::setPosition);


        int directionId = 0;
        for (Percentage percentage : employee.getPercentages()){
            if (employee.getPercentages().size() == 1){
                directionId = employee.getPercentages().get(0).getDirectionId();
            } else {
               directionId = 5;
            }
        }
        Optional<Direction> optionalDirection = directionRepository.findById(directionId);
        optionalDirection.ifPresent(employee::setDirection);

        if (employee.getId() != 0) {
            return edit(employee);
        }


        Employee employeeSaved = employeeRepository.save(employee);
        for (Percentage percentage : employee.getPercentages()){
            percentage.setEmployeeId(employeeSaved.getId());
        }
        percentageRepository.saveAll(employee.getPercentages());
        return new ApiResponse(true, employeeSaved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.EMPLOYEE, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(Employee employee) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());
        if (optionalEmployee.isPresent()){
            Employee employeeEdited = employeeRepository.save(employee);
            percentageRepository.deleteAllByEmployeeId(employeeEdited.getId());
            Employee employeeSaved = employeeRepository.save(employee);
            for (Percentage percentage : employee.getPercentages()){
                percentage.setEmployeeId(employeeSaved.getId());
            }
            percentageRepository.saveAll(employee.getPercentages());
            return new ApiResponse(true, employeeEdited, LanguageManager.getLangMessage("edited"));
        }else{
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.EMPLOYEE, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(int id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()){
            optionalEmployee.get().setStatus(Status.PASSIVE);
            Employee employeePassive = employeeRepository.save(optionalEmployee.get());
            percentageRepository.deleteAllByEmployeeId(id);
            return new ApiResponse(true, employeePassive, LanguageManager.getLangMessage("deleted"));
        }else{
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }
}