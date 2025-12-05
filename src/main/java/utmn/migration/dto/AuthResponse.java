package utmn.migration.dto;

public record AuthResponse(
        String token,
        String email,
        String name
) {}