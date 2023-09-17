package uz.cluster.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import uz.cluster.entity.references.model.Direction;


import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "documents")
@Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
public class Document extends Auditable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private long documentId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "direction_id")
    private Direction direction;

    @Column(name = "numbers")
    private int numbers;

    @Transient
    private int directionId;


    public Document(Document document) {
        if (document == null)
            return;
        init(document);

    }
    private void init(Document document) {
        this.documentId = document.getDocumentId();
        this.date = document.getDate();
        this.direction = document.getDirection();
        this.directionId = document.getDirectionId();
        this.numbers = document.getNumbers();
    }
}
