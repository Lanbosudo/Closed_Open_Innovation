import java.util.*;

public class Firms_beta extends Firms_alpha {
    public Firms_alpha partner;
    public Firms_alpha[] partnerLst;

    /*
    public void initial(Firms_alpha randomPrtnr, double[][] contributionLst_out) {
        Random rand = new Random();
        int N = ClosedInnovation.N;
        for (int i = 0; i < N/2; i ++) {
            featureLst[i] = rand.nextInt(2);
        }
        //System.out.println(Arrays.toString(featureLst)); //debug

        partner = randomPrtnr;
        contributionLst = contributionLst_out;
        WTP = calcWTP();
    }
    */
    public void initial(Firms_alpha[] openFirms_alpha, int partnerNum, double[][] contributionLst_out) {
        Random rand = new Random();
        int N = ClosedInnovation.N;
        for (int i = 0; i < N/2; i ++) {
            featureLst[i] = rand.nextInt(2);
        }
        //System.out.println(Arrays.toString(featureLst)); //debug

        partnerLst = openFirms_alpha;
        partner = partnerLst[partnerNum];
        contributionLst = contributionLst_out;
        WTP = calcWTP();
    }

    public int findValue (int i) {
        return i >= 8? featureLst[i-8]: partner.featureLst[i];
    }
    
    public void searchPartner() {
        Firms_alpha oldPartner = partner;
        for (Firms_alpha newPartner: partnerLst) {
            partner = newPartner; double newWTP = calcWTP();
            if (WTP >= newWTP) { partner = oldPartner;}
        }
    }
}
