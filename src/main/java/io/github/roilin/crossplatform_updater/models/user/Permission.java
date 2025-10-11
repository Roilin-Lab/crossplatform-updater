package io.github.roilin.crossplatform_updater.models.user;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Permission implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String resource;
    private String operation;
    private Set<Role> roles;

    @Override
    public String getAuthority() {
        return String.format("%s:%s", this.resource, this.operation);
    }
}
