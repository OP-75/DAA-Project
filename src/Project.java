import java.util.ArrayList;
import java.util.Random;

import javax.crypto.IllegalBlockSizeException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Project {
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

        String[] ips = generateRandomIPAddressArray(10000, 6900);

        

        //here num of bytes is the num of bytes we want to consider while puting a count of zeros in bucket, ie num of buckets = 2^(numOfBytes*8)
        // due to some reasons I have kept the numOfBytesToConsider = 1 manually since I cant convert the first n bytes i bytes[] b to unsigned int

        //sarthaks loglog
        // SarthakLogLog algoObj = new SarthakLogLog();

        //my loglog
        // LogLog algoObj = new LogLog();
        HarmonicMeanLogLog algoObj = new HarmonicMeanLogLog();
        for (int i = 0; i < ips.length; i++) {
            algoObj.addToLogLog(ips[i]);
        }

        //IMPORTANT!!!! -  According to formula we dont have to divide by num of buckets filled but actually have to divide by TOTAL NUM OF BUCKETS
        // ALSO WE HAVE TO CALC COUNT AS (2^avgCountOf0s)*Total num of buckets*alpha
        //I read somewhere that alpha was found out to be 0.7 experimentally
        System.out.println(algoObj.getNumOfUniqueElements());



        ips = null;

        //lets test the Algo for small cardinalities (0 to 1000):
        int[] smallValTest = {677, 63, 394, 513, 505, 359, 670, 137, 26, 567, 992, 200, 503, 524, 901, 935, 831, 313, 57, 315, 21, 323, 302, 378, 697, 109, 273, 947, 49, 884, 312, 139, 271, 246, 396, 717, 555, 541, 264, 751, 134, 736, 453, 74, 192, 89, 649, 81, 758, 3, 835, 273, 356, 913, 863, 146, 289, 348, 969, 652, 847, 444, 657, 256, 932, 450, 49, 178, 347, 500, 248, 709, 616, 417, 215, 247, 306, 262, 475, 787, 865, 976, 402, 937, 939, 28, 902, 476, 182, 219, 859, 443, 362, 217, 33, 185, 129, 64, 304, 508};
        
        ArrayList<Double> errRateArr = new ArrayList<>();

        for (int actualCardinality : smallValTest) {
            String[] newIpArr = generateRandomIPAddressArray(actualCardinality+100, actualCardinality);
            LogLog loglogObj = new LogLog();
            for (int i = 0; i < newIpArr.length; i++) {
                loglogObj.addToLogLog(newIpArr[i]);
            }

            double estimateCardinality = loglogObj.getNumOfUniqueElements();
            double errorRate = Math.abs(estimateCardinality - actualCardinality)/actualCardinality;
            errRateArr.add(errorRate);
            // System.out.println(errorRate+" ,"+estimateCardinality+" , real = "+actualCardinality);
        }

        double errSum = 0;
        for (Double err : errRateArr) {
            errSum += err;
        }

        System.out.println("Avg error rate for small cardinalities (in percent) = "+errSum*100/errRateArr.size());


        //lets test the Algo for large cardinalities (20k to 100k):
        int[] largeCardinalityTest = {32975, 92360, 24920, 64911, 53426, 67064, 77810, 81144, 57684, 71302, 86453, 81794, 94099, 27743, 91733, 94381, 47239, 79389, 43384, 22623, 42188, 73519, 40379, 69097, 91388, 28514, 23792, 96316, 25219, 96404, 99156, 93864, 32051, 32120, 29557, 58662, 85323, 69108, 47139, 51819, 55891, 42878, 49771, 88340, 93735, 37050, 60750, 23736, 51321, 36944, 38344, 47003, 71037, 80794, 39518, 46121, 46400, 43009, 91441, 86753, 37393, 26564, 91810, 91996, 45443, 67015, 84603, 99666, 61793, 23796, 97425, 29276, 94693, 44489, 35871, 48252, 73914, 26691, 78123, 44837, 56771, 30353, 34120, 23686, 61471, 86287, 28233, 52027, 38681, 54091, 64999, 39879, 85121, 36372, 74648, 68914, 58601, 65233, 48279, 53443};
        
        errRateArr = new ArrayList<>();

        for (int actualCardinality : largeCardinalityTest) {
            String[] newIpArr = generateRandomIPAddressArray(actualCardinality+100, actualCardinality);
            LogLog loglogObj = new LogLog();
            for (int i = 0; i < newIpArr.length; i++) {
                loglogObj.addToLogLog(newIpArr[i]);
            }

            double estimateCardinality = loglogObj.getNumOfUniqueElements();
            double errorRate = Math.abs(estimateCardinality - actualCardinality)/actualCardinality;
            errRateArr.add(errorRate);
            // System.out.println(errorRate+" ,"+estimateCardinality+" , real = "+actualCardinality);
        }

        errSum = 0;
        for (Double err : errRateArr) {
            errSum += err;
        }
        
        System.out.println("Avg error rate for large cardinalities (in percent) = "+errSum*100/errRateArr.size());
        
        
        //---------------------------------------------------------------------------
        
        //lets test the Harmonic Algo for small cardinalities (0 to 1000):
        smallValTest = new int[]{677, 63, 394, 513, 505, 359, 670, 137, 26, 567, 992, 200, 503, 524, 901, 935, 831, 313, 57, 315, 21, 323, 302, 378, 697, 109, 273, 947, 49, 884, 312, 139, 271, 246, 396, 717, 555, 541, 264, 751, 134, 736, 453, 74, 192, 89, 649, 81, 758, 3, 835, 273, 356, 913, 863, 146, 289, 348, 969, 652, 847, 444, 657, 256, 932, 450, 49, 178, 347, 500, 248, 709, 616, 417, 215, 247, 306, 262, 475, 787, 865, 976, 402, 937, 939, 28, 902, 476, 182, 219, 859, 443, 362, 217, 33, 185, 129, 64, 304, 508};
        
        errRateArr = new ArrayList<>();

        for (int actualCardinality : smallValTest) {
            String[] newIpArr = generateRandomIPAddressArray(actualCardinality+100, actualCardinality);
            HarmonicMeanLogLog loglogObj = new HarmonicMeanLogLog();
            for (int i = 0; i < newIpArr.length; i++) {
                loglogObj.addToLogLog(newIpArr[i]);
            }

            double estimateCardinality = loglogObj.getNumOfUniqueElements();
            double errorRate = Math.abs(estimateCardinality - actualCardinality)/actualCardinality;
            errRateArr.add(errorRate);
            // System.out.println(errorRate+" ,"+estimateCardinality+" , real = "+actualCardinality);
        }

        errSum = 0;
        for (Double err : errRateArr) {
            errSum += err;
        }

        System.out.println("Avg error rate for small cardinalities using Harmonic Algo (in percent) = "+errSum*100/errRateArr.size());


        //lets test the Median Algo for large cardinalities (20k to 100k):
        largeCardinalityTest = new int[]{32975, 92360, 24920, 64911, 53426, 67064, 77810, 81144, 57684, 71302, 86453, 81794, 94099, 27743, 91733, 94381, 47239, 79389, 43384, 22623, 42188, 73519, 40379, 69097, 91388, 28514, 23792, 96316, 25219, 96404, 99156, 93864, 32051, 32120, 29557, 58662, 85323, 69108, 47139, 51819, 55891, 42878, 49771, 88340, 93735, 37050, 60750, 23736, 51321, 36944, 38344, 47003, 71037, 80794, 39518, 46121, 46400, 43009, 91441, 86753, 37393, 26564, 91810, 91996, 45443, 67015, 84603, 99666, 61793, 23796, 97425, 29276, 94693, 44489, 35871, 48252, 73914, 26691, 78123, 44837, 56771, 30353, 34120, 23686, 61471, 86287, 28233, 52027, 38681, 54091, 64999, 39879, 85121, 36372, 74648, 68914, 58601, 65233, 48279, 53443};
        
        errRateArr = new ArrayList<>();

        for (int actualCardinality : largeCardinalityTest) {
            String[] newIpArr = generateRandomIPAddressArray(actualCardinality+100, actualCardinality);
            HarmonicMeanLogLog loglogObj = new HarmonicMeanLogLog();
            for (int i = 0; i < newIpArr.length; i++) {
                loglogObj.addToLogLog(newIpArr[i]); 
            }

            double estimateCardinality = loglogObj.getNumOfUniqueElements();
            double errorRate = Math.abs(estimateCardinality - actualCardinality)/actualCardinality;
            errRateArr.add(errorRate);
            // System.out.println(errorRate+" ,"+estimateCardinality+" , real = "+actualCardinality);
        }

        errSum = 0;
        for (Double err : errRateArr) {
            errSum += err;
        }

        System.out.println("Avg error rate for large cardinalities using Harmonic Algo(in percent) = "+errSum*100/errRateArr.size());

        
        
        //---------------------------------------------------------------------------
        
        //lets test the  Median Algo with Median for small cardinalities (0 to 1000):
        smallValTest = new int[]{677, 63, 394, 513, 505, 359, 670, 137, 26, 567, 992, 200, 503, 524, 901, 935, 831, 313, 57, 315, 21, 323, 302, 378, 697, 109, 273, 947, 49, 884, 312, 139, 271, 246, 396, 717, 555, 541, 264, 751, 134, 736, 453, 74, 192, 89, 649, 81, 758, 3, 835, 273, 356, 913, 863, 146, 289, 348, 969, 652, 847, 444, 657, 256, 932, 450, 49, 178, 347, 500, 248, 709, 616, 417, 215, 247, 306, 262, 475, 787, 865, 976, 402, 937, 939, 28, 902, 476, 182, 219, 859, 443, 362, 217, 33, 185, 129, 64, 304, 508};
        
        errRateArr = new ArrayList<>();

        for (int actualCardinality : smallValTest) {
            String[] newIpArr = generateRandomIPAddressArray(actualCardinality+100, actualCardinality);
            SarthakLogLog loglogObj = new SarthakLogLog();
            for (int i = 0; i < newIpArr.length; i++) {
                loglogObj.addToLogLog(newIpArr[i]);
            }

            double estimateCardinality = loglogObj.getNumOfUniqueElements();
            double errorRate = Math.abs(estimateCardinality - actualCardinality)/actualCardinality;
            errRateArr.add(errorRate);
            // System.out.println(errorRate+" ,"+estimateCardinality+" , real = "+actualCardinality);
        }

        errSum = 0;
        for (Double err : errRateArr) {
            errSum += err;
        }

        System.out.println("Avg error rate for small cardinalities  using Median Algo (in percent) = "+errSum*100/errRateArr.size());


        //lets test the Median Algo for large cardinalities (20k to 100k):
        largeCardinalityTest = new int[]{32975, 92360, 24920, 64911, 53426, 67064, 77810, 81144, 57684, 71302, 86453, 81794, 94099, 27743, 91733, 94381, 47239, 79389, 43384, 22623, 42188, 73519, 40379, 69097, 91388, 28514, 23792, 96316, 25219, 96404, 99156, 93864, 32051, 32120, 29557, 58662, 85323, 69108, 47139, 51819, 55891, 42878, 49771, 88340, 93735, 37050, 60750, 23736, 51321, 36944, 38344, 47003, 71037, 80794, 39518, 46121, 46400, 43009, 91441, 86753, 37393, 26564, 91810, 91996, 45443, 67015, 84603, 99666, 61793, 23796, 97425, 29276, 94693, 44489, 35871, 48252, 73914, 26691, 78123, 44837, 56771, 30353, 34120, 23686, 61471, 86287, 28233, 52027, 38681, 54091, 64999, 39879, 85121, 36372, 74648, 68914, 58601, 65233, 48279, 53443};
        
        errRateArr = new ArrayList<>();

        for (int actualCardinality : largeCardinalityTest) {
            String[] newIpArr = generateRandomIPAddressArray(actualCardinality+100, actualCardinality);
            SarthakLogLog loglogObj = new SarthakLogLog();
            for (int i = 0; i < newIpArr.length; i++) {
                loglogObj.addToLogLog(newIpArr[i]);
            }

            double estimateCardinality = loglogObj.getNumOfUniqueElements();
            double errorRate = Math.abs(estimateCardinality - actualCardinality)/actualCardinality;
            errRateArr.add(errorRate);
            // System.out.println(errorRate+" ,"+estimateCardinality+" , real = "+actualCardinality);
        }

        errSum = 0;
        for (Double err : errRateArr) {
            errSum += err;
        }

        System.out.println("Avg error rate for large cardinalities using Median Algo(in percent) = "+errSum*100/errRateArr.size());
        
        

        



        
    }


}


class LogLog{

    int[] bucketsOfCountOfMax0s = null;
    
    LogLog(){
        
        this.bucketsOfCountOfMax0s = makeBuckets();
    }
    
    byte[] toHash(String str) throws NoSuchAlgorithmException {
        byte[] hashValue = null;
        
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        
        hashValue = md.digest(str.getBytes());
        
        return  hashValue;
    }
    
    int[] makeBuckets(){
        
        //here numberOfBytesToConsider = 1, then num of bits will be 1*8 then num of buckets will be 2^8 
        int numOfBits = 8;

        //this func makes buckets of sized 2^numOfBits & initializes them with -1

        int size = (int)Math.pow(2, numOfBits);
        int[] bucketsArr = new int[size];
        //we store the max number of zeros on the right side of the bytes arr [n-1,n-2...] in the bucket (unsigned int)byte[0]  

        for (int i = 0; i < bucketsArr.length; i++) {
            bucketsArr[i] = -1;
        }

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
        String binaryStr = "";
        for (byte x: b) {
            // this coverts byte to a binary string
            String binStrOfByte = String.format("%8s", Integer.toBinaryString(x & 0xFF)).replace(' ', '0');
            binaryStr = binaryStr + binStrOfByte;
        }

        
        
        // System.out.println(Byte.toUnsignedInt(b[0]));
        int bucketIndx = Byte.toUnsignedInt(b[0]);
        

        
        //now lets count the max number of 0s in our binaryStr representation of byte array "b"
        //not we are counting from right to left
        // System.out.println(binaryStr.length() +" ,"+b.length*8+", Bucket indx = "+bucketIndx);
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
            // System.out.println("count of 0 = "+countOf0s);
        }

    }

    double getNumOfUniqueElements(){
        double count = -1;

        //we need to keep track of buckets that have acctually been filled atleast once i.e bucket[i]!=-1;
        // so that we have a proper denomiator to divide by for average
        long totalCountOfMax0s = 0; //this is the total sum of all buckets (excluding buckets that are = -1)
        for (int i = 0; i < bucketsOfCountOfMax0s.length; i++) {

            if (bucketsOfCountOfMax0s[i]!=-1) {
                totalCountOfMax0s += bucketsOfCountOfMax0s[i];
                
            }
        }

        // count = (double)totalCountOfMax0s/(double)numberOfBucketsFilled;

        //IMPORTANT!!!! -  According to formula we dont have to divide by num of buckets filled but actually have to divide by TOTAL NUM OF BUCKETS
        // ALSO WE HAVE TO CALC COUNT AS (2^avgCountOf0s)*Total num of buckets*alpha
        //I read somewhere that alpha was found out to be 0.7 experimentally

        //IMP! numberOfBucketsFilled = m = 256 in our implementation
        int m = bucketsOfCountOfMax0s.length; //m= num of buckets
        double alpha = 0.78;
        
        count = (double)totalCountOfMax0s/(double)m;
        count = Math.pow(2, count)*m*alpha;

        return count;
    }



}
