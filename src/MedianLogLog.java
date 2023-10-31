import java.util.Collections;
import java.util.PriorityQueue;


public class MedianLogLog extends LogLog {


    @Override
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
        double alpha = 0.99; //found by trial & err
        double median = myMed.getMedian();

        count = Math.pow(2, median)*numberOfBucketsFilled*alpha;
        return count;

      
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