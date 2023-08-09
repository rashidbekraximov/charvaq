package uz.cluster.services.references_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.FormDao;
import uz.cluster.entity.auth.User;
import uz.cluster.entity.references.model.Form;
import uz.cluster.enums.Status;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.auth.SystemRoleName;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.references.FormRepository;
import uz.cluster.util.LanguageManager;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FormService {
    private final FormRepository formRepository;

    @CheckPermission(form = FormEnum.FORM_LIST, permission = Action.CAN_VIEW)
    public List<FormDao> getAll() {

        List<Form> repositoryAll = formRepository.getForms();
        List<FormDao> formDaoList = new ArrayList<>();
        for (Form form : repositoryAll
        ) {
            FormDao formDao = new FormDao();
            formDao.copy(form);
            formDao.init();
            formDaoList.add(formDao);
        }
        return formDaoList;

    }

    @CheckPermission(form = FormEnum.FORM_LIST, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse update(FormDao formDao) {
        formDao.prepare();
        Optional<Form> form = formRepository.findById(formDao.getId());

        if (form.isEmpty()) {
            return new ApiResponse(true, LanguageManager.getLangMessage("not saved"));
        }

        Form finalOrigin = form.get();
        finalOrigin.copy(formDao);
        if (form.get().getParentFormId() != 0){
            form.get().setParentForm(formRepository.getOne(form.get().getParentFormId()));
        }
        formRepository.save(finalOrigin);
        return new ApiResponse(true, LanguageManager.getLangMessage("saved"));
    }

    public FormDao getFormById(int id) {
        Form form = formRepository.findById(id).orElse(new Form());
        FormDao formDao = new FormDao();
        formDao.copy(form);
        formDao.init();
        return formDao;
    }

    public List<Form> getAllFormLikeTree(User user) {
        List<Form> formList = formRepository.getForms();

        formList.forEach(form -> {
            form.setSearchKey();
            form.setParentFormId(form.getParentForm() != null ? form.getParentForm().getId() : null);
        });

//        List<Form> sortedList = formList.stream()
//                .sorted(Comparator.comparing(Form::isStarred).reversed())
//                .collect(Collectors.toList());

        formList.removeIf(form -> form.getParentForm() != null);
        filterByUserPermission(formList, user);
        return formList;
    }

    public void filterByUserPermission(List<Form> forms, @NotNull User user) {
        if (
                user.getSystemRoleName() == SystemRoleName.SYSTEM_ROLE_SUPER_ADMIN
                        || user.getSystemRoleName() == SystemRoleName.SYSTEM_ROLE_ADMIN
        ) {
            return;
        }

        if (user.getSystemRoleName() == SystemRoleName.SYSTEM_ROLE_MAIN_AUDITOR || user.getSystemRoleName() == SystemRoleName.SYSTEM_ROLE_AUDITOR || user.getSystemRoleName() == SystemRoleName.SYSTEM_ROLE_MODERATOR) {
            forms.removeIf(f -> f.getId() == 130);
            return;
        }
    }

    @Transactional
    public ApiResponse delete(int id) {
        Optional<Form> optionalForm = formRepository.findById(id);
        if (optionalForm.isEmpty()) {
            return new ApiResponse(false, id, LanguageManager.getLangMessage("cant_find"));
        }else{
            optionalForm.get().setStatus(Status.PASSIVE);
            formRepository.save(optionalForm.get());
            return new ApiResponse(true, id, LanguageManager.getLangMessage("deleted"));
        }
    }

}
