package uz.cluster.services.produce;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.produce.CostDao;
import uz.cluster.dao.produce.ReadyProductDao;
import uz.cluster.entity.produce.ReadyProduct;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.references.model.Unit;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.enums.produce.ProduceEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.produce.ProduceCostDao;
import uz.cluster.repository.produce.ReadyProductRepository;
import uz.cluster.repository.references.ProductTypeRepository;
import uz.cluster.repository.references.UnitRepository;
import uz.cluster.util.LanguageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReadyProductService {

    private final ReadyProductRepository readyProductRepository;

    private final ProductTypeRepository productTypeRepository;

    private final UnitRepository unitRepository;

    private final CostService costService;

    @CheckPermission(form = FormEnum.READY_PRODUCT, permission = Action.CAN_VIEW)
    public List<ReadyProductDao> getList() {
        List<ReadyProductDao> list = new ArrayList<>();
        List<ProduceCostDao> produceCostDaoList = readyProductRepository.getAllByProductTypeId();
        produceCostDaoList.forEach(produce -> {
            ReadyProductDao readyProductDao = new ReadyProductDao();
            Optional<ProductType> optionalProductType = productTypeRepository.findById(produce.getProductTypeId());
            optionalProductType.ifPresent(readyProductDao::setProductType);
            readyProductDao.setAmount(produce.getAmount());
            list.add(readyProductDao);
        });
        return list;
    }

    public ReadyProductDao getById(int id) {
        Optional<ReadyProduct> optionalRemainder = readyProductRepository.findById(id);
        if (optionalRemainder.isEmpty()) {
            return null;
        } else {
            return optionalRemainder.get().asDao();
        }
    }

    @CheckPermission(form = FormEnum.READY_PRODUCT, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(ReadyProductDao remainderDao) {
        ReadyProduct remainder = remainderDao.copy(remainderDao);

        if (remainder.getProductTypeId() == 0 || remainder.getUnitId() == 0) {
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }

        Optional<ProductType> optionalProductType = productTypeRepository.findById(remainder.getProductTypeId());
        optionalProductType.ifPresent(remainder::setProductType);

        Optional<Unit> optionalUnit = unitRepository.findById(remainder.getUnitId());
        optionalUnit.ifPresent(remainder::setUnit);

        remainder.setAllCostAmount(remainder.getCostAmount() * remainder.getAmount());
        readyProductRepository.save(remainder);
        CostDao costDao = new CostDao();
        costDao.setDate(remainder.getDate());
        costDao.setCostTypeId(ProduceEnum.PRODUCE_COST.getValue());
        costDao.setAmount(remainder.getAmount() * remainder.getCostAmount());
        costService.add(costDao);
        ReadyProduct remainderSaved = readyProductRepository.save(remainder);
        return new ApiResponse(true, remainderSaved, LanguageManager.getLangMessage("saved"));
    }
}