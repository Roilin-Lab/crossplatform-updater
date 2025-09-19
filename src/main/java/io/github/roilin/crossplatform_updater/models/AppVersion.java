package io.github.roilin.crossplatform_updater.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.EnumType;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_version")
public class AppVersion {
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
}
