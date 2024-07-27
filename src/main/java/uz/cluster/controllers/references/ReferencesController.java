package uz.cluster.controllers.references;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.cluster.entity.purchase.Price;
import uz.cluster.services.logistic.TechnicianService;
import uz.cluster.services.references_service.DefaultReferenceService;
import uz.cluster.services.references_service.FormService;

import java.util.*;

@RestController
@RequestMapping(path = "api/references/")
@Tag(name = "Oxshash malumotlar select ucun", description = "Spravichniy tablitsalar ustida amallar")
@RequiredArgsConstructor
public class ReferencesController {

    private final DefaultReferenceService defaultReferenceService;
    private final TechnicianService technicianService;

    public static Map<String, Map<Integer, String>> globalReferenceItems = new HashMap<>();

    public static Map<String, String> localReferences = new HashMap<>();
    public static Map<String, String> globalReferences = new HashMap<>();

    static {
        globalReferences.put("nasos_cost_type", "r_nasos_cost_type");
        globalReferences.put("produce_cost", "r_produce_cost");
        globalReferences.put("product_type", "r_product_type");
        globalReferences.put("product_for_produce", "r_product_for_produce");
        globalReferences.put("fuel_type", "r_fuel_type");
        globalReferences.put("spare_types", "r_spare_part_type");
        globalReferences.put("drobilka_type", "r_drobilka_type");
        globalReferences.put("cost_type", "r_cost_type");
        globalReferences.put("payment_type", "r_payment_type");
        globalReferences.put("technique_type", "r_technique_type");
        globalReferences.put("position", "r_position");
        globalReferences.put("direction", "r_direction");
        globalReferences.put("unit", "r_unit");
        globalReferences.put("reference_list", "references_list");
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
            return defaultReferenceService.getReferenceItems(viewName);
        }
        return null;
    }

    @GetMapping(value = "/def/searched")
    public List<Price> getSearchedAllReferences(@RequestParam String filter) {
        return defaultReferenceService.getSearchedReferenceLists(filter);
    }

    @Operation(summary = "oddish spravichniylar nomi")
    @GetMapping(value = "/def-name/{referenceKey}/{id}")
    public String getAllReferencesName(@PathVariable String referenceKey,@PathVariable int id) {
        if (!referenceKey.equals("technician")){
            Map<Integer,String> map = defaultReferenceService.getReferenceItems(referenceKey);
            for (Integer key : map.keySet()){
                if (key == id){
                    return map.get(key);
                }
            }
            return "";
        }else{
            Map<Integer,String> map = technicianService.getTechnicianForSelect();
            for (Integer key : map.keySet()){
                if (key == id){
                    return map.get(key);
                }
            }
            return "";
        }
    }
}
