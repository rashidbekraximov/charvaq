package uz.cluster.services.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.purchase.PriceDao;
import uz.cluster.entity.purchase.Price;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.references.model.Unit;
import uz.cluster.enums.Status;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.purchase.PriceRepository;
import uz.cluster.repository.references.ProductTypeRepository;
import uz.cluster.repository.references.UnitRepository;
import uz.cluster.util.LanguageManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository priceRepository;

    private final ProductTypeRepository productTypeRepository;

    @CheckPermission(form = FormEnum.PRICE, permission = Action.CAN_VIEW)
    public List<PriceDao> getPriceList() {
        return priceRepository.findAll().stream().map(Price::asDao).collect(Collectors.toList());
    }

    public PriceDao getById(int id) {
        Optional<Price> optionalPrice = priceRepository.findById(id);
        if (optionalPrice.isEmpty()) {
            return null;
        } else {
            return optionalPrice.get().asDao();
        }
    }

    public double getPriceById(int id) {
        Optional<Price> optionalPrice = priceRepository.findByProductType_Id(id);
        if (optionalPrice.isEmpty()) {
            return 0;
        } else {
            return optionalPrice.get().getPrice();
        }
    }

    @CheckPermission(form = FormEnum.PRICE, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(PriceDao priceDao) {
        Price price = priceDao.copy(priceDao);

        if (price.getProductTypeId() == 0) {
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }

        Optional<ProductType> optionalProductType = productTypeRepository.findById(price.getProductTypeId());
        optionalProductType.ifPresent(price::setProductType);

        if (price.getId() != 0) {
            return edit(price);
        }

        Optional<Price> optionalPrice = priceRepository.findByProductType_Id(price.getProductTypeId());
        if (optionalPrice.isPresent()){
            return new ApiResponse(false, LanguageManager.getLangMessage("already_created"));
        }

        Price priceSaved = priceRepository.save(price);
        return new ApiResponse(true, priceSaved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.PRICE, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(Price price) {
        Optional<Price> optionalPrice = priceRepository.findById(price.getId());
        if (optionalPrice.isPresent()){
            Price priceEdited = priceRepository.save(price);
            return new ApiResponse(true, priceEdited, LanguageManager.getLangMessage("edited"));
        }else{
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

}
