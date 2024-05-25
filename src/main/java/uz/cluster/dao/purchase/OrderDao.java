package uz.cluster.dao.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.reference.BaseDao;
import uz.cluster.entity.purchase.Order;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.enums.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDao extends BaseDao {

    private String client;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String phoneNumber;

    private String location;

    private double totalValue;

    private double fare;

    private double km;

    private Status status;

    List<OrderProductDao> purchasedProductList = new ArrayList<>();

    public Order copy(OrderDao orderDao){
        Order order = new Order();
        order.setId((int) orderDao.getId());
        order.setClient(orderDao.getClient());
        order.setPhoneNumber(orderDao.getPhoneNumber());
        order.setLocation(orderDao.getLocation());
        order.setKm(orderDao.getKm());
        order.setDate(orderDao.getDate());
        order.setFare(orderDao.getFare());
        order.setValue(orderDao.getTotalValue());
        order.setStatus(orderDao.getStatus());
        for (OrderProductDao purchasedProductDao : orderDao.getPurchasedProductList()){
            order.getPurchasedProductList().add(purchasedProductDao.copy(purchasedProductDao));
        }
        return order;
    }
}
