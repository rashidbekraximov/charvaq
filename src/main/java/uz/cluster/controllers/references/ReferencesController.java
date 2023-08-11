package uz.cluster.controllers.references;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.cluster.services.references_service.DefaultReferenceService;
import uz.cluster.services.references_service.FormService;

import java.util.*;

@RestController
@RequestMapping(path = "api/references/")
@Tag(name = "Oxshash malumotlar select ucun", description = "Spravichniy tablitsalar ustida amallar")
@RequiredArgsConstructor
public class ReferencesController {

    private final DefaultReferenceService defaultReferenceService;
    private final FormService formService;

    public static Map<String, Map<Integer, String>> globalReferenceItems = new HashMap<>();

    public static Map<String, String> globalReferences = new HashMap<>();
    public static Map<String, String> localReferences = new HashMap<>();

    static {
        globalReferences.put("product_type", "r_product_type");
        globalReferences.put("status", "r_status");
        globalReferences.put("currency_unit", "r_currency_unit");
        globalReferences.put("salary_groups", "r_salary_groups");
        globalReferences.put("salary_type", "r_salary_type");
        globalReferences.put("unit_of_measurements", "r_unit_of_measurements");
        globalReferences.put("forms", "r_forms");
        globalReferences.put("reference_list", "references_list");
        globalReferences.put("communal_group", "r_communal_group");
        globalReferences.put("tax_type", "r_tax_type");
    }

    public static void clearGlobalCache() {
        globalReferenceItems.clear();
    }


    @Operation(summary = "oddish spravichniylar ro'yxatini olish")
    @GetMapping(value = "/def/{referenceKey}")
    public Map<Integer, String> getAllReferences(@PathVariable(name = "referenceKey") String referenceKey) {
        if (globalReferences.containsKey(referenceKey)) {

            String viewName = globalReferences.get(referenceKey);
            System.out.println(referenceKey + " == " + viewName);


            Map<Integer, String> items = defaultReferenceService.getReferenceItems(viewName);
            return items;
        }
        return null;
    }
}
