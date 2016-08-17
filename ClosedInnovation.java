import java.util.*;
//import java.util.stream.*;
import java.io.*;

public class ClosedInnovation {

    public  double[] performanceList = new double[Global.contributionNum * Global.interactionNum];
    public String FileName = "Closed_Data";
    
    public void begin () {
        int N = Global.N, K = Global.K, firmsNum = Global.firmsNum;
        int contributionNum = Global.contributionNum, interactionNum = Global.interactionNum;
        for (int interactionNo = 0; interactionNo < interactionNum; interactionNo ++) {
            Global.newInteractLst(interactionNo);
            for (int contributionNo = 0; contributionNo < contributionNum; contributionNo ++) {
                Global.newContributionLst(contributionNo);
                //if (landscapeNo%10 == 0)//debug
                //System.out.println("landscape "+landscapeNo + " begins.");

                Firms[] closedFirms = new Firms [firmsNum];
                //initial firms
                for (int i = 0; i < firmsNum; i ++) {
                    closedFirms[i] = new Firms();
                    closedFirms[i].initial();
                }
                //System.out.println(closedFirms[0].WTP); //debug

                /* debug
                for (int i = 0; i < N; i ++) {
                    for (int j = 0; j < K; j ++) {
                        System.out.print(intrctList[i][j] + " " );
                    }
                    System.out.println("\n");

                    if (i == 0) {
                        int i = 0;
                        for (int j = 0; j < (Math.pow(2, K+1)); j ++) {
                            System.out.print(contributionLst[i][j] + " ");
                        }
                        System.out.println("\n");
                    }
                }

                System.out.println(Arrays.toString(closedFirms[0].featureLst));
                closedFirms[0].revertBit(0);
                System.out.println(Arrays.toString(closedFirms[0].featureLst));
            */

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
                performanceList[interactionNo*contributionNum+contributionNo] = performanceSum/firmsNum;
            }
        }
        //System.out.println(Arrays.toString(performanceList));
        
        //File f = new File(FileName);
        //if (f.exists() && !f.isDirectory()) {
        writeResult(FileName);
    }
    
    public void writeResult(String FileName) {
        String result = Arrays.toString(performanceList);
        /*int length = Global.contributionNum * Global.interactionNum;
          double result = 0;
          for (int i = 0; i < length; i ++)
          result += performanceList[i];
          result /= length;*/
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FileName + ".csv", true));
            bw.write("K = "+ Global.K + "\n");
            bw.write(result.substring(1, result.length()-1) + "\n");
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
