import java.util.*;

public class Main {
    public static void main (String[] args) {

        /*System.out.println("select mode: (1)Closed (2)Open Fixed (3)Open Flexible");
        Scanner reader = new Scanner(System.in);
        int mode = reader.nextInt();*/
	int mode = Integer.parseInt(args[0]);

        Global global = new Global(args[1], args[2], args[3]);
        if (mode == 1) {
            ClosedInnovation closedInnovation = new ClosedInnovation();
            closedInnovation.begin();
        }

        else if (mode == 2) {
            OpenInnovationFixed openInnovationFixed = new OpenInnovationFixed();
            openInnovationFixed.begin();
        }

        else if (mode == 3) {
            OpenInnovationFlexible openInnovationFlexible = new OpenInnovationFlexible();
            openInnovationFlexible.begin();
        }

    }

}
