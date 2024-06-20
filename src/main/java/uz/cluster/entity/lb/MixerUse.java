package uz.cluster.entity.lb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Audited(withModifiedFlag = true)
@Getter
@Setter
@Table(name = "mixer_use")
public class MixerUse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lb_purchase_id",nullable = true)
    @JsonIgnore
    private LBPurchase lbPurchase;

    @ManyToOne
    @JoinColumn(name = "mixer_id")
    private Mixer mixer;

    @Column(name = "xodka",columnDefinition = "real default 0")
    private byte xodka;

    @Transient
    private int mixerId;
}
