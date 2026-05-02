package utmn.migration.dto;

public record DeleteConfirmationResponse(
        Long stepId,
        String receiveText,
        int conditionCount,
        int ruleCount,
        boolean canDelete
) {
}
