import java.util.*;
//import java.util.stream.*;
import java.io.*;

public class ClosedInnovation {

    public  double[] performanceList = new double[Global.contributionNum * Global.interactionNum];
    public String FileName = "Closed_Data";
    public double[] roundList = new double[Global.roundNum];
    
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

                    Firms[] closedFirms = new Firms [firmsNum];
                    //initial firms
                    for (int i = 0; i < firmsNum; i ++) {
                        closedFirms[i] = new Firms();
                        closedFirms[i].initial();
                    }


                    int firmsChgd;
                    int count = 0; //debug
                    do {
                        firmsChgd = 0;
                        for (Firms closedFirm: closedFirms) {
                            closedFirm.search();
                            if (firmsChgd == 0 && closedFirm.newPlanFnd == 1)
                                firmsChgd = 1;
                            closedFirm.change(); closedFirm.performanceCalc();
                        }
                        //System.out.println(++count + " loops."); //debug
                    } while (firmsChgd == 1);

                    double performanceSum = 0;
                    for (Firms closedFirm: closedFirms) {
                        performanceSum += closedFirm.WTP;
                    }
                
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
        
        //File f = new File(FileName);
        //if (f.exists() && !f.isDirectory()) {
        writeResult(FileName);
    }
    
    public void writeResult(String FileName) {
        String result = Arrays.toString(performanceList);
        int length = Global.contributionNum * Global.interactionNum;
        /*
        double result = 0;
        for (int i = 0; i < length; i ++)
            result += performanceList[i];
        result /= length;
        */
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FileName + "_K_"+Global.K+ ".csv", true));
            bw.write("K = "+ Global.K + "\n");
            bw.write(result.substring(1, result.length()-1) + "\n");

            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
