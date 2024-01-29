package com.plugin.bean;

/**
 * XXXXXX
 *
 * @author 沈钦林
 * @date 2023/4/3 16:08
 */
public class LoginDto {
    private static final long serialVersionUID=1L;

    private String username;

    private String password;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
