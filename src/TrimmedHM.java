import java.util.Arrays;


// refer to: https://static.googleusercontent.com/media/research.google.com/en//pubs/archive/40671.pdf


public class TrimmedHM extends LogLog{
    @Override
    double getNumOfUniqueElements() {

        //deep copy the array then sort
        
        int[] bucketsCopy = Arrays.copyOfRange(bucketsOfCountOfMax0s, 0, bucketsOfCountOfMax0s.length);

        Arrays.sort(bucketsCopy);
        //we say that this sorting takes const time

        int trimPercentage = (int)(0.15 * bucketsCopy.length);
        int m = 0;
        double harmonicMeanDenominator = 0;

        for(int i = trimPercentage; i < bucketsCopy.length - trimPercentage; i++) {
            harmonicMeanDenominator += 1.0 / Math.pow(2, bucketsOfCountOfMax0s[i]);
            m++;
        }

        double alpha = 2; //found by trial & err

        double Z = (double) m / (double) harmonicMeanDenominator;
        double count = m * (alpha) * Z;

        return count;
    }

}
