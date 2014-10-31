package de.rwth.idsg.ixsi.enums;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 31.10.2014
 */
public enum ErrorCodeType {
    AUTH_PROVIDER_UNKNOWN("auth_provider_unknown"),     // Authentifizierung: Unbekannte Provider-ID
    AUTH_INVALID_PASSWORD("auth_invalid_password"),     // Authentifizierung: User-Passwort-Kombination ungültig
    AUTH_INVALID_TOKEN("auth_invalid_token"),           // Authentifizierung: User-Token-Kombination ungültig
    AUTH_SESSION_INVALID("auth_session_invalid"),       // Authentifizierung: Session ist ungültig/abgelaufen
    AUTH_ANON_NOT_ALLOWED("auth_anon_not_allowed"),     // Authentifizierung: Anonymer User nicht erlaubt
    AUTH_NOT_AUTHORIZED("auth_not_authorized"),         // Autorisation: Nutzer ist zu dieser Anfrage nicht berechtigt

    SYS_BACKEND_FAILED("sys_backend_failed"),       // System: Hintergrundsystem antwortet nicht
    SYS_UNKNOWN_FAILURE("sys_unknown_failure"),     // System: Unbekannter Fehler
    SYS_NOT_IMPLEMENTED("sys_not_implemented"),     // System: Request nicht implementiert

    // System: Request ist nicht plausibel. Dieser Wert sollte stets verwendet werden,
    // wenn inhaltlich Fehler im Request enthalten sind.
    SYS_REQUEST_NOT_PLAUSIBLE("sys_request_not_plausible"),

    BOOKING_TARGET_UNKNOWN("booking_target_unknown"),       // Buchungsziel unbekannt
    PRICE_INFO_NOT_AVAILABLE("price_info_not_available"),   // Preisinformationen nicht verfügbar

    BOOKING_TOO_SHORT("booking_too_short"),                         // Buchungsdauer zu kurz
    BOOKING_TOO_LONG("booking_too_long"),                           // Buchungsdauer zu lang
    BOOKING_TARGET_NOT_AVAILABLE("booking_target_not_available"),   // Buchungsziel im gegebenen Zeitraum nicht buchbar
    BOOKING_CHANGE_NOT_POSSIBLE("booking_change_not_possible"),     // Buchungsänderung kann nicht durchgeführt werden

    // Unbekannte Buchungs-ID. Dieser Wert sollte auch verwendet werden,
    // wenn die Buchungs-ID einem anderen User zugeordnet ist.
    BOOKING_ID_UNKNOWN("booking_id_unknown"),

    // Angefragte Sprache nicht vollständig unterstützt,
    // andere Sprache geliefert.
    LANGUAGE_NOT_SUPPORTED("language_not_supported");

    private final String value;

    private ErrorCodeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
