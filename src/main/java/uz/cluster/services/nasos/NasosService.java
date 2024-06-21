package uz.cluster.services.nasos;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.Na;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.nasos.NasosDao;
import uz.cluster.dao.nasos.NasosData;
import uz.cluster.entity.nasos.Nasos;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.lb.LBPurchaseRepository;
import uz.cluster.repository.nasos.NasosCostRepository;
import uz.cluster.repository.nasos.NasosRepository;
import uz.cluster.repository.purchase.PurchaseRepository;
import uz.cluster.services.general.SparePartService;
import uz.cluster.util.LanguageManager;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class NasosService {

    private final NasosRepository nasosRepository;

    private final NasosCostRepository nasosCostRepository;

    private final LBPurchaseRepository lbPurchaseRepository;

    public List<NasosData> getDataList(String beginDate,String endDate){
        List<NasosData> nasosData = new ArrayList<>();
        List<Double> summAmount = nasosRepository.findAllByBetweenDate(beginDate,endDate);
        List<Double> costAmount = nasosCostRepository.findAllByBetweenDate(beginDate,endDate);
        List<Double> nasosPurchasedAmount = lbPurchaseRepository.findAllByBetweenDate(beginDate,endDate);
        NasosData nasosData1 = new NasosData();
        nasosData1.setId(100);
        nasosData1.setName("Asosiy sotuv");
        nasosPurchasedAmount.forEach(nasosData1::setAmount);
        nasosData.add(nasosData1);

        NasosData nasosData2 = new NasosData();
        nasosData2.setId(200);
        nasosData2.setName("Xizmat ko'rsatish");
        summAmount.forEach(nasosData2::setAmount);
        nasosData.add(nasosData2);

        NasosData nasosData3 = new NasosData();
        nasosData3.setId(300);
        nasosData3.setName("Umumiy xarajat");
        costAmount.forEach(nasosData3::setAmount);
        nasosData.add(nasosData3);

        NasosData nasosData4 = new NasosData();
        nasosData4.setId(400);
        nasosData4.setName("Sof foyda");
        nasosData4.setAmount((nasosData1.getAmount() + nasosData2.getAmount()) - nasosData3.getAmount());
        nasosData.add(nasosData4);

        return IntStream.range(0, nasosData.size()).map(i -> nasosData.size() - 1-i).mapToObj(nasosData::get).collect(Collectors.toList());
    }


    @CheckPermission(form = FormEnum.NASOS, permission = Action.CAN_VIEW)
    public List<NasosDao> getNasos(){
        return nasosRepository.findAll(Sort.by(Sort.Order.desc("date"))).stream().map(Nasos::asDao).collect(Collectors.toList());
    }

    public NasosDao getById(long id){
        Optional<Nasos> nasos = nasosRepository.findById(id);
        return nasos.map(Nasos::asDao).orElse(null);
    }

    @CheckPermission(form = FormEnum.NASOS, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(NasosDao nasosDao) {
        Nasos nasos = nasosDao.asDao(nasosDao);
        if (nasos.getId() != 0){
            return edit(nasos);
        }
        Nasos saved = null;
        try {
            saved = nasosRepository.save(nasos);
            log.info("Nasos service qo'shildi !");
        }catch (Exception e){
            log.error("Nasos serviceni qo'shishda xatolik yuzaga keldi !");
        }
        return new ApiResponse(true, saved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.NASOS, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(Nasos nasos) {
        Nasos saved = null;
        try {
            saved = nasosRepository.save(nasos);
            log.info("Nasos service o'zgartirildi !");
        }catch (Exception e){
            log.error("Nasos serviceni o'zgartirishda xatolik yuzaga keldi !");
        }
        return new ApiResponse(true, saved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.NASOS, permission = Action.CAN_DELETE)
    public ApiResponse delete(long id) {
        Optional<Nasos> optionalNasos = nasosRepository.findById(id);
        if (optionalNasos.isPresent()){
            nasosRepository.deleteById(id);
            log.info("Filtr ID=>" + id + " tozalandi !");
            return new ApiResponse(true, LanguageManager.getLangMessage("deleted"));
        }else{
            log.error("Filtr ID=> " + id + "(O'chirish uchun uchun) topilmadi !");
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
