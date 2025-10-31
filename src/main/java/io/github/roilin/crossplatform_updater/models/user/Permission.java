package io.github.roilin.crossplatform_updater.models.user;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "permissions", uniqueConstraints = {@UniqueConstraint(columnNames = {"resource", "operation"})})
public class Permission implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String resource;
    @Column(nullable = false)
    private String operation;
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;

    @Override
    public String getAuthority() {
        return String.format("%s:%s", this.resource, this.operation);
    }
}
