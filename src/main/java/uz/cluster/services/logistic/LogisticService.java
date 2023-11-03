package uz.cluster.services.logistic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import uz.cluster.dao.salary.SalaryDao;
import uz.cluster.dao.logistic.DashboardLogistic;
import uz.cluster.dao.logistic.DashboardTechnician;
import uz.cluster.entity.forms.Salary;
import uz.cluster.entity.logistic.BringDrobilkaProduct;
import uz.cluster.entity.logistic.Logistic;
import uz.cluster.entity.logistic.Technician;
import uz.cluster.entity.references.model.Percentage;
import uz.cluster.enums.CostEnum;
import uz.cluster.enums.forms.DirectionEnum;
import uz.cluster.repository.logistic.BringDrobilkaProductRepository;
import uz.cluster.repository.logistic.LogisticDao;
import uz.cluster.repository.logistic.LogisticRepository;
import uz.cluster.repository.purchase.PurchaseRepository;
import uz.cluster.repository.references.PercentageRepository;
import uz.cluster.repository.logistic.TechnicianRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class LogisticService {

    private final LogisticRepository logisticRepository;

    private final PercentageRepository percentageRepository;

    private final TechnicianRepository technicianRepository;

    private final BringDrobilkaProductRepository bringDrobilkaProductRepository;

    private final PurchaseRepository purchaseRepository;

    public void createBringProductLogisticCost(BringDrobilkaProduct bringDrobilkaProduct){
        List<Logistic> logistics = new ArrayList<>();
        double km = bringDrobilkaProduct.getKm();
        Logistic logistic1 = new Logistic();
        logistic1.setDocumentId(bringDrobilkaProduct.getId());
        logistic1.setCostId(CostEnum.GAS.getValue());
        logistic1.setDate(bringDrobilkaProduct.getDate());
        logistic1.setAmount(bringDrobilkaProduct.getTechnician().getPerKmGasCost() * km);
        logistic1.setTechnician(bringDrobilkaProduct.getTechnician());
        logistics.add(logistic1);
        Logistic logistic2 = new Logistic();
        logistic2.setDocumentId(bringDrobilkaProduct.getId());
        logistic2.setCostId(CostEnum.BALLON.getValue());
        logistic2.setDate(bringDrobilkaProduct.getDate());
        logistic2.setAmount(bringDrobilkaProduct.getTechnician().getPerKmBallonCost() * km);
        logistic2.setTechnician(bringDrobilkaProduct.getTechnician());
        logistics.add(logistic2);
        Logistic logistic3 = new Logistic();
        logistic3.setDocumentId(bringDrobilkaProduct.getId());
        logistic3.setCostId(CostEnum.OIL.getValue());
        logistic3.setDate(bringDrobilkaProduct.getDate());
        logistic3.setAmount(bringDrobilkaProduct.getTechnician().getPerKmOilCost() * km);
        logistic3.setTechnician(bringDrobilkaProduct.getTechnician());
        logistics.add(logistic3);
        Logistic logistic4 = new Logistic();
        logistic4.setDocumentId(bringDrobilkaProduct.getId());
        logistic4.setCostId(CostEnum.AMORTIZATION.getValue());
        logistic4.setDate(bringDrobilkaProduct.getDate());
        logistic4.setAmount(bringDrobilkaProduct.getTechnician().getPerKmAmortization() * km);
        logistic4.setTechnician(bringDrobilkaProduct.getTechnician());
        logistics.add(logistic4);
        logisticRepository.saveAll(logistics);
    }

    public void deleteAllById(int id){
        logisticRepository.deleteAllByDocumentId(id);
    }

    public void createSalaryLogisticCost(SalaryDao salaryDao, Salary salary){
        Logistic logistic = new Logistic();
        logistic.setDocumentId((int) salary.getId());
        logistic.setCostId(CostEnum.SALARY.getValue());
        logistic.setDate(salaryDao.getDate());
        double salaryAmount = salary.getAllAmount();
        for (Percentage percentage : percentageRepository.findAllByEmployeeId(salary.getEmployee().getId())){
            if (percentage.getDirectionId() == DirectionEnum.LOGISTIC.getValue()){
                logistic.setAmount(salaryAmount * (percentage.getPercentage() / 100));
            }
        }
        Optional<Technician> optionalTechnician = technicianRepository.findByEmployee_Id(salary.getEmployee().getId());
        if (optionalTechnician.isPresent()){
            logistic.setTechnician(optionalTechnician.get());
        }else{
            throw new NotFoundException("Texnika biriktirilmagan");
        }
        logisticRepository.save(logistic);
    }

    public List<DashboardLogistic> getTotalAllCost(){
        List<DashboardLogistic> dashboardLogistics = new ArrayList<>();
        List<LogisticDao> list = logisticRepository.getAllByCost();
        double totalCostAmount = 0;

        for (LogisticDao logistic : list){
            DashboardLogistic dashboardLogistic = new DashboardLogistic();
            dashboardLogistic.setCostId(logistic.getCostId());
            dashboardLogistic.setAmount(logistic.getAmount());
            totalCostAmount += logistic.getAmount();
            dashboardLogistics.add(dashboardLogistic);
        }
        DashboardLogistic dashboardLogisticTotal = new DashboardLogistic();
        dashboardLogisticTotal.setCostId(CostEnum.ALL_COST.getValue());
        dashboardLogisticTotal.setAmount(totalCostAmount);
        dashboardLogistics.add(dashboardLogisticTotal);

        DashboardLogistic dashboardLogisticTotalIncome = new DashboardLogistic();
        List<Double> allIncome = bringDrobilkaProductRepository.getAllIncome();
        dashboardLogisticTotalIncome.setCostId(CostEnum.ALL_INCOME.getValue());
        allIncome.forEach(dashboardLogisticTotalIncome::setAmount);
        List<Double> lastIncome = purchaseRepository.getAllIncome();
        lastIncome.forEach(last -> {
            dashboardLogisticTotalIncome.setAmount(dashboardLogisticTotalIncome.getAmount() + last);
        });
        dashboardLogistics.add(dashboardLogisticTotalIncome);

        DashboardLogistic dashboardLogisticTotalProfit = new DashboardLogistic();
        dashboardLogisticTotalProfit.setCostId(CostEnum.ALL_PROFIT.getValue());
        dashboardLogisticTotalProfit.setAmount(dashboardLogisticTotalIncome.getAmount() - dashboardLogisticTotal.getAmount());
        dashboardLogistics.add(dashboardLogisticTotalProfit);

        return IntStream.range(0, dashboardLogistics.size()).map(i -> dashboardLogistics.size() - 1-i).mapToObj(dashboardLogistics::get).collect(Collectors.toList());
    }

    public List<DashboardTechnician> getTechnicianList(){
        List<DashboardTechnician> dashboardTechnicians = new ArrayList<>();

        for (Technician technician : technicianRepository.findAll()){
            DashboardTechnician dashboardTechnician = new DashboardTechnician();
            dashboardTechnician.setId(technician.getId());
            dashboardTechnician.setName(technician.getEmployee().getName());
            dashboardTechnician.setTechnique(technician.getTechniqueType().getName().getActiveLanguage());
            double totalAmount = 0;
            for (LogisticDao dao : logisticRepository.getAllByTechnicianId(technician.getId())){
                DashboardLogistic dashboardLogistic = new DashboardLogistic();
                dashboardLogistic.setCostId(dao.getCostId());
                dashboardLogistic.setAmount(round(dao.getAmount()));
                totalAmount += dao.getAmount();
                dashboardTechnician.getDashboardLogistics().add(dashboardLogistic);
            }
            DashboardLogistic dashboardLogistic = new DashboardLogistic();
            dashboardLogistic.setCostId(CostEnum.ALL_COST.getValue());
            dashboardLogistic.setAmount(round(totalAmount));
            dashboardTechnician.getDashboardLogistics().add(dashboardLogistic);
            DashboardLogistic dashboardLogisticIncome = new DashboardLogistic();
            List<Double> allIncome = bringDrobilkaProductRepository.getAllIncomeTechnicianId(technician.getId());
            dashboardLogisticIncome.setCostId(CostEnum.ALL_INCOME.getValue());
            allIncome.forEach(dashboardLogisticIncome::setAmount);
            List<Double> lastIncome = purchaseRepository.getAllIncomeTechnicianId(technician.getId());
            lastIncome.forEach(last -> {
                dashboardLogisticIncome.setAmount(round(dashboardLogisticIncome.getAmount() + last));
            });
            dashboardTechnician.getDashboardLogistics().add(dashboardLogisticIncome);
            DashboardLogistic dashboardLogisticProfit = new DashboardLogistic();
            dashboardLogisticProfit.setCostId(CostEnum.ALL_PROFIT.getValue());
            dashboardLogisticProfit.setAmount(round(dashboardLogisticIncome.getAmount() - totalAmount));
            dashboardTechnician.getDashboardLogistics().add(dashboardLogisticProfit);
            dashboardTechnicians.add(dashboardTechnician);
        }

        return dashboardTechnicians;
    }

    public double round(double value){
        return Math.round((value * 100.0))/100.0;
    }
}
