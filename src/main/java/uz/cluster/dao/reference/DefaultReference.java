package uz.cluster.dao.reference;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import uz.cluster.entity.references.model.Reference;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class DefaultReference implements Serializable {

    int referenceId;

    List<Reference> items;


}
