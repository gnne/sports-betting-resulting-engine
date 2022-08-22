import java.util.*;

class MarketTotalGoals extends Market implements ResultSubscriber {
    final static MarketTotalGoals INSTANCE = new MarketTotalGoals(2);

    private MarketTotalGoals(int id) {
        marketIncidentType = IncidentType.GOAL;
        this.marketSelections = new TreeSet<>(Comparator.comparing(Selection::getId));
    }

    @Override
    public void update(Market market, Map<TeamType, Integer> scores) {
        if(market.getClass().getName().equals(this.getClass().getName())) {
            this.scores = scores;
            setSelections();
            printResultsForMarket();
        }
    }

    void setSelections() {
        Selection over3 = new Selection(4, "Over 3 goals", condition -> scores.get(TeamType.NONE) > 3 );
        Selection exactly3 = new Selection(5, "Exactly 3 goals", condition -> scores.get(TeamType.NONE) == 3);
        Selection under3 = new Selection(6, "Under 3 goals", condition -> scores.get(TeamType.NONE) < 3);
        this.marketSelections.add(over3);
        this.marketSelections.add(exactly3);
        this.marketSelections.add(under3);
        addToAll();
    }
}
