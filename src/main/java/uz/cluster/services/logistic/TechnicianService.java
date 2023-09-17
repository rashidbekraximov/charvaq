package uz.cluster.services.logistic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.reference.EmployeeDao;
import uz.cluster.dao.logistic.TechnicianDao;
import uz.cluster.entity.logistic.Technician;
import uz.cluster.entity.references.model.*;
import uz.cluster.enums.Status;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.logistic.TechnicianRepository;
import uz.cluster.repository.references.*;
import uz.cluster.services.salary.EmployeeService;
import uz.cluster.util.LanguageManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TechnicianService {

    private final TechnicianRepository technicianRepository;

    private final EmployeeService employeeService;

    private final DirectionRepository directionRepository;

    private final TechniqueTypeRepository techniqueTypeRepository;

    @CheckPermission(form = FormEnum.TECHNICIAN, permission = Action.CAN_VIEW)
    public List<TechnicianDao> getTechnicianList() {
        return technicianRepository.findAll().stream().map(Technician::asDao).collect(Collectors.toList());
    }

    public TechnicianDao getById(int id) {
        Optional<Technician> optionalTechnician = technicianRepository.findById(id);
        if (optionalTechnician.isEmpty()) {
            return null;
        } else {
            return optionalTechnician.get().asDao();
        }
    }

    public Map<Integer,String> getTechnicianForSelect(){
        Map<Integer,String> htmlOption = new HashMap<>();
        List<Technician> technicians = technicianRepository.findAll();
        for (Technician technician : technicians) {
            htmlOption.put(technician.getId(), technician.getTechniqueType().getName().getActiveLanguage());
        }
        return htmlOption;
    }


    @CheckPermission(form = FormEnum.TECHNICIAN, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(TechnicianDao technicianDao) {
        Technician technician = technicianDao.copy(technicianDao);

        if (technician.getTechniqueTypeId() == 0 || technician.getDirectionId() == 0 || technician.getEmployeeId() == 0) {
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }

        Optional<TechniqueType> optionalTechniqueType = techniqueTypeRepository.findById(technician.getTechniqueTypeId());
        optionalTechniqueType.ifPresent(technician::setTechniqueType);

        Optional<Direction> optionalDirection = directionRepository.findById(technician.getDirectionId());
        optionalDirection.ifPresent(technician::setDirection);

        EmployeeDao employeeDao = employeeService.getById(technician.getEmployeeId());
        if (employeeDao != null){
            Employee employee = employeeDao.copy(employeeDao);
            technician.setEmployee(employee);
        }else{
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }

        if (technician.getId() != 0) {
            return edit(technician);
        }

        Technician technicianSaved = technicianRepository.save(technician);
        return new ApiResponse(true, technicianSaved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.TECHNICIAN, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(Technician technician) {
        Optional<Technician> optionalTechnician = technicianRepository.findById(technician.getId());
        if (optionalTechnician.isPresent()){
            Technician technicianEdited = technicianRepository.save(technician);
            return new ApiResponse(true, technicianEdited, LanguageManager.getLangMessage("edited"));
        }else{
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.TECHNICIAN, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(int id) {
        Optional<Technician> optionalTechnician = technicianRepository.findById(id);
        if (optionalTechnician.isPresent()){
            optionalTechnician.get().setStatus(Status.PASSIVE);
            Technician technicianPassive = technicianRepository.save(optionalTechnician.get());
            return new ApiResponse(true, technicianPassive, LanguageManager.getLangMessage("deleted"));
        }else{
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
