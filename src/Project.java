import java.util.Arrays;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Project {
    private static String generateRandomIPAddress() {
        Random rand = new Random();
        StringBuilder ipAddress = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            ipAddress.append(rand.nextInt(256)); // Each part of the IP address should be between 0 and 255
            if (i != 3) {
                ipAddress.append(".");
            }
        }

        return ipAddress.toString();
    }


    private static String[] generateRandomIPAddressArray(int size, int uniqueCount) {
        if (size <= 0 || uniqueCount <= 0 || uniqueCount > size) {
            throw new IllegalArgumentException("Invalid array size or unique count.");
        }

        String[] ipAddressArray = new String[size];
        for (int i = 0; i < uniqueCount; i++) {
            ipAddressArray[i] = generateRandomIPAddress();
        }

        for (int i = uniqueCount; i < size; i++) {
            int randomIndex = new Random().nextInt(uniqueCount);
            ipAddressArray[i] = ipAddressArray[randomIndex];
        }

        return ipAddressArray;
    }

    private static byte[] toHash(String str) throws NoSuchAlgorithmException {
        byte[] hashValue = null;

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        hashValue = md.digest(str.getBytes());

        return  hashValue;
    }
    


    public static void main(String[] args) throws NoSuchAlgorithmException {
        // System.out.println(Arrays.toString(generateRandomIPAddressArray(5, 2)));

        String str = generateRandomIPAddress();
        System.out.println(str);
        System.out.println(Arrays.toString(str.getBytes()));
        System.out.println(Arrays.toString(toHash(str)));
    }
}
