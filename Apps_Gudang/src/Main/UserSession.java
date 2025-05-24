package Main;

public class UserSession {
    private static String userId;
    private static String namaUser;
    private static String levelUser;
    
    public static void setUserId(String id) {
        userId = id;
        System.out.println("UserSession - Set UserId: " + id);
    }
    
    public static void setNamaUser(String nama) {
        namaUser = nama;
        System.out.println("UserSession - Set NamaUser: " + nama);
    }
    
    public static void setLevelUser(String level) {
        levelUser = level;
        System.out.println("UserSession - Set LevelUser: " + level);
    }
    
    // Getter methods
    public static String getUserId() {
        System.out.println("UserSession - Get UserId: " + userId);
        return userId;
    }
    
    public static String getNamaUser() {
        System.out.println("UserSession - Get NamaUser: " + namaUser);
        return namaUser;
    }
    
    public static String getLevelUser() {
        System.out.println("UserSession - Get LevelUser: " + levelUser);
        return levelUser;
    }
    
    // Method untuk check apakah user sudah login
    public static boolean isLoggedIn() {
        boolean loggedIn = (userId != null && namaUser != null && levelUser !=null);
        System.out.println("UserSession - isLoggedIn: " + loggedIn);
        return loggedIn;
    }
    
    public static void clearSession() {
        userId = null;
        namaUser = null;
        levelUser = null;
        System.out.println("UserSession - Session cleared");
    }
}