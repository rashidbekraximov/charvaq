package uz.cluster.services.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.purchase.RemainderDao;
import uz.cluster.entity.logistic.BringDrobilkaProduct;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.purchase.Remainder;
import uz.cluster.entity.references.model.Unit;
import uz.cluster.enums.MCHJ;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.references.ProductTypeRepository;
import uz.cluster.repository.purchase.RemainderRepository;
import uz.cluster.repository.references.UnitRepository;
import uz.cluster.util.LanguageManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RemainderService {

    private final RemainderRepository remainderRepository;

    private final ProductTypeRepository productTypeRepository;

    private final UnitRepository unitRepository;

    @CheckPermission(form = FormEnum.REMAINDER, permission = Action.CAN_VIEW)
    public List<RemainderDao> getRemainderList(MCHJ mchj) {
        return remainderRepository.findAllByMchj(mchj).stream().map(Remainder::asDao).collect(Collectors.toList());
    }

    public RemainderDao getById(int id) {
        Optional<Remainder> optionalRemainder = remainderRepository.findById(id);
        if (optionalRemainder.isEmpty()) {
            return null;
        } else {
            return optionalRemainder.get().asDao();
        }
    }

    @CheckPermission(form = FormEnum.REMAINDER, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(RemainderDao remainderDao) {
        Remainder remainder = remainderDao.copy(remainderDao);

        if (remainder.getProductTypeId() == 0 || remainder.getUnitId() == 0) {
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }

        Optional<ProductType> optionalProductType = productTypeRepository.findById(remainder.getProductTypeId());
        optionalProductType.ifPresent(remainder::setProductType);

        Optional<Unit> optionalUnit = unitRepository.findById(remainder.getUnitId());
        optionalUnit.ifPresent(remainder::setUnit);

        if (remainder.getId() != 0) {
            return edit(remainder);
        }

        Optional<Remainder> optionalRemainder = remainderRepository.findByProductType_IdAndMchj(remainder.getProductTypeId(),remainder.getMchj());
        if (optionalRemainder.isPresent()){
            return new ApiResponse(false, LanguageManager.getLangMessage("already_created"));
        }

        Remainder remainderSaved = remainderRepository.save(remainder);
        return new ApiResponse(true, remainderSaved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.REMAINDER, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(Remainder remainder) {
        Optional<Remainder> optionalRemainder = remainderRepository.findById(remainder.getId());
        if (optionalRemainder.isPresent()){
            Remainder remainderEdited = remainderRepository.save(optionalRemainder.get());
            return new ApiResponse(true, remainderEdited, LanguageManager.getLangMessage("edited"));
        }else{
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.REMAINDER, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(int id) {
        Optional<Remainder> optionalRemainder = remainderRepository.findById(id);
        if (optionalRemainder.isPresent()){
            Remainder remainderPassive = remainderRepository.save(optionalRemainder.get());
            return new ApiResponse(true, remainderPassive, LanguageManager.getLangMessage("deleted"));
        }else{
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

    public void addByBringing(BringDrobilkaProduct bringDrobilkaProduct) {
        Optional<Remainder> optionalRemainder = remainderRepository.findByProductType_IdAndMchj(bringDrobilkaProduct.getProductType().getId(),bringDrobilkaProduct.getMchj());
        if(optionalRemainder.isPresent()){
            optionalRemainder.get().setAmount(optionalRemainder.get().getAmount() + bringDrobilkaProduct.getAmount());
            remainderRepository.save(optionalRemainder.get());
        }else{
            Remainder remainder = new Remainder();
            remainder.setProductType(bringDrobilkaProduct.getProductType());
            remainder.setUnit(bringDrobilkaProduct.getUnit());
            remainder.setAmount(bringDrobilkaProduct.getAmount());
            remainder.setMchj(bringDrobilkaProduct.getMchj());
            remainderRepository.save(remainder);
        }
    }

    public boolean purchase(int productId,double amount){
        Optional<Remainder> optionalRemainder = remainderRepository.findByProductType_IdAndMchj(productId,MCHJ.CHSM);
        if (optionalRemainder.isPresent() && optionalRemainder.get().getAmount() >= amount){
            optionalRemainder.get().setAmount(optionalRemainder.get().getAmount() - amount);
            remainderRepository.save(optionalRemainder.get());
            return false;
        }else{
            return true;
        }
    }

    public void enter(int productId,double amount){
        Optional<Remainder> optionalRemainder = remainderRepository.findByProductType_IdAndMchj(productId,MCHJ.CHSM);
        if (optionalRemainder.isPresent()){
            optionalRemainder.get().setAmount(optionalRemainder.get().getAmount() + amount);
            remainderRepository.save(optionalRemainder.get());
        }else{
            Remainder remainder  = new Remainder();
            Optional<ProductType> optionalProductType = productTypeRepository.findById(productId);
            optionalProductType.ifPresent(remainder::setProductType);
            remainder.setUnitId(uz.cluster.enums.Unit.DONA.getValue());
            remainder.setAmount(amount);
        }
    }


}
