package pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true) // игнорирует неописанные поля, т.е. можно описать в классе не все поля
public class UserPojo {
    private int id;
    private String email;
    @JsonProperty("first_name") //реальное название поля в json
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String avatar;
}
