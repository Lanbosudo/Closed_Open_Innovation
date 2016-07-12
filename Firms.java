import java.util.*;

public class Firms {
    public int[] featureLst = new int [ClosedInnovation.N];
    public int newPlanFnd = 0;
    public int newPlanIndctr = 0;
    public double WTP = 0;

    public void initial () { //firms iniitalisaztion: set initial feature list and flags
        Random rand = new Random();
        int N = ClosedInnovation.N;
        for (int i = 0; i < N; i ++) {
            featureLst[i] = rand.nextInt(2);
        }
        //System.out.println(Arrays.toString(featureLst)); //debug
        
        WTP = calcWTP();
        //System.out.println(WTP); //debug
    }

    public double calcWTP () {
        int N = ClosedInnovation.N; int K = ClosedInnovation.K;
        int decimalNum = 0; double contributionSum = 0;
        for (int i = 0; i < N; i ++) {

            decimalNum = bin2Dec(ClosedInnovation.intrctList[i], K, findValue(i));
            contributionSum += ClosedInnovation.contributionLst[i][decimalNum];
            
        }

        return contributionSum/N;
    }

    //utility functions: findValue, bin2Dec, revertBit
    public int findValue (int i) {
        /*     set value item num featrList*/
        return featureLst[i];
    }
    public int bin2Dec (int[] intrctList_item, int K, int currentFtr) {
        int returnVl = 0;
        for (int i = 0; i < K; i ++)
            returnVl += findValue(intrctList_item[i]) * (int) Math.pow(2, i);
        return returnVl + currentFtr * (int) Math.pow(2, K);
    }
    public void revertBit(int i) {
        if (featureLst[i] == 1) featureLst[i] = 0;
        else featureLst[i] = 1;
    }
    //utility functions end

    public void search () {
        for (int i = 0; i < ClosedInnovation.N; i++) {
            //revert one feature and compare WTP, then set flags
            revertBit(i);
            double oldWTP = WTP;
            WTP = calcWTP();
            if (WTP > oldWTP) {
                newPlanFnd = 1; newPlanIndctr = i;
            }
            else
                WTP = oldWTP;
            revertBit(i);
        }
    }
    public void change () {
        if (newPlanFnd == 1) {
            revertBit(newPlanIndctr);
            newPlanFnd = 0;
        }
    } //change feature if flaged
    public void performanceCalc () {
        WTP = calcWTP();
    } //recalculate
}
