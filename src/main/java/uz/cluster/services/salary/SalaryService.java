package uz.cluster.services.salary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.salary.SalaryDao;
import uz.cluster.dao.salary.Tabel;
import uz.cluster.entity.Document;
import uz.cluster.entity.forms.Salary;
import uz.cluster.entity.references.model.Direction;
import uz.cluster.entity.references.model.Employee;
import uz.cluster.entity.references.model.Position;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.DocumentRepository;
import uz.cluster.repository.forrms.SalaryRepository;
import uz.cluster.repository.references.DirectionRepository;
import uz.cluster.repository.references.EmployeeRepository;
import uz.cluster.repository.references.PositionRepository;
import uz.cluster.services.logistic.LogisticService;
import uz.cluster.util.LanguageManager;

import java.beans.Transient;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalaryService {

    private final SalaryRepository salaryRepository;

    private final DocumentRepository documentRepository;

    private final EmployeeRepository employeeRepository;

    private final DirectionRepository directionRepository;

    private final PositionRepository positionRepository;

    @CheckPermission(form = FormEnum.SALARY, permission = Action.CAN_VIEW)
    public List<SalaryDao> getList() {
        List<Document> documents = documentRepository.findAll();
        List<SalaryDao> list = new ArrayList<>();
        for (Document document : documents) {
            List<Salary> salaries = salaryRepository.findAllByDocumentId(document.getDocumentId());
            list.add(new SalaryDao(document, salaries));
        }
        return list;
    }

    public SalaryDao getById(long id) {
        Optional<Document> document = documentRepository.findById(id);
        List<Salary> salaries = new ArrayList<>();
        if (document.isPresent()){
            salaries = salaryRepository.findAllByDocumentId(document.get().getDocumentId());
            return new SalaryDao(document.get(),salaries);
        }
        return null;
    }

    @CheckPermission(form = FormEnum.TABEL, permission = Action.CAN_VIEW)
    public List<Salary> getTableList(int id) {
        List<Salary> salaries = new ArrayList<>();
        for (Employee employee : employeeRepository.findAllByDirection_Id(id)) {
            Salary salary = new Salary();
            salary.setEmployeeId(employee.getId());
            salary.setPositionId(employee.getPosition().getId());
            salary.setPerHourWageAmount(employee.getPerHourWageAmount());
            salaries.add(salary);
        }
        return salaries;
    }

    @CheckPermission(form = FormEnum.TABEL, permission = Action.CAN_VIEW)
    public List<Tabel> getEmployeeList(Date beginDate, Date endDate,int id) {
        List<Employee> employees = employeeRepository.findAllByDirection_Id(id);
        List<Tabel> tabels = new ArrayList<>();
        for (Employee employee : employees){
            Tabel tabel = new Tabel();
            tabel.setId(employee.getId());
            tabel.setName(employee.getName());
            tabel.setPosition(employee.getPosition());
            tabel.setHourDays(employeeRepository.getAllByDateAndEmployeeId(beginDate, endDate, employee.getId()));
            tabels.add(tabel);
        }
        return tabels;
    }

    @CheckPermission(form = FormEnum.TABEL, permission = Action.CAN_VIEW)
    public List<String> getHeaderList(Date beginDate, Date endDate, int directionId) {
        List<String> strings = new ArrayList<>();
        for (Document document : documentRepository.getAllByDateAndDirectionId(beginDate, endDate, directionId)) {
            strings.add(document.getDate().toString());
        }
        return strings;
    }

    @CheckPermission(form = FormEnum.SALARY, permission = Action.CAN_ADD)
    public ApiResponse add(SalaryDao salaryDao) {
        Optional<Document> optionalDocument = documentRepository.findByDate(salaryDao.getDate());
        if (optionalDocument.isPresent()) {
            return new ApiResponse(false, LanguageManager.getLangMessage("already_created"));
        }
        Document documentNew = new Document();
        Optional<Direction> optionalDirection = directionRepository.findById(salaryDao.getDirectionId());
        optionalDirection.ifPresent(documentNew::setDirection);
        documentNew.setDate(salaryDao.getDate());
        documentNew.setNumbers(salaryDao.getForms().size());
        Document documentSaved = documentRepository.save(documentNew);
        for (Salary salary : salaryDao.getForms()) {
            salary.setDocumentId(documentSaved.getDocumentId());
            Optional<Position> optionalPosition = positionRepository.findById(salary.getPositionId());
            optionalPosition.ifPresent(salary::setPosition);
            Optional<Employee> optionalEmployee = employeeRepository.findById(salary.getEmployeeId());
            optionalEmployee.ifPresent(salary::setEmployee);
            Salary saved = salaryRepository.save(salary);
        }
        return new ApiResponse(true, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.SALARY, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(SalaryDao salaryDao) {
        Optional<Document> document = documentRepository.findById(salaryDao.getDocumentId());
        if (document.isPresent()) {
            Optional<Direction> optionalDirection = directionRepository.findById(salaryDao.getDirectionId());
            optionalDirection.ifPresent(document.get()::setDirection);
            document.get().setDate(salaryDao.getDate());
            document.get().setNumbers(salaryDao.getForms().size());
            documentRepository.save(document.get());
            salaryRepository.deleteAllByDocumentId(document.get().getDocumentId());
            for (Salary salary : salaryDao.getForms()) {
                salary.setDocumentId(document.get().getDocumentId());
                Optional<Position> optionalPosition = positionRepository.findById(salary.getPositionId());
                optionalPosition.ifPresent(salary::setPosition);
                Optional<Employee> optionalEmployee = employeeRepository.findById(salary.getEmployeeId());
                optionalEmployee.ifPresent(salary::setEmployee);
                salaryRepository.save(salary);
            }
        } else {
            return new ApiResponse(true, document, LanguageManager.getLangMessage("cant_find"));
        }
        return new ApiResponse(true, document, LanguageManager.getLangMessage("edited"));
    }

    @CheckPermission(form = FormEnum.SALARY, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(long id) {
        documentRepository.deleteByDocumentId(id);
        salaryRepository.deleteAllByDocumentId(id);
        return new ApiResponse(true, null, LanguageManager.getLangMessage("deleted"));
    }
}
