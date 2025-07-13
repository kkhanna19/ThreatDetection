package com.kajal.security;

import java.io.*;               // To read files
import java.util.*;             // For HashMap
import java.util.regex.*;       // For pattern matching (Regex)

public class LogParser {

    public static void main(String[] args) {
        // Path to the log file
        String filePath = "src/main/resources/access_log.txt";

        // Store failed attempts for each IP
        HashMap<String, Integer> failedCount = new HashMap<>();

        try {
            // Read the file line by line
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            // Pattern to find IPs from failed login attempts
            Pattern pattern = Pattern.compile("IP:(\\d+\\.\\d+\\.\\d+\\.\\d+).*Status:FAIL");

            while ((line = reader.readLine()) != null) {
                // Match the pattern in each line
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    // Extract the IP address
                    String ip = matcher.group(1);

                    // Count failed attempts for this IP
                    int currentCount = failedCount.getOrDefault(ip, 0);
                    failedCount.put(ip, currentCount + 1);
                }
            }

            // Close the file reader
            reader.close();

            // Print and insert suspicious IPs (those with more than 3 fails)
            System.out.println("Suspicious IPs:");
            for (Map.Entry<String, Integer> entry : failedCount.entrySet()) {
                if (entry.getValue() > 3) {
                    String ip = entry.getKey();
                    int attempts = entry.getValue();

                    System.out.println("IP: " + ip + " → Failed Attempts: " + attempts);

//                    Save to MySQL database
                    DatabaseSaver.insertSuspiciousIP(ip, attempts);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
