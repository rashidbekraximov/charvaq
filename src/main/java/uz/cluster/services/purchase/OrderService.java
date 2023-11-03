package uz.cluster.services.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.purchase.OrderDao;
import uz.cluster.dao.purchase.PurchaseDao;
import uz.cluster.dao.purchase.PurchasedProductDao;
import uz.cluster.entity.purchase.Order;
import uz.cluster.entity.purchase.PurchasedProduct;
import uz.cluster.entity.references.model.Employee;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.enums.Status;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.purchase.OrderRepository;
import uz.cluster.repository.references.ProductTypeRepository;
import uz.cluster.util.LanguageManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductTypeRepository productTypeRepository;

    @CheckPermission(form = FormEnum.ORDER, permission = Action.CAN_VIEW)
    public List<OrderDao> getOrderList() {
        return orderRepository.findAll().stream().map(Order::asDao).collect(Collectors.toList());
    }

    public OrderDao getById(int id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty()) {
            return null;
        } else {
            OrderDao orderDao = optionalOrder.get().asDao();
            PurchasedProductDao purchaseDao = new PurchasedProductDao();
            purchaseDao.setProductTypeId(orderDao.getProductType().getId());
            purchaseDao.setWeight(orderDao.getWeight());
            purchaseDao.setPrice(orderDao.getPrice());
            purchaseDao.setValue(orderDao.getValue());
            orderDao.getPurchasedProductList().add(purchaseDao);
            return orderDao;
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

        if (order.getProductTypeId() == 0) {
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }

        Optional<ProductType> optionalProductType = productTypeRepository.findById(order.getProductTypeId());
        optionalProductType.ifPresent(order::setProductType);

        if (order.getId() != 0) {
            return edit(order);
        }
        order.setStatus(Status.ACTIVE);
        Order orderSaved = orderRepository.save(order);
        return new ApiResponse(true, orderSaved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.ORDER, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(Order order) {
        Optional<Order> optionalOrder = orderRepository.findById(order.getId());
        if (optionalOrder.isPresent()){
            Order orderEdited = orderRepository.save(optionalOrder.get());
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
            Order orderPassive = orderRepository.save(optionalOrder.get());
            return new ApiResponse(true, orderPassive, LanguageManager.getLangMessage("deleted"));
        }else{
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

}
