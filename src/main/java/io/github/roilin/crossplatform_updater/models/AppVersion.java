package io.github.roilin.crossplatform_updater.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_version")
public class AppVersion implements Comparable<AppVersion> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String version;

  @JdbcType(PostgreSQLEnumJdbcType.class)
  private Platform platform; 
  
  private LocalDateTime releaseDate;
  private String changeLog;
  private boolean isActive;
  
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private UpdateType updateType;

  @Override
  public int compareTo(AppVersion other) {
    return other.getReleaseDate().compareTo(this.releaseDate);
  }
}
