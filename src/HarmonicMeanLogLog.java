public class HarmonicMeanLogLog extends LogLog {
    @Override
    double getNumOfUniqueElements(){
        double count = -1;

        //we need to keep track of buckets that have acctually been filled atleast once i.e bucket[i]!=-1;
        // so that we have a proper denomiator to divide by for average
        double harmonicMeanDenominator = 0; //this is the total sum of all buckets (excluding buckets that are = -1)
        int m = 0; //m= num of buckets
        for (int i = 0; i < bucketsOfCountOfMax0s.length; i++) {
            
            if (bucketsOfCountOfMax0s[i]!=-1) {
                harmonicMeanDenominator += 1.0/(double)bucketsOfCountOfMax0s[i];
                m++;
            }
        }

        

        //IMP! numberOfBucketsFilled = m = 256 in our implementation

        double alpha = 1;
        // double alpha = 0.78;
        
        double harmonicMean = (double)m/(double)harmonicMeanDenominator;
        count = m*alpha*Math.pow(2, harmonicMean);

        return count;
    }

}
