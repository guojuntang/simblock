package simblock.util;

import java.security.MessageDigest;

public class Helper {
    public static String calSHA256(String s){
        byte[] cipher_byte;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(s.getBytes());
            cipher_byte = md.digest();
            StringBuilder sb = new StringBuilder(2 * cipher_byte.length);
            for (byte b : cipher_byte) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
