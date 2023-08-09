package uz.cluster.services.references_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.reference.TaxesDao;
import uz.cluster.entity.references.model.TaxType;
import uz.cluster.entity.references.model.Taxes;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.references.TaxTypeRepository;
import uz.cluster.repository.references.TaxesRepository;
import uz.cluster.util.GlobalParams;
import uz.cluster.util.LanguageManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaxService {

    private final TaxesRepository taxesRepository;

    private final TaxTypeRepository taxTypeRepository;

    @CheckPermission(form = FormEnum.TAXES, permission = Action.CAN_VIEW)
    public List<TaxesDao> getTaxList(){
        return taxesRepository.findAllByClusterId(GlobalParams.getCurrentClusterId()).stream().map(Taxes::asDao).collect(Collectors.toList());
    }

    public TaxesDao getById(int id){
        Optional<Taxes> optionalTaxes = taxesRepository.findById(id);
        if (optionalTaxes.isEmpty()){
            return new TaxesDao();
        }else {
            return optionalTaxes.get().asDao();
        }
    }


    public Taxes getByTaxTypeAndClusterId(int id){
        Taxes taxes = new Taxes();
        Optional<TaxType> optionalTaxType = taxTypeRepository.findById(id);
        Optional<Taxes> optionalTaxes = taxesRepository.findByTaxTypeAndClusterId(optionalTaxType.get(),GlobalParams.getCurrentClusterId());
        if (optionalTaxes.isPresent()){
            return optionalTaxes.get();
        }else {
            if (id == 1){
                taxes.setTaxAmount(0.15);
                return taxes;
            }else if (id == 2){
                taxes.setTaxAmount(0.12);
                return taxes;
            }
        }
        return taxes;
    }

    @CheckPermission(form = FormEnum.TAXES, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(TaxesDao tax) {
        Taxes taxes = tax.copy(tax);

        if (taxes.getId() != 0){
            return edit(taxes);
        }
        if (taxes.getTaxTypeId() == 0) {
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }
        Optional<Taxes> optionalTaxes = taxesRepository.findByTaxType_Id(taxes.getTaxTypeId());
        Optional<TaxType> optionalTaxType = taxTypeRepository.findById(taxes.getTaxTypeId());
        optionalTaxType.ifPresent(taxes::setTaxType);
        if (optionalTaxes.isPresent()){
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }
        taxes.setClusterId(GlobalParams.getCurrentClusterId());
        Taxes taxes1 = taxesRepository.save(taxes);
        return new ApiResponse(true, taxes1, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.TAXES, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(Taxes taxes) {
        Optional<Taxes> optionalTaxes = taxesRepository.findById(taxes.getId());
        if (taxes.getTaxType().getId() == 0) {
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }
        if (optionalTaxes.isEmpty()){
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }
        optionalTaxes.get().setTaxType(taxes.getTaxType());
        optionalTaxes.get().setTaxAmount(taxes.getTaxAmount());
        optionalTaxes.get().setStatus(taxes.getStatus());
        optionalTaxes.get().setClusterId(GlobalParams.getCurrentClusterId());
        Taxes tax = taxesRepository.save(optionalTaxes.get());
        return new ApiResponse(true, tax, LanguageManager.getLangMessage("saved"));
    }
}
