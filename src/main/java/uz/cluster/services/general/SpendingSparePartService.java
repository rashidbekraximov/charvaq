package uz.cluster.services.general;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.dao.general.SpendingSparePartDao;
import uz.cluster.entity.general.SpendingSparePart;
import uz.cluster.entity.general.Warehouse;
import uz.cluster.entity.logistic.Technician;
import uz.cluster.entity.references.model.SparePartType;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.general.SpendingSparePartRepository;
import uz.cluster.repository.general.WarehouseRepository;
import uz.cluster.repository.logistic.TechnicianRepository;
import uz.cluster.repository.references.SparePartTypeRepository;
import uz.cluster.util.LanguageManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpendingSparePartService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);

    public final SpendingSparePartRepository spendingSparePartRepository;

    public final SparePartTypeRepository sparePartTypeRepository;

    public final TechnicianRepository technicianRepository;

    public final WarehouseRepository warehouseRepository;


    public List<SpendingSparePartDao> getList(){
        return spendingSparePartRepository.findAll().stream().map(SpendingSparePart::asDao).collect(Collectors.toList());
    }

    public SpendingSparePartDao getById(long id){
        Optional<SpendingSparePart> optionalSpendingSparePart = spendingSparePartRepository.findById(id);
        if (optionalSpendingSparePart.isPresent()){
            logger.info("Bu ID => " + id + " bo'yicha ehtiyot qism ombordan olindi :)");
            return optionalSpendingSparePart.get().asDao();
        }else{
            logger.error("Bu ID => " + id + " bo'yicha ehtiyot qism ombordan topilmadi :( ");
        }
        return null;
    }

    public ApiResponse delete(long id) {
        Optional<SpendingSparePart> optionalSpendingSparePart = spendingSparePartRepository.findById(id);
        if (optionalSpendingSparePart.isPresent()){
            optionalSpendingSparePart.get().setQty(0);
            optionalSpendingSparePart.get().setValue(0);
            SpendingSparePart remainderPassive = spendingSparePartRepository.save(optionalSpendingSparePart.get());
            logger.info("Omborda ma'lumot tozalandi !");
            return new ApiResponse(true, remainderPassive, LanguageManager.getLangMessage("deleted"));
        }else{
            logger.error("Ombor bo'shatilmadi !");
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }


    @Transactional
    public ApiResponse add(SpendingSparePartDao spendingSparePartDao) {
        SpendingSparePart spendingSparePart = spendingSparePartDao.asDao(spendingSparePartDao);

        if (spendingSparePart.getSparePartTypeId() == 0 || spendingSparePart.getTechnicianId() == 0) {
            logger.error("Ehtiyot qism turi yoki texnikachi tanlanmadi !");
            return new ApiResponse(false, "Ehtiyot qism turi yoki texnikachi tanlanmadi !");
        }

        Optional<SparePartType> optionalSparePartType = sparePartTypeRepository.findById(spendingSparePart.getSparePartTypeId());
        optionalSparePartType.ifPresent(spendingSparePart::setSparePartType);

        Optional<Technician> technician = technicianRepository.findById(spendingSparePart.getSparePartTypeId());
        technician.ifPresent(spendingSparePart::setTechnician);

        Optional<Warehouse> optionalWarehouse = warehouseRepository.findBySparePartType_Id(spendingSparePart.getSparePartTypeId());
        if (optionalWarehouse.isPresent()){
            Warehouse warehouse = optionalWarehouse.get();
            warehouse.setQty(warehouse.getQty() - spendingSparePart.getQty());
            warehouse.setValue(warehouse.getValue() - spendingSparePart.getValue());
            spendingSparePartRepository.save(spendingSparePart);
            logger.info("Ehtiyot qism ombordagi mavjud " + spendingSparePart.getQty() + " mahsulotdan ayirildi !");
            return new ApiResponse(true, warehouse, LanguageManager.getLangMessage("saved"));
        }else {
            logger.error("Ehtiyot qism omborda topilmadi :( !");
            return new ApiResponse(false, null, "Ehtiyot qism omborda topilmadi :( !..");
        }
    }



}
