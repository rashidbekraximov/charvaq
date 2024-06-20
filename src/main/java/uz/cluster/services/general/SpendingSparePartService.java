package uz.cluster.services.general;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.general.SpendingSparePartDao;
import uz.cluster.entity.general.SpendingSparePart;
import uz.cluster.entity.general.Warehouse;
import uz.cluster.entity.logistic.Technician;
import uz.cluster.entity.references.model.FuelType;
import uz.cluster.entity.references.model.SparePartType;
import uz.cluster.enums.ItemEnum;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.general.SpendingSparePartRepository;
import uz.cluster.repository.general.WarehouseRepository;
import uz.cluster.repository.logistic.TechnicianRepository;
import uz.cluster.repository.references.FuelTypeRepository;
import uz.cluster.repository.references.SparePartTypeRepository;
import uz.cluster.util.LanguageManager;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpendingSparePartService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);

    public final SpendingSparePartRepository spendingSparePartRepository;

    public final SparePartTypeRepository sparePartTypeRepository;

    public final FuelTypeRepository fuelTypeRepository;

    public final TechnicianRepository technicianRepository;

    public final WarehouseRepository warehouseRepository;

    @CheckPermission(form = FormEnum.SPEND_FROM_WAREHOUSE, permission = Action.CAN_VIEW)
    public List<SpendingSparePartDao> getList(int id, Date beginDate, Date endDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String beginDateText = (beginDate != null) ? sdf.format(beginDate) : null;
        String endDateText = (endDate != null) ? sdf.format(endDate) : null;
        if (beginDateText != null && endDateText != null){
            return spendingSparePartRepository.findByParams(id,beginDateText,endDateText).stream().map(SpendingSparePart::asDao).collect(Collectors.toList());
        }else {
            return spendingSparePartRepository.findAllByTechnician_Id(id).stream().map(SpendingSparePart::asDao).collect(Collectors.toList());
        }
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


    @CheckPermission(form = FormEnum.SPEND_FROM_WAREHOUSE, permission = Action.CAN_DELETE)
    public ApiResponse delete(long id) {
        Optional<SpendingSparePart> optionalSpendingSparePart = spendingSparePartRepository.findById(id);
        if (optionalSpendingSparePart.isPresent()){
            if (optionalSpendingSparePart.get().getItem() == ItemEnum.SPARE_PART){
                Optional<Warehouse> optionalWarehouse = warehouseRepository.findBySparePartType_IdAndPrice(optionalSpendingSparePart.get().getSparePartType().getId(),optionalSpendingSparePart.get().getPrice());
                if (optionalWarehouse.isPresent()){
                    optionalWarehouse.get().setQty(optionalSpendingSparePart.get().getQty() + optionalWarehouse.get().getQty());
                    warehouseRepository.save(optionalWarehouse.get());
                }
            }else {
                Optional<Warehouse> optionalWarehouse = warehouseRepository.findByFuelType_IdAndPrice(optionalSpendingSparePart.get().getFuelType().getId(),optionalSpendingSparePart.get().getPrice());
                if (optionalWarehouse.isPresent()){
                    optionalWarehouse.get().setQty(optionalSpendingSparePart.get().getQty() + optionalWarehouse.get().getQty());
                    warehouseRepository.save(optionalWarehouse.get());
                }
            }
            spendingSparePartRepository.deleteById(id);
            logger.info("Omborga sarflangan mahsulot qaytarildi !");
            return new ApiResponse(true, LanguageManager.getLangMessage("deleted"));
        }else{
            logger.error("Omborga sarflangan mahsulotni qaytarishda xatolik yuzaga keldi !");
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }


    @CheckPermission(form = FormEnum.SPEND_FROM_WAREHOUSE, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(SpendingSparePartDao spendingSparePartDao) {
        SpendingSparePart spendingSparePart = spendingSparePartDao.asDao(spendingSparePartDao);

        if (spendingSparePart.getSparePartTypeId() == 0 || spendingSparePart.getTechnicianId() == 0) {
            logger.error("Ehtiyot qism turi yoki texnikachi tanlanmadi !");
            return new ApiResponse(false, "Ehtiyot qism turi yoki texnikachi tanlanmadi !");
        }

        Optional<SparePartType> optionalSparePartType = sparePartTypeRepository.findById(spendingSparePart.getSparePartTypeId());
        optionalSparePartType.ifPresent(spendingSparePart::setSparePartType);

        Optional<FuelType> optionalFuelType = fuelTypeRepository.findById(spendingSparePart.getFuelTypeId());
        optionalFuelType.ifPresent(spendingSparePart::setFuelType);

        Optional<Technician> technician = technicianRepository.findById(spendingSparePart.getSparePartTypeId());
        technician.ifPresent(spendingSparePart::setTechnician);

        if (spendingSparePart.getItem() == ItemEnum.SPARE_PART){
            Optional<Warehouse> optionalWarehouse = warehouseRepository.findBySparePartType_IdAndPrice(spendingSparePart.getSparePartTypeId(),spendingSparePart.getPrice());
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
        }else {
            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByFuelType_IdAndPrice(spendingSparePart.getFuelTypeId(),spendingSparePart.getPrice());
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
}
