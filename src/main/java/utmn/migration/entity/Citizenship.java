package utmn.migration.entity;

public enum Citizenship {
    UKRAINE("Украина"),
    BELARUS("Беларусь"),
    KYRGYZSTAN("Киргизия"),
    KAZAKHSTAN("Казахстан"),
    ARMENIA("Армения"),
    TAJIKISTAN("Таджикистан"),
    UZBEKISTAN("Узбекистан"),
    OTHER("Другое");

    private final String displayName;

    Citizenship(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}