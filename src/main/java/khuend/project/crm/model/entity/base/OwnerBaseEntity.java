package khuend.project.crm.model.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public class OwnerBaseEntity extends BaseEntity {

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "organization_id", nullable = false)
    private String organizationId;
}