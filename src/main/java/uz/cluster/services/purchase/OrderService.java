package uz.cluster.services.purchase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.purchase.OrderDao;
import uz.cluster.entity.purchase.Order;
import uz.cluster.entity.purchase.OrderedProduct;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.enums.Status;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.purchase.OrderProductRepository;
import uz.cluster.repository.purchase.OrderRepository;
import uz.cluster.repository.references.ProductTypeRepository;
import uz.cluster.util.LanguageManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderProductRepository orderProductRepository;

    private final ProductTypeRepository productTypeRepository;

    @CheckPermission(form = FormEnum.ORDER, permission = Action.CAN_VIEW)
    public List<OrderDao> getOrderList() {
        return orderRepository.findAll(Sort.by(Sort.Order.desc("date"), Sort.Order.asc("status"))).stream().map(Order::asDao).collect(Collectors.toList());
    }

    public OrderDao getById(int id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty()) {
            log.error("");
            return null;
        } else {
            List<OrderedProduct> products = orderProductRepository.findAllByPurchaseId(optionalOrder.get().getId());
            optionalOrder.get().setPurchasedProductList(products);
            log.info("");
            return optionalOrder.get().asDao();
        }
    }

    public Map<Integer,String> getForSelect(){
        Map<Integer,String> htmlOption = new HashMap<>();
        List<Order> orders = orderRepository.findAllByStatus(Status.ACTIVE);
        for (Order order : orders) {
            htmlOption.put(order.getId(), order.getClient());
        }
        return htmlOption;
    }

    @CheckPermission(form = FormEnum.ORDER, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(OrderDao orderDao) {
        Order order = orderDao.copy(orderDao);


        if (order.getId() != 0) {
            return edit(order);
        }
        order.setStatus(Status.ACTIVE);
        Order orderSaved = orderRepository.save(order);

        for (OrderedProduct o : order.getPurchasedProductList()){
            o.setPurchaseId(orderSaved.getId());
            Optional<ProductType> optionalProductType = productTypeRepository.findById(o.getProductTypeId());
            optionalProductType.ifPresent(o::setProductType);
            orderProductRepository.save(o);
        }

        return new ApiResponse(true, orderSaved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.ORDER, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(Order order) {
        Optional<Order> optionalOrder = orderRepository.findById(order.getId());
        if (optionalOrder.isPresent()){
            orderProductRepository.deleteAllByPurchaseId(optionalOrder.get().getId());
            Order orderEdited = orderRepository.save(order);
            for (OrderedProduct o : order.getPurchasedProductList()){
                o.setPurchaseId(orderEdited.getId());
                orderProductRepository.save(o);
            }
            return new ApiResponse(true, orderEdited, LanguageManager.getLangMessage("edited"));
        }else{
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.ORDER, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(int id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()){
            optionalOrder.get().setStatus(Status.REJECTED);
            orderProductRepository.deleteAllByPurchaseId(optionalOrder.get().getId());
            Order orderPassive = orderRepository.save(optionalOrder.get());
            return new ApiResponse(true, orderPassive, LanguageManager.getLangMessage("deleted"));
        }else{
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

}
