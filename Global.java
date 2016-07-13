public class Global {
    public static int N = 16, K, firmsNum = 100;

    public  static int[][] intrctList = new int[N][K];
    public  static double[][] contributionLst = new double[N][(int) Math.pow(2, (K+1))];
    public  static int landscapeNum = 50;

    public Global () {
        System.out.println("input K:");
        Scanner reader = new Scanner(System.in);
        K = reader.nextInt();
        
        newInteractLst();
    }

    public static void newInteractLst() {
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
    }

    public static void newContributionLst(int landscapeNo) {
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
    }
}
