package uz.cluster.services.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.cluster.repository.purchase.PurchasedProductRepository;

@Service
@RequiredArgsConstructor
public class PurchasedProductService {

    private final PurchasedProductRepository purchasedProductRepository;

}
