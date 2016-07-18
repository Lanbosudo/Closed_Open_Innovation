import java.util.*;

public class Firms_alpha extends Firms {
    public int[] featureLst = new int [Global.N/2];
    public Firms_beta partner;
    public Firms_beta[] partnerLst;

    public void initial(Firms_beta[] openFirms_beta, int partnerNum) {
        Random rand = new Random();
        int N = Global.N;
        for (int i = 0; i < N/2; i ++) {
            featureLst[i] = rand.nextInt(2);
        }
        //System.out.println(Arrays.toString(featureLst)); //debug
        
        partnerLst = openFirms_beta;
        partner = partnerLst[partnerNum];
        //System.out.println(Arrays.toString(partner.featureLst)); //debug
        WTP = calcWTP();
    }

    /**************************chnge all closed Class to Global class*/
    public double calcWTP () {
        int N = Global.N; int K = Global.K;
        int decimalNum = 0; double contributionSum = 0;
        for (int i = 0; i < N; i ++) {

            decimalNum = bin2Dec(Global.intrctList[i], K, findValue(i));
            contributionSum += Global.contributionLst[i][decimalNum];
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
        for (int i = 0; i < Global.N/2; i++) {
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
