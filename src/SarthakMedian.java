import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Random;

import javax.crypto.IllegalBlockSizeException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SarthakMedian {
    static String generateRandomIPAddress() {
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

    static String[] generateRandomIPAddressArray(int size, int uniqueCount) {
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

    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException {

        String[] ips = generateRandomIPAddressArray(1000, 600);

        //here num of bytes is the num of bytes we want to consider while puting a count of zeros in bucket, ie num of buckets = 2^(numOfBytes*8)
        // due to some reasons I have kept the numOfBytesToConsider = 1 manually since I cant convert the first n bytes i bytes[] b to unsigned int
        SarthakLogLog algoObj = new SarthakLogLog();
        for (String ip : ips) {
            algoObj.addToLogLog(ip);
        }
        //IMPORTANT!!!! -  According to formula we dont have to divide by num of buckets filled but actually have to divide by TOTAL NUM OF BUCKETS
        // ALSO WE HAVE TO CALC COUNT AS (2^avgCountOf0s)*Total num of buckets*alpha
        //I read somewhere that alpha was found out to be 0.7 experimentally
        System.out.println(algoObj.getNumOfUniqueElements());

        // String str = generateRandomIPAddress();
        // System.out.println(str);
        // System.out.println(Arrays.toString(str.getBytes()));
        // System.out.println(Arrays.toString(toHash(str)));
        // System.out.println(toHash(str).length*8+" bits");

        // //if we want to make a bucket of 1 byte = 8 bits = 2^8 buckets ie 256 buckets
        // System.out.println("Value of 0th byte in unsigned int = "+Byte.toUnsignedInt(toHash(str)[0]));

    }
}


class SarthakLogLog {

    int[] bucketsOfCountOfMax0s = null;
    int numOfByteToConsider = 1;
    SarthakLogLog(){
        //here numberOfBytes is the number of bytes to consider while defining the bucketsArr size
        //ie whether to consider 1, 2, 3 etc. num of bytes
        int numOfBits = numOfByteToConsider*8;
        this.bucketsOfCountOfMax0s = makeBuckets(numOfBits);
    }

    byte[] toHash(String str) throws NoSuchAlgorithmException {
        byte[] hashValue = null;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        hashValue = md.digest(str.getBytes());
        return hashValue;
    }

    int[] makeBuckets(int numOfBits){
        if(numOfBits<=0) throw new IllegalArgumentException("Number of bits should be >= 1");
        //this func makes buckets of sized 2^numOfBits & initializes them with -1
        int size = (int)Math.pow(2, numOfBits);
        int[] bucketsArr = new int[size];
        //we store the max number of zeros on the right side of the bytes arr [n-1,n-2...] in the bucket (unsigned int)byte[0]
        Arrays.fill(bucketsArr, -1);
        return bucketsArr;
    }

    void addToLogLog(String str) throws NoSuchAlgorithmException, IllegalBlockSizeException{
        byte[] b = toHash(str);
        //remember byte arr b is of const size!!! so any operations u do using loops are O(1)

        // https://stackoverflow.com/questions/7619058/convert-a-byte-array-to-integer-in-java-and-vice-versa
        // see the above link on how to get int from byte


        //below lines covert byte to binary string, 2 is the base btw
        // byte b = (byte) Integer.parseInt("00000010",2);
        // System.out.println(b);
        // System.out.println(Integer.toString(b,2));


        //convert byte arr into binary string
        StringBuilder binaryStr = new StringBuilder();
        for (byte x: b) {
            // this coverts byte to a binary string
            String binStrOfByte = String.format("%8s", Integer.toBinaryString(x & 0xFF)).replace(' ', '0');
            binaryStr.append(binStrOfByte);
        }
        if(numOfByteToConsider!=1){
            throw new IllegalBlockSizeException("numOfByteToConsider variable != 1");
        }
        // System.out.println(Byte.toUnsignedInt(b[0]));
        int bucketIndx = Byte.toUnsignedInt(b[0]);
        //now lets count the max number of 0s in our binaryStr representation of byte array "b"
        //note - we are counting from right to left
//        System.out.println(binaryStr.length() +" ,"+b.length*8+", Bucket index = "+bucketIndx);
        int countOf0s = 0;
        for (int i = binaryStr.length()-1; i >=0 ; i--) {

            if(binaryStr.charAt(i)=='1'){
                break;
            }
            else{
                countOf0s++;
            }
        }
        if(countOf0s!=0){
            countOf0s = Math.max(countOf0s, bucketsOfCountOfMax0s[bucketIndx]);
            bucketsOfCountOfMax0s[bucketIndx] = countOf0s;
//            System.out.println("count of 0 = "+countOf0s);
        }

    }
    double getNumOfUniqueElements(){
        double count = -1;
        //we need to keep track of buckets that have actually been filled at least once i.e bucket[i]!=-1;
        // so that we have a proper denominator to divide by for average

        //CREATING OBJECT FOR MEDIAN
        MedianOfStream myMed = new MedianOfStream();

        for (int i = 0; i < bucketsOfCountOfMax0s.length; i++) {
            myMed.add(bucketsOfCountOfMax0s[i]);
        }

        long numberOfBucketsFilled = bucketsOfCountOfMax0s.length;
        double alpha = 0.7;
        double median = myMed.getMedian();

//        count = median/numberOfBucketsFilled;
        count = Math.pow(2, median)*numberOfBucketsFilled*alpha;
        return count;

        /*
         Hitesh's code
         */

//        long totalCountOfMax0s = 0; //this is the total sum of all buckets (excluding buckets that are = -1)
//        for (int i = 0; i < bucketsOfCountOfMax0s.length; i++) {
//
//            if (bucketsOfCountOfMax0s[i]!=-1) {
//                totalCountOfMax0s += bucketsOfCountOfMax0s[i];
//            }
//        }


        // count = (double)totalCountOfMax0s/(double)numberOfBucketsFilled;
        //IMPORTANT!!!! -  According to formula we don't have to divide by num of buckets filled but actually have to divide by TOTAL NUM OF BUCKETS
        // ALSO WE HAVE TO CALC COUNT AS (2^avgCountOf0s)*Total num of buckets*alpha
        //I read somewhere that alpha was found out to be 0.7 experimentally
//dfghj
//        long numberOfBucketsFilled = bucketsOfCountOfMax0s.length;
//        double alpha = 0.75;
//        count = (double)totalCountOfMax0s/(double)numberOfBucketsFilled;
//        count = Math.pow(2, count)*numberOfBucketsFilled*alpha;
//
//        return count;
    }

}
class MedianOfStream{
    PriorityQueue<Integer> pq1;
    PriorityQueue<Integer> pq2;

    MedianOfStream(){
        pq1 = new PriorityQueue<>(Collections.reverseOrder());
        pq2 = new PriorityQueue<>();
    }

    private static boolean even = true;
    public void add(int num){
        if(pq2.size()==0 && pq1.size()==0){
            pq1.add(num);
            return;
        }
        if(even){
            pq1.add(num);
            pq2.add(pq1.poll());
        }
        else{
            pq2.add(num);
            pq1.add(pq2.poll());
        }
        even = !even;
    }

    public double getMedian(){
        if(even){
            return (double) (pq1.peek() + pq2.peek())/2;
        }
        else{
            return (double)(pq1.peek());
        }
    }

}