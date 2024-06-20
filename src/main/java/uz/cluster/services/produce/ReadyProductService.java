package uz.cluster.services.produce;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.produce.CostDao;
import uz.cluster.dao.produce.ReadyProductDao;
import uz.cluster.entity.produce.ReadyProduct;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.enums.ManufactureProduct;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.enums.produce.ProduceEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.produce.ReadyProductRepository;
import uz.cluster.repository.references.ProductTypeRepository;
import uz.cluster.services.purchase.RemainderService;
import uz.cluster.util.LanguageManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadyProductService {

    private final ReadyProductRepository readyProductRepository;

    private final ProductTypeRepository productTypeRepository;

    private final CostService costService;

    private final RemainderService remainderService;

    private final ProduceRemainderService produceRemainderService;

    @CheckPermission(form = FormEnum.READY_PRODUCT, permission = Action.CAN_VIEW)
    public List<ReadyProductDao> getList() {
        return readyProductRepository.findAllByOrderByDateDesc().stream().map(ReadyProduct::asDao).collect(Collectors.toList());
    }

    public List<String> getListDate(String sex) {
        List<String> dates = new ArrayList<>();
        List<ReadyProduct> list = readyProductRepository.findAllByOrderByDateAsc(sex);
        Collections.reverse(list);
        list.forEach(l -> {
            dates.add(l.getDate().toString());
        });
        return dates;
    }

    public List<Double> getListAmount(String sex) {
        List<Double> dates = new ArrayList<>();
        List<ReadyProduct> list = readyProductRepository.findAllByOrderByDateAsc(sex);
        Collections.reverse(list);
        list.forEach(l -> {
            dates.add(l.getAmount());
        });
        return dates;
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

        if (remainder.getProductTypeId() == 0) {
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }

        Optional<ProductType> optionalProductType = productTypeRepository.findById(remainder.getProductTypeId());
        optionalProductType.ifPresent(remainder::setProductType);

        remainderService.enter(remainder.getProductTypeId(),remainder.getAmount(),remainder.getSexEnum());

        ApiResponse api = produceRemainderService.minusICHRemainder(remainder.getCostPerKgSementAmount()*remainder.getAmount(),remainder.getSexEnum());
        if (!api.isSuccess()){
            return new ApiResponse(false,  "Sement omborda topilmadi");
        }
        Optional<ReadyProduct> readyProduct = readyProductRepository.findByDateAndSexEnum(remainder.getDate(),remainder.getSexEnum());
        if (readyProduct.isPresent()){
            CostDao costDao = new CostDao();
            costDao.setDate(readyProduct.get().getDate());
            costDao.setSpendingTypeId(ProduceEnum.PERMAMENT_COST.getValue());
            costDao.setAmount(remainder.getAmount() * remainder.getCostAmount());
            costDao.setSexEnum(remainder.getSexEnum());
            costService.add(costDao);
            readyProduct.get().setAmount(readyProduct.get().getAmount() + remainder.getAmount());
            readyProduct.get().setAllCostAmount(readyProduct.get().getAllCostAmount() + (remainder.getCostAmount() * remainder.getAmount()));
            ReadyProduct remainderSaved = readyProductRepository.save(readyProduct.get());
            return new ApiResponse(true, remainderSaved, LanguageManager.getLangMessage("saved"));
        }

        remainder.setAllCostAmount(remainder.getCostAmount() * remainder.getAmount());
        readyProductRepository.save(remainder);
        CostDao costDao = new CostDao();
        costDao.setDate(remainder.getDate());
        costDao.setSpendingTypeId(ProduceEnum.PERMAMENT_COST.getValue());
        costDao.setAmount(remainder.getAmount() * remainder.getCostAmount());
        costDao.setSexEnum(remainder.getSexEnum());
        costService.add(costDao);
        ReadyProduct remainderSaved = readyProductRepository.save(remainder);
        return new ApiResponse(true, remainderSaved, LanguageManager.getLangMessage("saved"));
    }
}