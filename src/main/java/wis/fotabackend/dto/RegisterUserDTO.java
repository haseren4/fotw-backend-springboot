package wis.fotabackend.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterUserDTO{
    private String callsign;
    private String password;
    private String email;

    public RegisterUserDTO(){} // REQUIRED (public no-args)

    public String getCallsign() {
        return callsign;
    }
    public void setUsername(String callsign) {
        this.callsign = callsign;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword_hash(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Override public String toString() {
        return "RegisterUserDTO{" +
                "callsign='" + callsign + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
