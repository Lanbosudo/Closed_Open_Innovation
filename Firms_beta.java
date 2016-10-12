import java.util.*;

public class Firms_beta extends Firms_alpha {
    public Firms_alpha partner;
    public Firms_alpha[] partnerLst;

    public void initial(Firms_alpha[] openFirms_alpha, int partnerNum) {

        //System.out.println(Arrays.toString(featureLst)); //debug

        partnerLst = openFirms_alpha;
        partner = partnerLst[partnerNum];
        WTP = calcWTP();
    }

    public int findValue (int i) {
        return i >= Global.N/2? featureLst[i-Global.N/2]: partner.featureLst[i];
    }
    
    public void searchPartner() {
        Firms_alpha oldPartner = partner;
        double oldWTP = WTP;
        for (Firms_alpha newPartner: partnerLst) {
            partner = newPartner; WTP = calcWTP();
            if (oldWTP >= WTP) { partner = oldPartner; WTP = oldWTP;}

            //bug
            oldPartner = partner; oldWTP = WTP;
        }
    }
}
