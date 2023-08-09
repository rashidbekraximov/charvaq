package uz.cluster.services.references_service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.configuration.SpringSecurityAuditorAware;
import uz.cluster.dao.reference.DefaultReference;
import uz.cluster.entity.references.model.Reference;
import uz.cluster.entity.references.model.ReferenceList;
import uz.cluster.types.Nls;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DefaultReferenceService {

    @PersistenceContext
    private EntityManager entityManager;

    @CheckPermission(form = FormEnum.SIMILAR_INFORMATION, permission = Action.CAN_VIEW)
    public List<ReferenceList> getReferenceLists() {
        return (List<ReferenceList>) entityManager.createNativeQuery("select * from references_list", ReferenceList.class).getResultList();
    }
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Map<Integer, String> getReferenceItems(String viewName) {

        Map<Integer, String> references = new HashMap<>();
        String sqlString = "select id,name from " + viewName + " where status='ACTIVE' order by id ";

        Query query = entityManager.createNativeQuery(sqlString);
        List resultList = query.getResultList();

        resultList.forEach(o -> {
            Object[] refer = (Object[]) o;
            Integer id = (Integer) refer[0];
            Nls name = (Nls) refer[1];

            references.put(id, name.getActiveLanguage());
        });
        return references;
    }

    public List<Reference> getReferenceItems(int referenceId) {
        String tableName = entityManager.createNativeQuery("select table_name  from references_list t where id=:id").setParameter("id", referenceId).getSingleResult().toString();

        Query query = entityManager.createNativeQuery("select id, name from " + tableName + " order by id ");

        List resultList = query.getResultList();

        List<Reference> references = new ArrayList<>();

        resultList.forEach(o -> {
            Object[] refer = (Object[]) o;
            Integer id = (Integer) refer[0];
            Nls name = (Nls) refer[1];
            Reference reference = new Reference();

            reference.setId(id);
            reference.setName(name);
            reference.init();
            references.add(reference);
        });
        return references;
    }

    @Transactional
    public void save(DefaultReference reference) {
        String tableName = entityManager.createNativeQuery("select table_name  from references_list t where id=:id").setParameter("id", reference.getReferenceId()).getSingleResult().toString();
        Object createdByTable = entityManager.createNativeQuery("select table_name\n" +
                "from information_schema.columns\n" +
                "where table_schema not in ('information_schema', 'pg_catalog')\n" +
                "and column_name='created_by'\n" +
                "and table_name='" + tableName + "'").getSingleResult();

        boolean isHasAuditable = false;

        if (createdByTable != null && createdByTable.equals(tableName))
            isHasAuditable = true;

        List<Integer> oldIds = entityManager.createNativeQuery("select id from " + tableName).getResultList();

        SpringSecurityAuditorAware s = new SpringSecurityAuditorAware();
        int activeUserId = s.getCurrentAuditor().get();
        int maxId = 0;


        for (Integer oldId : oldIds) {
            final int currentId = oldId.intValue();

            if (maxId < currentId) {
                maxId = currentId;
            }

            if (reference.getItems().stream().filter(reId -> reId.getId() == currentId).count() == 0) {
                entityManager.createNativeQuery("delete from " + tableName + " where id = " + currentId).executeUpdate();
            }
        }

        for (Reference ref : reference.getItems()) {
            String query;

            if (ref.getId() == 0) {
                ref.setId(++maxId);
            }
            if (isHasAuditable) {
                query = "INSERT INTO " + tableName + "(id, name,status,created_by, created_on, modified_by, modified_on) VALUES(:id, row(:ru,:uz_cl,:uz_lat), :status,:created_by, :created_on, :modified_by, :modified_on)";
                query += " ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name,status=EXCLUDED.status, modified_by= EXCLUDED.modified_by, modified_on= EXCLUDED.modified_on";
            } else {
                query = "INSERT INTO " + tableName + "(id, name,status) VALUES(:id, row(:ru,:uz_cl,:uz_lat), :status)";
                query += " ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name,status=EXCLUDED.status";
            }
            char doubleQuote = 34;
            Query refQuery = entityManager.createNativeQuery(query);
            refQuery.setParameter("id", ref.getId());
            refQuery.setParameter("ru", ref.getRu().replaceAll(","," ").replaceAll("'"," ").replaceAll(String.valueOf(doubleQuote)," "));
            refQuery.setParameter("uz_cl", ref.getUzCl().replaceAll(","," ").replaceAll("'"," ").replaceAll(String.valueOf(doubleQuote)," "));
            refQuery.setParameter("uz_lat", ref.getUzLat().replaceAll(","," ").replaceAll("'"," ").replaceAll(String.valueOf(doubleQuote)," "));
            refQuery.setParameter("status", ref.getStatus().toString());
            if (isHasAuditable) {
                refQuery.setParameter("created_by", activeUserId);
                refQuery.setParameter("created_on", ref.getCreatedOn());
                refQuery.setParameter("modified_by", activeUserId);
                refQuery.setParameter("modified_on", ref.getModifiedOn());
            }
            refQuery.executeUpdate();
        }
    }
}
