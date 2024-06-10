package uz.cluster.entity.produce;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.cluster.dao.produce.ProduceRemainderDao;
import uz.cluster.entity.references.model.ProductForProduce;
import uz.cluster.enums.SexEnum;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "produce_reaminder")
public class ProduceRemainder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_for_produce_id")
    private ProductForProduce productForProduce;

    @Column(name = "amount")
    private double amount;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "sex", columnDefinition = "varchar(5) default 'SEX1'")
    private SexEnum sexEnum;

    @Transient
    private int productForProduceId;

    public ProduceRemainderDao asDao(){
        ProduceRemainderDao costDao = new ProduceRemainderDao();
        costDao.setId(getId());
        costDao.setProductForProduce(getProductForProduce());
        costDao.setProductForProduceId(getProductForProduceId());
        costDao.setAmount(getAmount());
        costDao.setSexEnum(getSexEnum());
        return costDao;
    }
}
