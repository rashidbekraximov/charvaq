package uz.cluster.entity.references.model;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import uz.cluster.entity.references.abstract_.AbstractReferenceModel;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
@Table(name = "r_spare_part_type")
public class SparePartType extends AbstractReferenceModel {
}
