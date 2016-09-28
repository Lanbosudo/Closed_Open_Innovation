import java.util.*;
//import java.util.stream.*;
import java.io.*;

public class OpenInnovationFlexible extends OpenInnovationFixed {
    public String FileName = "Flexible_Data";

    public void begin () {
        
        int N = Global.N, K = Global.K, firmsNum = Global.firmsNum;
        int contributionNum = Global.contributionNum, interactionNum = Global.interactionNum, roundNum = Global.roundNum;
        for (int interactionNo = 0; interactionNo < interactionNum; interactionNo ++) {
            Global.newInteractLst(interactionNo);
            for (int contributionNo = 0; contributionNo < contributionNum; contributionNo ++) {
                Global.newContributionLst(contributionNo);
                
                if ((interactionNo*contributionNum + contributionNo)%10 == 0)//debug
                    System.out.println("landscape "+ (interactionNo*contributionNum + contributionNo) + " begins.");
                
                for (int roundNo = 0; roundNo < roundNum; roundNo ++) {

                //divide two groups
                Random rand = new Random();
                int alphaNum = rand.nextInt(firmsNum/2)+1;
                Firms_alpha[] openFirms_alpha = new Firms_alpha[alphaNum];
                Firms_beta[] openFirms_beta = new Firms_beta[firmsNum-alphaNum];
                //initial firms; Caution!!! "for(T ins: Array)" cannot be used here
                for (int i = 0; i < alphaNum; i ++) {
                    openFirms_alpha[i] = new Firms_alpha();
                }
                for (int i = 0; i < firmsNum-alphaNum; i ++) {
                    openFirms_beta[i] = new Firms_beta();
                }
                for (Firms_alpha openFirm_alpha: openFirms_alpha) {
                    openFirm_alpha.initial(openFirms_beta, rand.nextInt(firmsNum-alphaNum));
                }
                for (Firms_beta openFirm_beta: openFirms_beta) {
                    openFirm_beta.initial(openFirms_alpha, rand.nextInt(alphaNum));
                }

                int firmsChgd; int count = 0;
                double avg = 0;
                do {
                    firmsChgd = 0;
                    avg = 0;
                    for (Firms_alpha openFirm_alpha: openFirms_alpha) {
                        openFirm_alpha.searchPartner();
                        openFirm_alpha.search();
                        if (firmsChgd == 0 && openFirm_alpha.newPlanFnd == 1)
                            firmsChgd = 1;
                    }
                    for (Firms_beta openFirm_beta: openFirms_beta) {
                        openFirm_beta.searchPartner();
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
                        //avg += openFirm_alpha.WTP;
                    }
                    for (Firms_beta openFirm_beta: openFirms_beta) {
                        openFirm_beta.performanceCalc();
                        //avg += openFirm_beta.WTP;
                    }
                    //if (++ count % 100 == 0) //debug
                    //System.out.println(count + " loops."); //debug
                    count ++;


                } while (firmsChgd == 1 && count < 200);

                double performanceSum = 0;
                for (Firms_alpha openFirm_alpha: openFirms_alpha)
                    performanceSum += openFirm_alpha.WTP;
                for (Firms_beta openFirm_beta: openFirms_beta)
                    performanceSum += openFirm_beta.WTP;

                if (roundNo == 0)
                    performanceList[interactionNo*contributionNum+contributionNo] = 0;
                performanceList[interactionNo*contributionNum+contributionNo] += performanceSum/firmsNum;
                if ((interactionNo*contributionNum + contributionNo) == 0)
                    roundList[roundNo] = performanceSum/firmsNum;
                }
                performanceList[interactionNo*contributionNum+contributionNo] /= roundNum;
                if ((interactionNo*contributionNum + contributionNo) == 0) {
                    String result = Arrays.toString(roundList);
                    int length = roundNum;
                    try {
                        BufferedWriter bw = new BufferedWriter(new FileWriter(FileName + "_K_" + K + ".csv", true));
                        bw.write("K = "+ Global.K + "\n");
                        bw.write(result.substring(1, result.length()-1) + "\n");
                        bw.flush();
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }        
        //System.out.println(Arrays.toString(performanceList));
        writeResult(FileName);
    }
}
