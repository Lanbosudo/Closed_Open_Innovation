import java.util.*;
import java.util.stream.*;

public class OpenInnovationFixed extends ClosedInnovation {
    public String FileName = "Fixed_Data";
    
    public void begin () {

        for (int landscapeNo = 0; landscapeNo < landscapeNum; landscapeNo++) {

            if (landscapeNo%10 == 0)//debug
                System.out.println("landscape "+landscapeNo + " begins.");

            /*************Global*/
            //landscape construction
            //contributionLst, find 2^(K+1)
            Random rand = new Random(landscapeNo);
            for (int i = 0; i < N; i ++) {
            
                double[] contributionLst_item = new double[(int) Math.pow(2, K+1)];
                //random
                for (int j = 0; j < (Math.pow(2, K+1)); j ++) {
                    contributionLst_item[j] = rand.nextDouble();
                }
                contributionLst[i] = contributionLst_item;
            }
            /*end*/

            //divide two groups
            rand = new Random();
            int alphaNum = 0;
            for (int count = 0; count < firmsNum; count ++) {
                if (rand.nextInt(2) == 1) alphaNum ++;
            }
            //System.out.println("alphaNum: " + alphaNum); //debug
            Firms_alpha[] openFirms_alpha = new Firms_alpha[alphaNum];
            Firms_beta[] openFirms_beta = new Firms_beta[firmsNum-alphaNum];
            //initial firms; Caution!!!
            for (int i = 0; i < alphaNum; i ++) {
                openFirms_alpha[i] = new Firms_alpha();
            }
            for (int i = 0; i < firmsNum-alphaNum; i ++) {
                openFirms_beta[i] = new Firms_beta();
            }

            /***************no need to give contribution matrix*/
            for (Firms_alpha openFirm_alpha: openFirms_alpha) {
                openFirm_alpha.initial(openFirms_beta, rand.nextInt(firmsNum-alphaNum), contributionLst);
            }
            
            for (Firms_beta openFirm_beta: openFirms_beta) {
                openFirm_beta.initial(openFirms_alpha, rand.nextInt(alphaNum), contributionLst);
            }
            /*end*/

            // new method
            for (Firms_alpha openFirm_alpha: openFirms_alpha) {
                openFirm_alpha.searchPartner(); //add for comparison
            }
            for (Firms_beta openFirm_beta: openFirms_beta) {
                openFirm_beta.searchPartner(); //add for comparison
            }

            /*
            System.out.println("init: "+openFirms_alpha[0].WTP); //debug
            System.out.println("featureLst: "+Arrays.toString(openFirms_alpha[0].featureLst)); //debug
            if (openFirms_alpha[0].featureLst[0] == 1) openFirms_alpha[0].featureLst[0] = 0;
            else openFirms_alpha[0].featureLst[0] = 1;
            openFirms_alpha[0].revertBit(0);
            System.out.println("featureLst: "+Arrays.toString(openFirms_alpha[0].featureLst)); //debug
            System.out.println("change 1 feature:" + openFirms_alpha[0].calcWTP());
            openFirms_alpha[0].revertBit(0); //debug
            System.out.println("change 1 feature:" + openFirms_alpha[0].calcWTP());
            System.out.println("featureLst: "+Arrays.toString(openFirms_alpha[0].featureLst)); //debug
            System.out.println("3 contributionLst: " + ClosedInnovation.contributionLst[0][0] + ' '+OpenInnovationFixed.contributionLst[0][0] + ' '+OpenInnovationFlexible.contributionLst[0][0]);*/

            int firmsChgd; int count = 0;
            do {
                firmsChgd = 0;
                for (Firms_alpha openFirm_alpha: openFirms_alpha) {
                    openFirm_alpha.search();
                    if (firmsChgd == 0 && openFirm_alpha.newPlanFnd == 1)
                        firmsChgd = 1;
                }
                //System.out.println("before partner: "+openFirms_alpha[0].WTP); //debug
                for (Firms_beta openFirm_beta: openFirms_beta) {
                    openFirm_beta.search();
                    if (firmsChgd == 0 && openFirm_beta.newPlanFnd == 1)
                        firmsChgd = 1;
                }

                for (Firms_alpha openFirm_alpha: openFirms_alpha) {
                    openFirm_alpha.change();
                }
                for (Firms_beta openFirm_beta: openFirms_beta) {
                    openFirm_beta.change();
                }
                for (Firms_alpha openFirm_alpha: openFirms_alpha) {
                    openFirm_alpha.performanceCalc();
                }
                //System.out.println("after: "+openFirms_alpha[0].WTP); //debug
                for (Firms_beta openFirm_beta: openFirms_beta) {
                    openFirm_beta.performanceCalc();
                }
                //if (++ count % 100 == 0) //debug
                //System.out.println(++count + " loops."); //debug
                count ++;
            } while (firmsChgd == 1 && count < 200);

            double performanceSum = 0;
            for (Firms_alpha openFirm_alpha: openFirms_alpha)
                performanceSum += openFirm_alpha.WTP;
            for (Firms_beta openFirm_beta: openFirms_beta)
                performanceSum += openFirm_beta.WTP;
            performanceList[landscapeNo] = performanceSum/firmsNum;
        }
        //System.out.println(Arrays.toString(performanceList));
        writeResult(FileName);
    }
}
