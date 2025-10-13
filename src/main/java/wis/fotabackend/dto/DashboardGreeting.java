package wis.fotabackend.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DashboardGreeting {
    private String callsign;
    private String greeting;
    private String messageOfTheDay;

    public DashboardGreeting() {}

    public DashboardGreeting(String callsign, String greeting, String messageOfTheDay) {
        this.callsign = callsign;
        this.greeting = greeting;
        this.messageOfTheDay = messageOfTheDay;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public String getMessageOfTheDay() {
        return messageOfTheDay;
    }

    public void setMessageOfTheDay(String messageOfTheDay) {
        this.messageOfTheDay = messageOfTheDay;
    }

    @Override
    public String toString() {
        return "DashboardGreeting{" +
                "callsign='" + callsign + '\'' +
                ", greeting='" + greeting + '\'' +
                ", messageOfTheDay='" + messageOfTheDay + '\'' +
                '}';
    }
}
