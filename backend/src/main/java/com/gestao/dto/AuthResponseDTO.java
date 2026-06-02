package com.gestao.dto;

public class AuthResponseDTO {

    private String token;
    private String refreshToken;
    private Long id;
    private String name;
    private String username;   // frontend espera "username" (= email)
    private String role;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String token, Long id, String name, String username, String role) {
        this.token    = token;
        this.id       = id;
        this.name     = name;
        this.username = username;
        this.role     = role;
    }

    public String getToken()        { return token; }
    public void setToken(String token)          { this.token = token; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String rt)      { this.refreshToken = rt; }

    public Long getId()             { return id; }
    public void setId(Long id)      { this.id = id; }

    public String getName()         { return name; }
    public void setName(String name){ this.name = name; }

    public String getUsername()     { return username; }
    public void setUsername(String u){ this.username = u; }

    public String getRole()         { return role; }
    public void setRole(String role){ this.role = role; }
}
