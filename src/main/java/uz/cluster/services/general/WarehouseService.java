package uz.cluster.services.general;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.general.WarehouseDao;
import uz.cluster.entity.general.SparePart;
import uz.cluster.entity.general.Warehouse;
import uz.cluster.entity.produce.ProduceRemainder;
import uz.cluster.enums.ItemEnum;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.general.WarehouseRepository;
import uz.cluster.util.LanguageManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);

    public final WarehouseRepository warehouseRepository;

    @CheckPermission(form = FormEnum.WAREHOUSE, permission = Action.CAN_VIEW)
    public List<WarehouseDao> getList(){
        return warehouseRepository.findAll().stream().map(Warehouse::asDao).collect(Collectors.toList());
    }

    public List<WarehouseDao> getItemList(String item){
        return warehouseRepository.findAllByItem(item.equals("FUEL") ? ItemEnum.FUEL : ItemEnum.SPARE_PART).stream().map(Warehouse::asDao).collect(Collectors.toList());
    }

    public List<WarehouseDao> getBySparePartType(String name,int id){
        if (name.equals(ItemEnum.FUEL.name())){
            return warehouseRepository.findAllByItemAndFuelType_Id(ItemEnum.FUEL,id).stream().map(Warehouse::asDao).collect(Collectors.toList());
        }else{
            return warehouseRepository.findAllByItemAndSparePartType_Id(ItemEnum.SPARE_PART,id).stream().map(Warehouse::asDao).collect(Collectors.toList());
        }
    }

    public WarehouseDao getById(long id){
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(id);
        if (optionalWarehouse.isPresent()){
            logger.info("Bu ID => " + id + " bo'yicha ehtiyot qism ombordan olindi :)");
            return optionalWarehouse.get().asDao();
        }else{
            logger.error("Bu ID => " + id + " bo'yicha ehtiyot qism ombordan topilmadi :( ");
        }
        return null;
    }

    @CheckPermission(form = FormEnum.WAREHOUSE, permission = Action.CAN_DELETE)
    public ApiResponse delete(long id) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(id);
        if (optionalWarehouse.isPresent()){
            optionalWarehouse.get().setQty(0);
            optionalWarehouse.get().setValue(0);
            Warehouse remainderPassive = warehouseRepository.save(optionalWarehouse.get());
            logger.info("Omborda ma'lumot tozalandi !");
            return new ApiResponse(true, remainderPassive, LanguageManager.getLangMessage("deleted"));
        }else{
            logger.error("Ombor bo'shatilmadi !");
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
