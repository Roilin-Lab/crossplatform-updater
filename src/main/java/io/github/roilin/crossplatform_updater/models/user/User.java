package io.github.roilin.crossplatform_updater.models.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.github.roilin.crossplatform_updater.models.Token;
import io.github.roilin.crossplatform_updater.models.UserDevice;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
@Table(name = "users")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Email
  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;
  private boolean enabled;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonIgnore
  private Set<UserDevice> devices;

  @OneToMany(mappedBy="user")
  private Set<Token> tokens;

  @ManyToOne
  private Role role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<String> authorities = new HashSet();
    role.getPermissions().forEach(p -> authorities.add(p.getAuthority()));
    authorities.add(role.getAuthority());
    return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
  }
}
