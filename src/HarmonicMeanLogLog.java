
// refer to: https://static.googleusercontent.com/media/research.google.com/en//pubs/archive/40671.pdf

public class HarmonicMeanLogLog extends LogLog {
    @Override
    double getNumOfUniqueElements() {
        double count = -1;

        // we need to keep track of buckets that have actually been filled at least once
        // i.e bucket[i]!=-1;
        // so that we have a proper denominator to divide by for average
        double harmonicMeanDenominator = 0; // this is the total sum of all buckets (excluding buckets that are = -1)
        int m = 0;
        
        for (int i = 0; i < bucketsOfCountOfMax0s.length; i++) {
            harmonicMeanDenominator += 1.0 / Math.pow(2, bucketsOfCountOfMax0s[i]);
            m++;
        }
        
        double alpha = 0.71;

        double Z = (double) m / (double) harmonicMeanDenominator;
        count = m * (1/alpha) * Z; //imp here we are doing 1/alpha ie dividing by alpha
        return count;
    }

}
