import java.util.*;

public class Firms_alpha extends Firms {
    public int[] featureLst = new int [ClosedInnovation.N/2];
    public Firms_beta partner;
    public Firms_beta[] partnerLst;
    public double contributionLst[][];

    /*
    public void initial(Firms_beta randomPrtnr, double[][] contributionLst_out) {
        Random rand = new Random();
        int N = ClosedInnovation.N;
        for (int i = 0; i < N/2; i ++) {
            featureLst[i] = rand.nextInt(2);
        }
        //System.out.println(Arrays.toString(featureLst)); //debug
        
        partner = randomPrtnr;
        contributionLst = contributionLst_out;
        //System.out.println(Arrays.toString(partner.featureLst)); //debug
        WTP = calcWTP();
    }
    */

    /**********remove contributionLst_out*/
    public void initial(Firms_beta[] openFirms_beta, int partnerNum, double[][] contributionLst_out) {
        Random rand = new Random();
        int N = ClosedInnovation.N;
        for (int i = 0; i < N/2; i ++) {
            featureLst[i] = rand.nextInt(2);
        }
        //System.out.println(Arrays.toString(featureLst)); //debug
        
        partnerLst = openFirms_beta;
        partner = partnerLst[partnerNum];
        contributionLst = contributionLst_out;
        //System.out.println(Arrays.toString(partner.featureLst)); //debug
        WTP = calcWTP();
    }

    /**************************chnge all closed Class to Global class*/
    public double calcWTP () {
        int N = ClosedInnovation.N; int K = ClosedInnovation.K;
        int decimalNum = 0; double contributionSum = 0;
        for (int i = 0; i < N; i ++) {

            decimalNum = bin2Dec(ClosedInnovation.intrctList[i], K, findValue(i));
            contributionSum += contributionLst[i][decimalNum];
        }
        return contributionSum/N;
    }

    public int findValue (int i) {
        return i < 8? featureLst[i]: partner.featureLst[i-8];
    }
    public void revertBit(int i) {
        if (featureLst[i] == 1) featureLst[i] = 0;
        else featureLst[i] = 1;
    }
    
    public void search () {
        for (int i = 0; i < ClosedInnovation.N/2; i++) {
            //revert one feature and compare WTP, then set flags
            revertBit(i);
            double oldWTP = WTP;
            WTP = calcWTP();
            if (WTP > oldWTP) {
                newPlanFnd = 1; newPlanIndctr = i;
            } else
                WTP = oldWTP;
            revertBit(i);
        }
    }

    public void searchPartner() {
        Firms_beta oldPartner = partner;
        for (Firms_beta newPartner: partnerLst) {
            partner = newPartner; double newWTP = calcWTP();
            if (WTP >= newWTP) { partner = oldPartner;}
        }
    }
}
