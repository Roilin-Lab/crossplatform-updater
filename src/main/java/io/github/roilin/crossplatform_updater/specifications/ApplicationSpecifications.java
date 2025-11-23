package io.github.roilin.crossplatform_updater.specifications;

import org.springframework.data.jpa.domain.Specification;

import io.github.roilin.crossplatform_updater.models.Application;
import io.github.roilin.crossplatform_updater.models.enums.ApplicationType;

public class ApplicationSpecifications {
  private static Specification<Application> titleLike(String title) {
    return (root, query, criteriaBuilder) -> {
      if (title == null || title.trim().isEmpty()) {
        return null;
      }
      return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.trim().toLowerCase() + "%");
    };
  }

  private static Specification<Application> publisherEqualse(String publisher) {
    return (root, query, criteriaBuildet) -> {
      if (publisher == null) {
        return null;
      }
      return criteriaBuildet.equal(root.get("publisher"), publisher);
    };
  }

  private static Specification<Application> developerEqualse(String developer) {
    return (root, query, criteriaBuildet) -> {
      if (developer == null) {
        return null;
      }
      return criteriaBuildet.equal(root.get("developer"), developer);
    };
  }

  private static Specification<Application> typeEqualse(ApplicationType type) {
    return (root, query, criteriaBuildet) -> {
      if (type == null) {
        return null;
      }
      return criteriaBuildet.equal(root.get("type"), type);
    };
  }

  public static Specification<Application> filter(String title, String publisher, String developer, ApplicationType type) {
    return Specification.allOf(titleLike(title), publisherEqualse(publisher), developerEqualse(developer), typeEqualse(type));
  }

}
