package utmn.migration.dto;

import java.util.List;

public record ProfileFormResponse(
        ProfileDataResponse profile,
        List<CitizenshipOptionResponse> citizenships
) {
}
