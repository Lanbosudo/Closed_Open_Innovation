import java.util.*;

public class Main {
    public static void main (String[] args) {

        System.out.println("select mode: (1)Closed (2)Open Fixed (3)Open Flexible");
        Scanner reader = new Scanner(System.in);
        int mode = reader.nextInt();
        
        if (mode == 1) {
            ClosedInnovation closedInnovation = new ClosedInnovation();
            //closedInnovation.initial();
            closedInnovation.begin();
        }

        else if (mode == 2) {
            OpenInnovationFixed openInnovationFixed = new OpenInnovationFixed();
            //openInnovationFixed.initial();
            openInnovationFixed.begin();
        }

        else if (mode == 3) {
            OpenInnovationFlexible openInnovationFlexible = new OpenInnovationFlexible();
            //openInnovationFlexible.initial();
            openInnovationFlexible.begin();
        }
    }

}
