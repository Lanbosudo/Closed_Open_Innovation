import java.util.*;
//import java.util.stream.*;
import java.io.*;

public class ClosedInnovation {

    public double[] performanceList = new double[Global.contributionNum * Global.interactionNum];
    public String FileName = "Closed_Data";
    public double ratio = 0.5;
    
    public void begin () {
        int N = Global.N, K = Global.K, firmsNum = Global.firmsNum;
        int contributionNum = Global.contributionNum, interactionNum = Global.interactionNum;
        for (int interactionNo = 0; interactionNo < interactionNum; interactionNo ++) {
            Global.newInteractLst(interactionNo);
            for (int contributionNo = 0; contributionNo < contributionNum; contributionNo ++) {
                Global.newContributionLst(contributionNo);
                if ((interactionNo*contributionNum + contributionNo)%10 == 0)//debug
                    System.out.println("landscape "+ (interactionNo*contributionNum + contributionNo) + " begins.");

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

                //for each landscape, only need top $ratio firms
                double performanceSum = 0;
                double []performanceArray = new double [firmsNum];

                for (int i_firm = 0; i_firm < firmsNum; i_firm++) {
                    performanceArray[i_firm] = closedFirms[i_firm].WTP;
                }
                Arrays.sort(performanceArray);
                for (int i_sum = (int) (firmsNum * ratio)-1; i_sum >= 0; i_sum--) {
                    performanceSum += performanceArray[firmsNum-1 - i_sum];
                }
                
                performanceList[interactionNo*contributionNum+contributionNo] = performanceSum/((int)(firmsNum*ratio));
            }
        }
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
