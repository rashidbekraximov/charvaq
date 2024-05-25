package uz.cluster.services.produce;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.produce.ProduceRemainderDao;
import uz.cluster.dao.purchase.RemainderDao;
import uz.cluster.entity.produce.ProduceRemainder;
import uz.cluster.entity.purchase.Remainder;
import uz.cluster.entity.references.model.ProductForProduce;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.references.model.Unit;
import uz.cluster.enums.ManufactureProduct;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.produce.ProduceRemainderRepository;
import uz.cluster.repository.references.ProductForProduceRepository;
import uz.cluster.util.LanguageManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProduceRemainderService {

    private static final Logger logger = LoggerFactory.getLogger(ProduceRemainderService.class);

    private final ProduceRemainderRepository produceRemainderRepository;

    private final ProductForProduceRepository productForProduceRepository;

    @Transactional(readOnly = true)
    public List<ProduceRemainderDao> getProduceRemainderList(){
        return produceRemainderRepository.findAll().stream().map(ProduceRemainder::asDao).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProduceRemainderDao getById(long id){
        Optional<ProduceRemainder> optionalProduceRemainder = produceRemainderRepository.findById(id);
        if (optionalProduceRemainder.isPresent()){
            logger.info(id + " bu ID bo'yicha malumot topildi !");
            return optionalProduceRemainder.get().asDao();
        }
        logger.error(id + " bu ID ga doir malumot topilmadi !!");
        return null;
    }

//    @CheckPermission(form = FormEnum.REMAINDER, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(ProduceRemainderDao produceRemainderDao) {
        ProduceRemainder remainder = produceRemainderDao.copy(produceRemainderDao);

        if (remainder.getProductForProduceId() == 0) {
            logger.error("Ishlab chiqarish uchun mahsulot tanlanmadi !");
            return new ApiResponse(false, "Ishlab chiqarish uchun mahsulot tanlanmadi !");
        }

        Optional<ProductForProduce> optionalProductForProduce = productForProduceRepository.findById(remainder.getProductForProduceId());
        optionalProductForProduce.ifPresent(remainder::setProductForProduce);

        Optional<ProduceRemainder> optionalProduceRemainder = produceRemainderRepository.findByProductForProduce_Id(remainder.getProductForProduceId());
        if (optionalProduceRemainder.isPresent()){
            ProduceRemainder produceRemainder = optionalProduceRemainder.get();
            produceRemainder.setAmount(produceRemainder.getAmount() + remainder.getAmount());
            produceRemainderRepository.save(optionalProduceRemainder.get());
            logger.info("Ishlab chiqarish uchun mahsulot ombordagi mavjud mahsulotga qo'shildi !");
            return new ApiResponse(true, produceRemainder, LanguageManager.getLangMessage("saved"));
        }

        ProduceRemainder remainderSaved = produceRemainderRepository.save(remainder);
        logger.info("Ishlab chiqarish uchun mahsulot omborda yaratildi !");
        return new ApiResponse(true, remainderSaved, LanguageManager.getLangMessage("saved"));
    }

    public void minusICHRemainder(double minusSement){
        Optional<ProduceRemainder> optionalProduceRemainder = produceRemainderRepository.findByProductForProduce_Id(ManufactureProduct.SEMENT.getValue());
        if (optionalProduceRemainder.isPresent()){
            ProduceRemainder produceRemainder = optionalProduceRemainder.get();
            if (produceRemainder.getAmount() - minusSement < 0){
                logger.error("Ishlab chiqarish uchun mahsulot omborda yetarli emas !");
            }else {
                produceRemainder.setAmount(produceRemainder.getAmount() - minusSement);
                logger.info("Ishlab chiqarish uchun mahsulot ombordan " + minusSement + " kg ayrildi !");
                produceRemainderRepository.save(produceRemainder);
            }
        }else{
            logger.error("Bu mahsulotga "  + ManufactureProduct.SEMENT.name() + " doir malumot topilmadi");
        }
    }

    @CheckPermission(form = FormEnum.REMAINDER, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(long id) {
        Optional<ProduceRemainder> optionalRemainder = produceRemainderRepository.findById(id);
        if (optionalRemainder.isPresent()){
            optionalRemainder.get().setAmount(0);
            ProduceRemainder remainderPassive = produceRemainderRepository.save(optionalRemainder.get());
            logger.info("Ishlab chiqarish uchun mahsulot qoldig'i tozalandi !");
            return new ApiResponse(true, remainderPassive, LanguageManager.getLangMessage("deleted"));
        }else{
            logger.error("Ishlab chiqarish uchun mahsulot omborda topilmadi (Bo'shatish uchun) !");
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
