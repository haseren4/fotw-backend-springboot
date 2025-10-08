// src/main/java/wis/fotabackend/dto/LocationProposal.java
package wis.fotabackend.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LocationProposal {

    private String proposedBy;
    private String siteName;
    private String category;
    private String location;
    private double latitude;
    private double longitude;

    public LocationProposal() {} // <-- REQUIRED (public no-args)

    public String getProposedBy() { return proposedBy; }
    public void setProposedBy(String proposedBy) { this.proposedBy = proposedBy; }

    public String getSiteName() { return siteName; }
    public void setSiteName(String siteName) { this.siteName = siteName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    @Override public String toString() {
        return "LocationProposal{" +
                "proposedBy='" + proposedBy + '\'' +
                ", siteName='" + siteName + '\'' +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
