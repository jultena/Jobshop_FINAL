import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private Integer b =3;

    public static void main(String[] args) throws FileNotFoundException {
        Main launch = new Main();
        launch.go();
    }



    private void go() throws FileNotFoundException {

        final File file = new File("./TextData/Monaldo/Fjsp/Job_Data/Brandimarte_Data/Text/Mk04.fjs");


        Scanner sc = new Scanner(file);

        Integer nbJob=sc.nextInt();

        Integer nbTotMach=sc.nextInt();

        Integer nbMachAct=sc.nextInt();

        Jobs tabJob = new Jobs(nbTotMach) ;
        for (Integer x = 1;x <= nbJob; x++){

            sc.nextLine();
            Job y = new Job("Job"+x);
            //on vient prendre le premier element de la ligne du job x qui correspond au nb d'activités
             Integer nbActivity = sc.nextInt() ;

            for (Integer i = 1; i<=nbActivity; i++){
                Integer idAct = x*10+i ;
                Activity A = new Activity(idAct);
                Integer nbMachPourAct = sc.nextInt();

                for (Integer j=1; j<=nbMachPourAct; j++){
                    Integer machine = sc.nextInt();
                    machine-- ;//mettre -1 si on test a partir des jeux de donnees fournis (nous afin créer un
                    Integer cout = sc.nextInt();
                    A.addMachine(machine, cout) ;
                }

                y.addActivity(A);

            }

            tabJob.addJob(y);
        }
        tabJob.printJobs();

        Algo monJoliAlgo = new Algo() ;
        ArrayList<Integer> result = new ArrayList<>() ;
        result = monJoliAlgo.testBoucle(tabJob);
        Integer meilleurResultat ;
        meilleurResultat = result.get(0) ;
        System.out.println("\n\n");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("$$$$$$$$$$$$$$$$ TABLEAU DE RESULTATS $$$$$$$$$$$$$$$$$$$$");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        for(Integer i = 1; i<result.size()+1; i++){
            if(result.get(i-1)!=(-2)) {
                if(result.get(i-1)<meilleurResultat){meilleurResultat= result.get(i-1) ;}
                System.out.println("Résultat n° " + i + " : " + result.get(i - 1));
            }
        }
        System.out.println("Meilleur Résultat : "+ meilleurResultat);


    }
}
