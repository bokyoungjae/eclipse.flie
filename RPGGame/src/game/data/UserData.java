package game.data;

import java.io.*;
import java.util.*;

public class UserData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String email;
    private String nickname;
    private int selectedCharacterIndex = -1; // -1 = not chosen yet
    private CharacterData characterData;
    private Date createdAt;

    public UserData(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = new Date();
    }

    // Getters & Setters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public int getSelectedCharacterIndex() { return selectedCharacterIndex; }
    public void setSelectedCharacterIndex(int idx) { this.selectedCharacterIndex = idx; }
    public CharacterData getCharacterData() { return characterData; }
    public void setCharacterData(CharacterData cd) { this.characterData = cd; }
    public Date getCreatedAt() { return createdAt; }
    public boolean isNewPlayer() { return selectedCharacterIndex == -1; }
}
