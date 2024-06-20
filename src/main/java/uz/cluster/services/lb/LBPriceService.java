package uz.cluster.services.lb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.lb.LBPriceDao;
import uz.cluster.dao.purchase.PriceDao;
import uz.cluster.entity.lb.LBPrice;
import uz.cluster.entity.purchase.Price;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.lb.LBPriceRepository;
import uz.cluster.repository.purchase.PriceRepository;
import uz.cluster.repository.references.ProductTypeRepository;
import uz.cluster.util.LanguageManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LBPriceService {

    private final LBPriceRepository priceRepository;

    private final ProductTypeRepository productTypeRepository;

    @CheckPermission(form = FormEnum.PRICE, permission = Action.CAN_VIEW)
    public List<LBPriceDao> getPriceList() {
        return priceRepository.findAll().stream().map(LBPrice::asDao).collect(Collectors.toList());
    }

    public LBPriceDao getById(int id) {
        Optional<LBPrice> optionalPrice = priceRepository.findById(id);
        if (optionalPrice.isEmpty()) {
            log.error("Bu Id " + id + " O'chirilmadi !");
            return null;
        } else {
            log.info("Bu Id " + id + " O'chirildi ! :)");
            return optionalPrice.get().asDao();
        }
    }

    public LBPrice getPriceById(int id) {
        Optional<LBPrice> optionalPrice = priceRepository.findByProductType_Id(id);
        if (optionalPrice.isEmpty()) {
            log.error("Bu Id " + id + " Xarajat topilmadi !");
            return null;
        } else {
            log.info("Bu id " + id + " xarajadlar chiqarildi !");
            return optionalPrice.get();
        }
    }

    @CheckPermission(form = FormEnum.PRICE, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(LBPriceDao priceDao) {
        LBPrice price = priceDao.copy(priceDao);

        if (price.getProductTypeId() == 0) {
            log.error("Ma;lumot qo'shib bo'lmadi ! :(");
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }

        Optional<ProductType> optionalProductType = productTypeRepository.findById(price.getProductTypeId());
        optionalProductType.ifPresent(price::setProductType);

        if (price.getId() != 0) {
            log.info("Ma'lumot yangilandi ");
            return edit(price);
        }

        Optional<LBPrice> optionalPrice = priceRepository.findByProductType_Id(price.getProductTypeId());
        if (optionalPrice.isPresent()){
            log.error("Malumot allaqachon yaratilgan !");
            return new ApiResponse(false, LanguageManager.getLangMessage("already_created"));
        }

        LBPrice priceSaved = priceRepository.save(price);
        log.info("Ma'lumot saqlandi !");
        return new ApiResponse(true, priceSaved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.PRICE, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(LBPrice price) {
        Optional<LBPrice> optionalPrice = priceRepository.findById(price.getId());
        if (optionalPrice.isPresent()){
            LBPrice priceEdited = priceRepository.save(price);
            log.info("Narx saqlandi");
            return new ApiResponse(true, priceEdited, LanguageManager.getLangMessage("edited"));
        }else{
            log.error("Narxni saqlab bo'lmadi");
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
