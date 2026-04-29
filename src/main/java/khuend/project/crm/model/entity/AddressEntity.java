package khuend.project.crm.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import khuend.project.crm.model.entity.base.OwnerBaseEntity;

import java.text.Normalizer;
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
@Table(name = "crm_addresses")
@SQLDelete(sql = "UPDATE crm_addresses SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class AddressEntity extends OwnerBaseEntity {

    @Column(name = "latitude", nullable = false)
    @Builder.Default
    private Double latitude = 0D;

    @Column(name = "longitude", nullable = false)
    @Builder.Default
    private Double longitude = 0D;

    @Column(name = "country")
    private String country;

    @Column(name = "country_id")
    private String countryId;

    @Column(name = "province")
    private String province;

    @Column(name = "province_id")
    private String provinceId;

    @Column(name = "district")
    private String district;

    @Column(name = "district_id")
    private String districtId;

    @Column(name = "ward")
    private String ward;

    @Column(name = "ward_id")
    private String wardId;

    @Column(name = "address")
    private String address;

    @Column(name = "address_zone_id")
    private String addressZoneId;

    @Column(name = "iam_address_id")
    private String iamAddressId;

    @Column(name = "metadata", length = 2000)
    private String metadata;

    @Column(name = "version", nullable = false)
    @Builder.Default
    private Integer version = 1;

    @PrePersist
    @PreUpdate
    public void buildMetadata() {
        StringBuilder builder = new StringBuilder();
        append(builder, latitude);
        append(builder, longitude);
        append(builder, address);
        append(builder, country);
        append(builder, province);
        append(builder, district);
        append(builder, ward);
        this.metadata = normalize(builder.toString());
    }

    private static void append(StringBuilder builder, Object value) {
        if (value == null) {
            return;
        }
        String text = value.toString().trim();
        if (text.isEmpty()) {
            return;
        }
        if (!builder.isEmpty()) {
            builder.append(';');
        }
        builder.append(text);
    }

    private static String normalize(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .replace('đ', 'd')
                .replace('Đ', 'D');
        return normalized.toLowerCase().replaceAll("\\s+", " ").trim();
    }
}