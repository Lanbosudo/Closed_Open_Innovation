import java.util.*;
import java.util.stream.*;
import java.io.*;

public class ClosedInnovation {

    /***********move to Global*/
    public static int N = 16, K, firmsNum = 100;

    public  static int[][] intrctList = new int[N][K];
    public  static double[][] contributionLst = new double[N][(int) Math.pow(2, (K+1))];
    public  int landscapeNum = 50;
    /*end*/
    public  double[] performanceList = new double[landscapeNum];

    public String FileName = "Closed_Data";
    
    public ClosedInnovation() {

        /*************move to Global*/
        System.out.println("input K:");
        Scanner reader = new Scanner(System.in);
        K = reader.nextInt();
        
        //set up the two mapping matrixes; one below, the other in begin()

        //random 10000;
        Random rand = new Random(10000);
        for (int i = 0; i < N; i ++) {

            int[] intrctList_item = new int[K];
            Arrays.fill(intrctList_item, -1);

            // find K random int except i, then insert the array
            for (int j = 0; j < K; j ++) {
                //random select 1~N but j
                int tmp = rand.nextInt(N);
                final int tmp_ = tmp;
                while (Arrays.stream(intrctList_item).anyMatch(Integer.valueOf(tmp)::equals) || tmp == i) {
                    tmp = rand.nextInt(N);
                }
                intrctList_item[j] = tmp;
            }
            Arrays.sort(intrctList_item);
            intrctList[i] = intrctList_item;
        }
        //System.out.println(Arrays.toString(intrctList[0])); //debug
        /*end*/
    }

    public void begin () {

        for (int landscapeNo = 0; landscapeNo < landscapeNum; landscapeNo++) {

            //if (landscapeNo%10 == 0)//debug
                //System.out.println("landscape "+landscapeNo + " begins.");

            /********************************************************/
            //landscape construction
            //contributionLst, find 2^(K+1)
            //Change to Global method
            Random rand = new Random(landscapeNo);
            for (int i = 0; i < N; i ++) {
            
                double[] contributionLst_item = new double[(int) Math.pow(2, K+1)];
                //random
                for (int j = 0; j < (Math.pow(2, K+1)); j ++) {
                    contributionLst_item[j] = rand.nextDouble();
                }
                contributionLst[i] = contributionLst_item;
            }
            /**********************************************************/
            //should be extracted and become an indivdual method to be inherited

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
            performanceList[landscapeNo] = performanceSum/firmsNum;
        }
        //System.out.println(Arrays.toString(performanceList));
        
        //File f = new File(FileName);
        //if (f.exists() && !f.isDirectory()) {
        writeResult(FileName);
    }
    
    public void writeResult(String FileName) {
        String result = Arrays.toString(performanceList);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FileName + ".csv", true));
            bw.write("K = "+ K + "\n");
            bw.write(result.substring(1, result.length()-1) + "\n");
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
