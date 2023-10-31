import java.util.Arrays;

public class TrimmedHM extends LogLog{
    @Override
    double getNumOfUniqueElements() {
        Arrays.sort(bucketsOfCountOfMax0s);
        //we say that this sorting takes const time

        int trimPercentage = (int)(0.1 * bucketsOfCountOfMax0s.length);
        int m = 0;
        double harmonicMeanDenominator = 0;

        for(int i = trimPercentage; i < bucketsOfCountOfMax0s.length - trimPercentage; i++) {
            if(bucketsOfCountOfMax0s[i]!=-1) {
                harmonicMeanDenominator += 1.0/(double)bucketsOfCountOfMax0s[i];
                m++;
            }
        }

//        double alpha = 1;
         double alpha = 0.78;

        double harmonicMean = (double) m / harmonicMeanDenominator;

        return m * alpha * Math.pow(2, harmonicMean);
    }

}
