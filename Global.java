import java.util.*;
import java.io.*;

public class Global {
    public static int N = 16, K = 0, firmsNum = 100;

    public static int[][] intrctList = new int[N][K];
    public static double[][] contributionLst = new double[N][(int) Math.pow(2, (K+1))];

    //number of landscapes
    public static int contributionNum = 0;
    public static int interactionNum = 0;

    public Global (String param1, String param2, String param3) {

        K = Integer.parseInt(param1);
        contributionNum = Integer.parseInt(param2);
        interactionNum = Integer.parseInt(param3);
    }

    public static void newInteractLst(int rank) {
        //random 10000;
        Random rand = new Random(rank);
        for (int i = 0; i < N; i ++) {

            int[] intrctList_item = new int[K];
            Arrays.fill(intrctList_item, -1);

            // find K random int except i, then insert the array
            for (int j = 0; j < K; j ++) {
                //random select 1~N but i
                int tmp;

                //java 8 feature
                //while (Arrays.stream(intrctList_item).anyMatch(Integer.valueOf(tmp)::equals) || tmp == i) {
                int sameFlag;
                do {
                    sameFlag = 0;
                    tmp = rand.nextInt(N);
                    for (int k = 0; k < j; k ++) {
                        if (intrctList_item[k] == tmp || tmp == i) {
                            sameFlag = 1; break; }
                    }
                } while (sameFlag == 1);
                
                
                intrctList_item[j] = tmp;
            }
            Arrays.sort(intrctList_item);
            intrctList[i] = intrctList_item;
        }
    }

    public static void newContributionLst(int rank) {
        //landscape construction
        //contributionLst, find 2^(K+1)
        Random rand = new Random(rank);
        for (int i = 0; i < N; i ++) {
            
            double[] contributionLst_item = new double[(int) Math.pow(2, K+1)];
            //random
            for (int j = 0; j < (Math.pow(2, K+1)); j ++) {
                contributionLst_item[j] = rand.nextDouble();
            }
            contributionLst[i] = contributionLst_item;
        }
    }
}
