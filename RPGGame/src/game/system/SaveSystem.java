package game.system;

import game.data.UserData;
import java.io.*;
import java.util.*;

public class SaveSystem {
    private static final String SAVE_DIR = System.getProperty("user.home") + "/RPGGameSave/";
    private static final String USERS_FILE = SAVE_DIR + "users.dat";

    static {
        new File(SAVE_DIR).mkdirs();
    }

    @SuppressWarnings("unchecked")
    public static Map<String, UserData> loadAllUsers() {
        File f = new File(USERS_FILE);
        if (!f.exists()) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (Map<String, UserData>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public static void saveAllUsers(Map<String, UserData> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean register(String username, String password, String email) {
        Map<String, UserData> users = loadAllUsers();
        if (users.containsKey(username)) return false;
        users.put(username, new UserData(username, password, email));
        saveAllUsers(users);
        return true;
    }

    public static UserData login(String username, String password) {
        Map<String, UserData> users = loadAllUsers();
        UserData u = users.get(username);
        if (u != null && u.getPassword().equals(password)) return u;
        return null;
    }

    public static void saveUser(UserData user) {
        Map<String, UserData> users = loadAllUsers();
        users.put(user.getUsername(), user);
        saveAllUsers(users);
    }
}
