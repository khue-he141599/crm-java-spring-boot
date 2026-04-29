package khuend.project.crm.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import khuend.project.crm.model.entity.base.BaseEntity;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DynamicUpdate
@Entity
@Table(name = "crm_users", indexes = {
      @Index(name = "idx_crm_users_iam_user_id", columnList = "iamUserId"),
      @Index(name = "idx_crm_users_username", columnList = "username"),
      @Index(name = "idx_crm_users_email", columnList = "email")
})
@SQLDelete(sql = "UPDATE crm_users SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class UserEntity extends BaseEntity {

   @Column(name = "account_type")
   private String accountType;

   @Column(name = "employee_no")
   private String employeeNo;

   @Column(name = "username", unique = true)
   private String username;

   @Column(name = "name")
   private String name;

   @Column(name = "fullname")
   private String fullname;

   @Column(name = "phone")
   private String phone;

   @JdbcTypeCode(SqlTypes.ARRAY)
   @Column(name = "phones", columnDefinition = "text[]")
   private String[] phones;

   @Column(name = "email")
   private String email;

   @Column(name = "avatar_url")
   private String avatarUrl;

   @Column(name = "organization_id")
   private UUID organizationId;

   @Column(name = "department_id")
   private UUID departmentId;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "department_id", insertable = false, updatable = false)
   private DepartmentEntity department;

   @Column(name = "address_id")
   private UUID addressId;

   @OneToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "address_id", insertable = false, updatable = false)
   private AddressEntity address;

   @Column(name = "working_address_id")
   private UUID workingAddressId;

   @Column(name = "created_by_id")
   private UUID createdById;

   @Enumerated(EnumType.STRING)
   @Column(name = "status", nullable = false)
   @Builder.Default
   private UserStatus status = UserStatus.PENDING;

   @Column(name = "iam_user_id", nullable = false)
   private String iamUserId;

   @Column(name = "iam_last_sync_at")
   private Instant iamLastSyncAt;

   @Column(name = "is_leader", nullable = false)
   @Builder.Default
   private boolean isLeader = false;

   @Column(name = "is_manager", nullable = false)
   @Builder.Default
   private boolean isManager = false;

   @Column(name = "technician_group_id")
   private UUID technicianGroupId;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "technician_group_id", insertable = false, updatable = false)
   private UserGroupEntity technicianGroup;

   @Column(name = "purchase_zone_id")
   private UUID purchaseZoneId;

   @JdbcTypeCode(SqlTypes.ARRAY)
   @Column(name = "group_receive_ticket_id", columnDefinition = "text[]")
   private String[] groupReceiveTicketIds;

   @Builder.Default
   @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(name = "crm_user_leader_group_relation", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
   private Set<UserGroupEntity> groups = new LinkedHashSet<>();

   @Transient
   private Object[] leaderGroups;

   @Transient
   private Object[] roleRelations;

   @Transient
   private Object[] workingZones;

   public String getName() {
      return (name == null || name.isBlank()) ? fullname : name;
   }

   public enum UserStatus {
      PENDING,
      ACTIVE,
      INACTIVE,
      BLOCKED
   }
}