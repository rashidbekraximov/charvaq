package uz.cluster.entity.references.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import uz.cluster.entity.Auditable;
import uz.cluster.types.Nls;
import uz.cluster.enums.Status;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Reference extends Auditable {

    @Id
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    int id;

    @Column(name = "name")
    Nls name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(20) default 'ACTIVE'")
    private Status status = Status.ACTIVE;

    @Transient
    String ru;

    @Transient
    String uzLat;

    @Transient
    String uzCl;

    public void init() {
        ru = name.getRu();
        uzLat = name.getUz_lat();
        uzCl = name.getUz_cl();
    }

    /**
     * Generic put method to map JPA native Query to this object.
     *
     * @param column
     * @param value
     */
    public void put(Object column, Object value) {
        if (((String) column).equals("name")) {
            setName((Nls) value);
        } else if (((String) column).equals("id")) {
            setId((int) value);
        }
    }
}
