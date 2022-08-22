import java.util.*;
import java.util.function.Predicate;

public abstract class Market  {
    static Comparator<Market> comparator = (m1, m2) -> Integer.valueOf(m1.getId()).compareTo(m2.getId());
    static Set<Market> allMarkets = new TreeSet<>(comparator);
    static Set<Selection> allSelections = new TreeSet<>(Selection.comparator);

    int id;
    IncidentType marketIncidentType;
    Set<Selection> marketSelections;
    Map<TeamType, Integer> scores;

    protected Market(int id) {  this.id = id; }

    Market() {}  // default constructor required

    int getId() {  return id;  }

    void addToAll(){
        allSelections.addAll(this.marketSelections);
        Market.allMarkets.add(this);
    }

    public String toString() {  return Utilities.getMarketName(this); }

    void printResultsForMarket() {
        System.out.println("--------- " + this + " ---------");
        for(Market.Selection selection : marketSelections) {
            System.out.println("\t" + selection.name + " : " + (selection.getResult() == 1));
        }
    }

    class Selection {
        int id;
        String name;
        int result = -1;
        Predicate<Selection> condition;
        static Comparator<Selection> comparator = Comparator.comparing(Selection::getId);

        Selection(int id, String name, Predicate<Selection> condition) {
            this.id = id; this.name = name; this.condition = condition;
        }

        int getResult() {  return result = condition.test(this) ? 1 : 0;  }

        public int getId() {  return id;  }

        public String toString() {  return name;  }
    }
}
