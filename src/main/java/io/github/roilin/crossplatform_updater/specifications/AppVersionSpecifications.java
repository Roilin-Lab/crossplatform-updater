package io.github.roilin.crossplatform_updater.specifications;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.enums.Platform;

public class AppVersionSpecifications {
    private static Specification<AppVersion> changeLogLike(String changeLog) {
        return (root, query, criteriaBuilder) -> {
            if (changeLog == null || changeLog.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("changeLog")),
                    "%" + changeLog.trim().toLowerCase() + "%");
        };
    }

    private static Specification<AppVersion> releaseDateLower(LocalDateTime maxDateTime) {
        return (root, query, criteriaBuilder) -> {
            if (maxDateTime == null) {
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("releaseDate"), maxDateTime);
        };
    }

    private static Specification<AppVersion> releaseDateGreater(LocalDateTime minDateTime) {
        return (root, query, criteriaBuilder) -> {
            if (minDateTime == null) {
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("releaseDate"), minDateTime);
        };
    }

    private static Specification<AppVersion> platformEqualse(Platform platform) {
        return (root, query, criteriaBuilder) -> {
            if (platform == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("platform"), platform);
        };
    }

    public static Specification<AppVersion> rangeDate(LocalDateTime max, LocalDateTime min) {
        return Specification.allOf(releaseDateGreater(min), releaseDateLower(max));
    }

    public static Specification<AppVersion> filterByPlatform(Platform platform) {
        return Specification.allOf(platformEqualse(platform));
    }
}
