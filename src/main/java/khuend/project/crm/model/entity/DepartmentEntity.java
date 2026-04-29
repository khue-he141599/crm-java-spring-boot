package khuend.project.crm.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import khuend.project.crm.model.entity.base.OwnerBaseEntity;

import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "crm_departments", indexes = {
        @Index(name = "idx_crm_departments_code", columnList = "code"),
        @Index(name = "idx_crm_departments_parent_id", columnList = "parent_id")
})
@SQLDelete(sql = "UPDATE crm_departments SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class DepartmentEntity extends OwnerBaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DepartmentStatus status = DepartmentStatus.ACTIVE;

    @Column(name = "parent_id")
    private UUID parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private DepartmentEntity parent;

    @Column
    private String name;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    private String[] types;

    @Column(name = "business_role_id")
    private String businessRoleId;

    @Column
    private String code;

    public enum DepartmentStatus {
        ACTIVE,
        INACTIVE
    }
}