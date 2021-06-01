package App.model.comm;

import com.google.gson.Gson;

public class User {
    private String authorizationCode;
    private String access_token;
    private String refresh_token;
    private String token_type;


    public User(String authorizationCode, String access_token, String refresh_token, String token_type) {
        this.authorizationCode = authorizationCode;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.token_type = token_type;
    }

    public User(String authorizationCode, String access_token, String refresh_token) {
        this.authorizationCode = authorizationCode;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.token_type = "Bearer";
    }

    public User(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    {
        access_token = "";
        refresh_token = "";
        token_type = "";
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "access_token='" + access_token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", token_type='" + token_type + '\'' +
                '}';
    }

    public static User CreateUserFromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
    }
}
