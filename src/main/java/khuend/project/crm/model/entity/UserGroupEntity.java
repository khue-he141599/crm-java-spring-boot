package khuend.project.crm.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import khuend.project.crm.model.entity.base.OwnerBaseEntity;

import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "crm_user_groups", indexes = {
      @Index(name = "idx_crm_user_groups_code", columnList = "code"),
      @Index(name = "idx_crm_user_groups_leader_id", columnList = "leaderId")
})
@SQLDelete(sql = "UPDATE crm_user_groups SET deletedAt = now() WHERE id = ?")
@SQLRestriction("deletedAt IS NULL")
public class UserGroupEntity extends OwnerBaseEntity {

   @Column(nullable = false)
   @Builder.Default
   private boolean is_active = true;

   @Column(name = "business_role_id")
   private String businessRoleId;

   @Column
   private String name;

   @Column(unique = true)
   private String code;

   @Column(name = "leader_id")
   private String leaderId;

   @Column(name = "export_team_id")
   private String exportTeamId;

   @Transient
   private Object exportTeam;

   @Builder.Default
   @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(name = "crm_user_leader_group_relation", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
   private Set<UserEntity> leaders = new LinkedHashSet<>();
}