/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package combinations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author H. KASSIMI
 */
public class Combinations {
    static Integer N1, N2, N3, HVMN1, HVMN2, HVMN3, NM, MINM, MAXM, HVMAX, PMAXM;
    static Double avg;
    static Niveau STN0, STN1, STN2, STN3;
    
    public static void main(String[] args) {
        ArrayList<ArrayList> attributions = new ArrayList();
        Predicate<ArrayList> conditionGroupes;
        ArrayList<ArrayList> result;
        Predicate<ArrayList> conditionEnseignants;

        N1 = 12;
        N2 = 8;
        N3 = 9;
        NM = 5;
        HVMN1 = 4;
        HVMN2 = 4;
        HVMN3 = 4;
        HVMAX = 24;
        PMAXM = 6;
        avg = (N1 * HVMN1 + N2 * HVMN2 + N3 * HVMN3) / (double) NM;
        MAXM = avg.intValue() - avg.intValue() % Stream.of(HVMN1, HVMN2, HVMN3).collect(Collectors.maxBy(Comparator.naturalOrder())).get() +
                Stream.of(HVMN1, HVMN2, HVMN3).collect(Collectors.maxBy(Comparator.naturalOrder())).get();
        if ( MAXM > HVMAX )
            MAXM = HVMAX;
        MINM = MAXM - Stream.of(HVMN1, HVMN2, HVMN3).collect(Collectors.maxBy(Comparator.naturalOrder())).get();
        STN0 = new Niveau("#", 0);
        STN1 = new Niveau("1A", HVMN1);
        STN2 = new Niveau("2A", HVMN2);
        STN3 = new Niveau("3A", HVMN3);
        
        conditionGroupes = (tab) -> {
            int sum = (int) tab.stream().collect(Collectors.summingInt(a -> ((Niveau) a).facteur));
            return sum >= MINM && sum <= MAXM
                    && tab.stream().map(d -> ((Niveau) d).nom).filter(l -> l != "#").distinct().count() > 1;
        };
        
        attributions.addAll(combiner(new Niveau[] { STN0, STN1, STN2 }, PMAXM, conditionGroupes));
        attributions.addAll(combiner(new Niveau[] { STN0, STN3, STN2 }, PMAXM, conditionGroupes));
        attributions.addAll(combiner(new Niveau[] { STN0, STN1, STN3 }, PMAXM, conditionGroupes));
        
        System.out.println("Trouvé : " + attributions.size());
        System.out.println(
            attributions.stream().map(
                a -> a.stream().map(
                    b -> ((Niveau) b).nom
                ).collect(Collectors.joining(" ")).toString()
            ).collect(Collectors.joining("\n"))
        );
        System.out.println("***");
        
        conditionEnseignants = (tab) -> {
            HashMap<String, Long> m = new HashMap();
            m.put("1A", 0l);
            m.put("2A", 0l);
            m.put("3A", 0l);
            m.putAll((HashMap<String, Long>) tab.stream().flatMap(a -> ((ArrayList) a).stream())
                .collect(Collectors.groupingBy(Niveau::getNom, Collectors.counting())));
            return m.get("1A").intValue() == N1 && 
                    m.get("2A").intValue() == N2 && 
                    m.get("3A").intValue() == N3;
        };
        result = combiner(attributions.stream().toArray(), NM, conditionEnseignants);
        System.out.println("Trouvé : " + result.size());
        System.out.println(
            result.stream().map(
                a -> a.stream().map(
                    b -> ((ArrayList) b).stream().map(
                        c -> ((Niveau) c).nom
                    ).collect(Collectors.joining(" "))
                ).collect(Collectors.joining(" ; ")).toString()
            ).collect(Collectors.joining("\n"))
        );
  
    }
    
    private static ArrayList<ArrayList> combiner(Object[] data, int nbreElements, Predicate<ArrayList> condition) {
        ArrayList<ArrayList> paths = new ArrayList();
        combiner(0, data, nbreElements, nbreElements, null, paths, condition);
        return paths;
    }
    
    private static void combiner(int indice, Object[] data, int profondeurCourante, int profondeur, ArrayList courant,
            ArrayList<ArrayList> combinaisons, Predicate<ArrayList> condition) {
        if ( profondeur == 0 ) {
            courant.add(data[indice]);
            if ( condition.test(courant))
                combinaisons.add(courant);
            return;
        }
        else if ( profondeur == profondeurCourante ) {
            courant = new ArrayList();
        }
        else {
            courant.add(data[indice]);
        }
        for ( int i = indice; i < data.length; i++ ) {
            combiner(i, data, profondeurCourante, profondeur - 1, new ArrayList(courant), combinaisons, condition);
        }
    }
    
    static class Niveau {
        String nom;
        Integer facteur;

        public Niveau(String nom, Integer facteur) {
            this.nom = nom;
            this.facteur = facteur;
        }

        public String getNom() {
            return nom;
        }

        public Integer getFacteur() {
            return facteur;
        }
        
    }
    
}