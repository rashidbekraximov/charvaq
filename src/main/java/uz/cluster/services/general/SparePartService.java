package uz.cluster.services.general;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.general.SparePartDao;
import uz.cluster.dao.produce.ProduceRemainderDao;
import uz.cluster.entity.general.SparePart;
import uz.cluster.entity.general.Warehouse;
import uz.cluster.entity.produce.ProduceRemainder;
import uz.cluster.entity.references.model.ProductForProduce;
import uz.cluster.entity.references.model.SparePartType;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.general.SparePartRepository;
import uz.cluster.repository.general.WarehouseRepository;
import uz.cluster.repository.references.SparePartTypeRepository;
import uz.cluster.services.produce.ProduceRemainderService;
import uz.cluster.util.LanguageManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SparePartService {

    private static final Logger logger = LoggerFactory.getLogger(SparePartService.class);

    private final SparePartRepository sparePartRepository;

    private final SparePartTypeRepository partTypeRepository;

    private final WarehouseRepository warehouseRepository;

    private final WarehouseService warehouseService;

    public List<SparePartDao> getList(){
        return sparePartRepository.findAllByOrderByDateDesc().stream().map(SparePart::asDao).collect(Collectors.toList());
    }

    public SparePartDao getById(Long id){
        Optional<SparePart> optionalSparePart = sparePartRepository.findById(id);
        if (optionalSparePart.isPresent()){
            logger.info("Bu ID => " + id + " bo'yicha ehtiyot qism olindi :)");
            return optionalSparePart.get().asDao();
        }else{
            logger.error("Bu ID => " + id + " bo'yicha ehtiyot qism topilmadi :( ");
        }
        return null;
    }

    @Transactional
    public ApiResponse add(SparePartDao sparePartDao) {
        SparePart sparePart = sparePartDao.copy(sparePartDao);

        if (sparePart.getSparePartTypeId() == 0) {
            logger.error("Ehtiyot qism turi tanlanmadi !");
            return new ApiResponse(false, "Ehtiyot qism turi tanlanmadi !");
        }

        Optional<SparePartType> optionalSparePartType = partTypeRepository.findById(sparePart.getSparePartTypeId());
        optionalSparePartType.ifPresent(sparePart::setSparePartType);

        Optional<Warehouse> optionalWarehouse = warehouseRepository.findBySparePartType_Id(sparePart.getSparePartTypeId());
        if (optionalWarehouse.isPresent()){
            Warehouse warehouse = optionalWarehouse.get();
            warehouse.setQty(warehouse.getQty() + sparePart.getQty());
            warehouse.setValue(warehouse.getValue() + sparePart.getValue());
            warehouseRepository.save(warehouse);
            logger.info("Ehtiyot qism ombordagi mavjud mahsulotga qo'shildi !");
            return new ApiResponse(true, warehouse, LanguageManager.getLangMessage("saved"));
        }else {
            Warehouse warehouse = new Warehouse();
            warehouse.setSparePartType(sparePart.getSparePartType());
            warehouse.setQty(sparePart.getQty());
            warehouse.setValue(sparePart.getValue());
            warehouse.setValue(sparePart.getPrice());
            warehouseRepository.save(warehouse);
            logger.info("Ehtiyot qism ombordga kirim bo'ldi :) !");
        }

        SparePart sparePartSaved = sparePartRepository.save(sparePart);
        logger.info("Ehtiyot qism muvaffaqiyatli saqlandi !");
        return new ApiResponse(true, sparePartSaved, LanguageManager.getLangMessage("saved"));
    }

//    @CheckPermission(form = FormEnum.REMAINDER, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(long id) {
        Optional<SparePart> optionalRemainder = sparePartRepository.findById(id);
        if (optionalRemainder.isPresent()){
            sparePartRepository.deleteById(id);
            logger.info("Ehtiyot qism ID=>" + id + " tozalandi !");
            return new ApiResponse(true, LanguageManager.getLangMessage("deleted"));
        }else{
            logger.error("Ehtiyot qism topilmadi ID=> " + id + "(O'chirish uchun uchun) !");
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }

}
