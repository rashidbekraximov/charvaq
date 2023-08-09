package uz.cluster.entity.references.model;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import uz.cluster.entity.Auditable;
import uz.cluster.enums.Status;
import uz.cluster.types.Nls;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "references_list")
@Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
public class ReferenceList extends Auditable {

    @Hidden
    @Id
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    @GeneratedValue
    private int id;

    @Column(name = "table_name")
    private String tableName;

    @Column(columnDefinition = "t_nls", name = "name")
    private Nls title;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(20) default 'ACTIVE'")
    private Status status = Status.ACTIVE;

}

