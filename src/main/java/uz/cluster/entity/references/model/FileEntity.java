package uz.cluster.entity.references.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
@Entity
@Table(name = "file")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String filePath;
    private long fileSize;
    private LocalDateTime uploadDateTime;

    public FileEntity() {
        this.uploadDateTime = LocalDateTime.now();
    }

    public FileEntity(String fileName, String filePath, long fileSize) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.uploadDateTime = LocalDateTime.now();
    }
}

