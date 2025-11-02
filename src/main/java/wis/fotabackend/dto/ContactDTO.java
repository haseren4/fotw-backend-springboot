package wis.fotabackend.dto;

public class ContactDTO {
    private int id;
    private Integer activationId;
    private String time; // ISO datetime or raw string from qso_time
    private String callsign; // callsign worked
    private String band; // mapped from frequency
    private String mode;
    private String rstSent;
    private String rstRcvd;
    private String notes;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getActivationId() { return activationId; }
    public void setActivationId(Integer activationId) { this.activationId = activationId; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getCallsign() { return callsign; }
    public void setCallsign(String callsign) { this.callsign = callsign; }

    public String getBand() { return band; }
    public void setBand(String band) { this.band = band; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getRstSent() { return rstSent; }
    public void setRstSent(String rstSent) { this.rstSent = rstSent; }

    public String getRstRcvd() { return rstRcvd; }
    public void setRstRcvd(String rstRcvd) { this.rstRcvd = rstRcvd; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
