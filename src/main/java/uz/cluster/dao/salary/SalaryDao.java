package uz.cluster.dao.salary;

import uz.cluster.entity.Document;
import uz.cluster.entity.forms.Salary;

import java.io.Serializable;
import java.util.List;

public class SalaryDao extends Document implements Serializable {

    private List<Salary> forms;

    public List<Salary> getForms() {
        return forms;
    }

    public void setForms(List<Salary> forms) {
        this.forms = forms;
    }

    public SalaryDao(Document document,List<Salary> salaries) {
        super(document);
        this.forms = salaries;
    }
}
