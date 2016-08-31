import java.util.*;
import java.io.*;

public class CheckLandscape {
    public static void main (String[] args) {
        Global global = new Global(args[0], args[1], args[2]);
        int contributionNum = Global.contributionNum, interactionNum = Global.interactionNum;
        
        Firms oneFirm = new Firms();
        double[] max = new double[contributionNum*interactionNum], min = new double[contributionNum*interactionNum];
        Arrays.fill(max, 0); Arrays.fill(min, 1);

        for (int interactionNo = 0; interactionNo < interactionNum; interactionNo ++) {
            Global.newInteractLst(interactionNo);
            for (int contributionNo = 0; contributionNo < contributionNum; contributionNo ++) {
                Global.newContributionLst(contributionNo);
        
                for (int i = 0; i < Math.pow(2, Global.N); i ++) {

                    int num = i;
                    for (int j = Global.N-1; j >= 0; j --) {
                        int digit = num/(int)Math.pow(2, j);
                        oneFirm.featureLst[Global.N-1-j] = digit;
                        num -= digit * Math.pow(2, j);
                    }
                    //debug //System.out.println(Arrays.toString(oneFirm.featureLst));
                    double value = oneFirm.calcWTP();
                    if (value > max[interactionNo*contributionNum+contributionNo])
                        max[interactionNo*contributionNum+contributionNo] = value;
                    if (value < min[interactionNo*contributionNum+contributionNo])
                        min[interactionNo*contributionNum+contributionNo] = value;
                }

            }
        }
        //System.out.println(Arrays.toString(max) + ", "+ Arrays.toString(min));
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("MaxMin.csv", true));
            String max_str = Arrays.toString(max), min_str = Arrays.toString(min);
            bw.write("Max:\n" + max_str.substring(1, max_str.length()-1)+ "\n");
            bw.write("Min:\n" + min_str.substring(1, min_str.length()-1) + "\n");
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
