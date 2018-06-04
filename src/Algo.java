import java.util.ArrayList;
import java.util.Random;
import java.util.Collections ;

public class Algo {

    private Graphe graphe;
    //private Choix choix;
    private Integer randInit ;
    private Integer randType_Choix ;
    private Integer randM_Machine ;
    private Integer randM_Activity ;
    private Integer randM_Activity2 ;
    private Integer randA_Machine ;
    private Integer randA_Activity ;


    public Algo(){
        this.graphe = new Graphe() ;
        this.randInit = 0 ;
        this.randType_Choix = 0 ;
        this.randM_Machine = 0 ;
        this.randM_Activity = 0 ;
        this.randM_Activity2 = 0 ;
        this.randA_Machine = 0 ;
        this.randA_Activity = 0 ;

    }

    public Choix copierChoix(Choix choixACopier){
        Choix retour = new Choix() ;
        for(Attribution a : choixACopier.getA()){
            Integer id_act = a.getActivite().getId() ;
            ArrayList<Integer> machines = new ArrayList<>() ;
            for(Integer i = 0; i< a.getActivite().getMachines().size() ; i++){
                Integer tmp = a.getActivite().getMachines().get(i);
                machines.add(tmp) ;
            }

            ArrayList<Integer> costs = new ArrayList<>() ;
            for(Integer i = 0; i< a.getActivite().getCosts().size() ; i++){
                Integer tmp2 = a.getActivite().getCosts().get(i);
                costs.add(tmp2) ;
            }
            Integer nbmach = a.getActivite().getNbmach() ;
            Activity newAct = new Activity(id_act) ;
            for(Integer i = 0 ; i< machines.size() ; i++){
                newAct.addMachine(machines.get(i), costs.get(i));
            }
            Integer machineAttr = a.getMachine() ;
            Integer machineCout = a.getCout() ;
            Attribution newAttr = new Attribution(newAct, machineAttr, machineCout) ;
            retour.addAttribution(newAttr);

        }

        for(Ordre o : choixACopier.getM()){
           Integer machine = o.getMachine() ;
           Ordre newOrdre = new Ordre(machine) ;
           ArrayList<Integer> newClass = new ArrayList<>() ;
           for(Integer i = 0; i< o.getClassement().size() ; i++){
               Integer tmp3 = o.getClassement().get(i);
               newOrdre.addClassement(tmp3); ;
           }

           retour.addOrdre(newOrdre);
        }

        return retour ;
    }




    private void creationGraphe(Jobs jobs){
        Sommet sommetDebut = new Sommet(0 , 0, (-1),0) ;
        graphe.addSommet(sommetDebut);
        Integer compteur = 1;
        for(Job j: jobs.getJobs()){
            for(Activity a : j.getActivities()){
                Sommet s = new Sommet(compteur,(-1), (-1), a.getId());
                graphe.addSommet(s);
                compteur++;
            }
        }
        Sommet sommetFin = new Sommet(compteur, 0, (-1), 10000);
        graphe.addSommet(sommetFin);
        graphe.afficherSommets() ;
    }

    private void creationArcs(Jobs jobs) {
        Integer nb;
        Integer cptj = 1;
        Integer cpts = 1;
        for (Job j : jobs.getJobs()) {
            nb = j.getNbActivities();
            Arc arcDebut = new Arc(graphe.getSommets().get(0).getDate(), graphe.getSommets().get(0), graphe.getSommets().get(cptj), false);
            graphe.addArc(arcDebut);
            System.out.println("Création de l'arc avec comme source" + arcDebut.getSource().getIdActivity() + " et comme dest" + arcDebut.getDest().getIdActivity());

            for (Integer i = 1; i < nb; i++) {
                Arc a = new Arc(graphe.getSommets().get(cpts).getDate(), graphe.getSommets().get(cpts), graphe.getSommets().get(cpts + 1), false);
                graphe.addArc(a);
                System.out.println("Création de l'arc avec comme source" + a.getSource().getIdActivity() + " et comme dest" + a.getDest().getIdActivity());

                cpts++;
            }
            Arc arcFin = new Arc(graphe.getSommets().get(cpts).getDate(), graphe.getSommets().get(cpts), graphe.getSommetWithId(graphe.getSommets().size()-1), false);
            graphe.addArc(arcFin);
            System.out.println("Création de l'arc avec comme source" + arcFin.getSource().getId() + " et comme dest" + arcFin.getDest().getId());
            cpts++ ;
            cptj=cpts ;
        }
    }

    /* Trouve un ID sommet en fonction de l'activité qu'il execute*/
    private Integer findSommet(Integer activity){
        Sommet retour = null;
        for(Sommet s : graphe.getSommets()){
            if(s.getIdActivity()==activity){
                retour = s ;
            }
        }
        if(retour==null){
            System.out.println("Problème dans le recherche d'un sommet lors de la mise a jour dans les choix");
        }
        return retour.getId() ;
    }

    /* Prend un tableau d'attributions et modifie les sommets et les arcs fixes en leur ajoutant des valeurs*/
    private void modifSommetArcWithAttribution(ArrayList<Attribution> attributions){
        for(Attribution attr : attributions){

            /*Mise à jour Sommet*/
            graphe.getSommets().get(findSommet(attr.getActivite().getId())).setDate(attr.getCout());
            graphe.getSommets().get(findSommet(attr.getActivite().getId())).setMachine(attr.getMachine());
            /*Mise à jour Arc*/
            graphe.findArcFixeWithSource(graphe.getSommets().get(findSommet(attr.getActivite().getId()))).setCost(attr.getCout());
            //System.out.println("On modifie l'arc qui a pour source : "+ graphe.getSommets().get(findSommet(attr.getActivite().getId()))+ " et on lui attribue le cout : " +attr.getCout());
        }

    }

    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        //
        // In particular, do NOT do 'Random rand = new Random()' here or you
        // will get not very good / not very random results.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    private void modifArcWithOrdre(ArrayList<Ordre> ordres){

        for(Ordre o : ordres){
            Integer classSize = o.getClassement().size() ;
            Integer i;
            for(i=0; i<(classSize-1) ; i++){
                Integer cout = graphe.getSommets().get(o.getClassement().get(i)).getDate() ;
                Sommet source = graphe.getSommets().get(o.getClassement().get(i)) ;
                Sommet dest = graphe.getSommets().get(o.getClassement().get(i+1)) ;
                Arc a = new Arc(cout ,source ,dest,true ) ;
                graphe.addArc(a);
            }
        }

    }



    private Choix initChoix(Jobs jobs){
        /* Partie A des choix, attribution activité/machine    */
        /* Modification des valeurs des sommets */
        Choix choixRetour = new Choix() ;

        Integer compteur = 1;
        for(Job j : jobs.getJobs()){
            for(Activity a : j.getActivities()){
                /*Générer un nb random */
                randInit = randInt(0, a.getNbmach()-1);
                /*On retrouve le coût*/
                Integer cost = a.getCosts().get(randInit) ;
                Attribution attribution = new Attribution(a, a.getMachines().get(randInit), cost) ;
                choixRetour.addAttribution(attribution);
            }
        }

        modifSommetArcWithAttribution(choixRetour.getA());

        /* Partie M des choix, ordre */
        Integer m ;
        for(m=0; m <jobs.getNbTotMach(); m++){
            Ordre ordre = new Ordre(m);
            for(Sommet sommet : graphe.getSommets()){
                if(sommet.getMachine()==m){
                    ordre.addClassement(sommet.getId());

                }
            }
            choixRetour.addOrdre(ordre);
            for(Integer s : ordre.getClassement()){

            }
        }

        modifArcWithOrdre(choixRetour.getM());
        return choixRetour ;

    }

    private Choix modifChoix(Choix ancienChoix ,Jobs jobs){
        Choix newChoix = new Choix() ;
        newChoix = copierChoix(ancienChoix);

        randType_Choix = randInt(0, 1) ;
        //SI le random est à 0 on fait une modif de M
        if(randType_Choix==0){


            randM_Machine = randInt(0, (jobs.getNbTotMach())-1) ;

            //System.out.println("taille du classement qu'on modifie : "+ newChoix.getM().get(randM_Machine).getClassement().size());
            if(newChoix.getM().get(randM_Machine).getClassement().size() > 1) {

                //System.out.println("ON SWAP L'ACTIVITE : " + activity + " ET L'ACTIVITE : " + activity2 + "DE LA MACHINE : " + randM_Machine);
                randM_Activity = randInt(0, newChoix.getM().get(randM_Machine).getClassement().size() - 1);
                Integer activity = newChoix.getM().get(randM_Machine).getClassement().get(randM_Activity) ;
                Integer activity2 = 0 ;

                if(randM_Activity==0){
                    randM_Activity2 = randM_Activity+1 ;
                    activity2 = newChoix.getM().get(randM_Machine).getClassement().get(randM_Activity2) ;
                }
                else if(randM_Activity==(newChoix.getM().get(randM_Machine).getClassement().size() - 1) ){
                    randM_Activity2 = randM_Activity -1 ;
                    activity2 = newChoix.getM().get(randM_Machine).getClassement().get(randM_Activity2) ;
                }
                else{
                    randM_Activity2 = randInt(0, 1);
                    if(randM_Activity2==1){
                        randM_Activity2 = randM_Activity+1 ;
                        activity2 = newChoix.getM().get(randM_Machine).getClassement().get(randM_Activity2) ;
                    }
                    else{
                        randM_Activity2 = randM_Activity -1 ;
                        activity2 = newChoix.getM().get(randM_Machine).getClassement().get(randM_Activity2) ;
                    }
                }
                //System.out.println("ON SWAP L'ACTIVITE : " + activity + " ET L'ACTIVITE : " + activity2 + "DE LA MACHINE : " + randM_Machine);
                newChoix.getM().get(randM_Machine).getClassement().set(randM_Activity, activity2);
                newChoix.getM().get(randM_Machine).getClassement().set(randM_Activity2, activity);

                this.graphe.delArcMobile();
                modifSommetArcWithAttribution(newChoix.getA());
                modifArcWithOrdre(newChoix.getM());
            }
        }
        //sinon on fait une modif de A
        else{

            this.randA_Activity = randInt(0, newChoix.getA().size()-1);
            Activity activity = newChoix.getA().get(randA_Activity).getActivite() ;
            Integer oldMachine = newChoix.getA().get(randA_Activity).getMachine() ;

            this.randA_Machine = randInt(0, activity.getNbmach()-1);
            Integer newMachine = activity.getMachines().get(randA_Machine) ;
            //System.out.println("MODIF ACTIVITY " + activity.getId() + " DE LA MACHINE  " + oldMachine + " VERS LA  " + newMachine);
            if(oldMachine != newMachine){
                newChoix.getA().get(randA_Activity).setMachine(newMachine);
                newChoix.getA().get(randA_Activity).setCout(activity.getCosts().get(randA_Machine));
                Integer index = newChoix.getM().get(oldMachine).getIndexWithActivity(graphe.getSommetWithIdActivity(activity.getId()).getId()) ;
                newChoix.getM().get(oldMachine).delClassement(graphe.getSommetWithIdActivity(activity.getId()).getId());
                newChoix.getM().get(newMachine).addClassement(graphe.getSommetWithIdActivity(activity.getId()).getId());
                this.graphe.delArcMobile();

                modifSommetArcWithAttribution(newChoix.getA());
                modifArcWithOrdre(newChoix.getM());
            }


        }

        return newChoix ;
    }

    public Integer testFonctionnement(Jobs jobs){
        creationGraphe(jobs);
        creationArcs(jobs);
        initChoix(jobs);
        Integer i = 0 ;
        Integer res = (-1);
        for(i=0 ; i<graphe.getSommets().size(); i++) {
            Sommet sommetStart = this.graphe.getSommetWithId(i);
            res = this.graphe.Cmax(sommetStart);
            System.out.println("Cout du Sommet " + i + " : "+ res);
        }
        return res ;
    }

    public ArrayList<Integer> runAlgo(Jobs jobs){
        creationGraphe(jobs);
        creationArcs(jobs);
        ArrayList<Integer> res = testBoucle(jobs) ;
        return res ;
    }

    public ArrayList<Integer> testBoucle(Jobs jobs){
        ArrayList<Integer> tabResultat = new ArrayList<>() ;
        Integer bestResultat = (-1);
        Integer newResultat = (-1);
        Choix bestChoix ;
        Choix nouveauChoix ;
        creationGraphe(jobs);
        creationArcs(jobs);
        Sommet lastSommet = this.graphe.getSommetWithId(graphe.getSommets().size()-1) ;
        bestChoix = initChoix(jobs);
        bestResultat = this.graphe.Cmax(lastSommet) ;

        Integer reset = 0 ;
        Integer plateau = 0 ;
        Integer i = 0 ;
        while(reset <500){
            i++ ;
            if(plateau==10){
                tabResultat.add(bestResultat) ;
                bestResultat = (-1);
                newResultat = (-1);
                reset ++ ;
                plateau = 0 ;
                System.out.println("###################################################################");
                System.out.println("######################CECI EST UN RESET############################");
                System.out.println("###################################################################");
                bestChoix = initChoix(jobs);
                this.graphe.resetCouts();
                bestResultat = this.graphe.Cmax(lastSommet) ;
                nouveauChoix = copierChoix(bestChoix) ;
                nouveauChoix = modifChoix(nouveauChoix ,jobs);
                this.graphe.resetCouts();
                newResultat = this.graphe.Cmax(lastSommet) ;
                System.out.println("Itération n° " + i + " : Meilleur résultat : "+ bestResultat+ " Nouveau résultat : " + newResultat);
                if((newResultat< bestResultat)&& (newResultat!= (-2))){
                    bestResultat = newResultat ;
                    bestChoix = copierChoix(nouveauChoix) ;
                    plateau = 0 ;
                }
                else{
                    plateau ++ ;
                }

            }
            else{


                nouveauChoix = copierChoix(bestChoix) ;
                nouveauChoix = modifChoix(nouveauChoix ,jobs);



                this.graphe.resetCouts();
                newResultat = this.graphe.Cmax(lastSommet) ;
                System.out.println("Itération n° " + i + " : Meilleur résultat : "+ bestResultat+ " Nouveau résultat : " + newResultat);
                if((newResultat< bestResultat)&& (newResultat!= (-2))){
                    bestResultat = newResultat ;
                    bestChoix = copierChoix(nouveauChoix) ;
                    plateau = 0 ;
                }
                else{
                    plateau ++ ;
                }



            }
        }
        return tabResultat ;

    }
}
