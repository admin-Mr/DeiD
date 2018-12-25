/**
 * Project:     隆典-Web應用系統開發平台專案
 * Filename:    UUID.java
 * Author:      吳家瑞
 * Create Date: 2006/2/16
 * Version:     
 * Description: 
 */
package util;

public class UUID {
    public static String generate() {
        return java.util.UUID.randomUUID().toString();
    }
}
