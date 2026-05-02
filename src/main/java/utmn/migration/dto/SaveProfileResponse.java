package utmn.migration.dto;

public record SaveProfileResponse(
        String message,
        boolean canOpenRoadmap,
        String redirectUrl,
        ProfileDataResponse profile
) {
}
