import java.util.*;

class MarketMatchWinner extends Market implements ResultSubscriber {
    final static MarketMatchWinner INSTANCE = new MarketMatchWinner(1);

    private MarketMatchWinner(int id) {
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
        Selection homeWins = new Selection(1, "Home Wins", condition -> scores.get(TeamType.HOME) > scores.get(TeamType.AWAY));
        Selection awayWins = new Selection(2, "Away Wins", condition -> scores.get(TeamType.AWAY) > scores.get(TeamType.HOME));
        Selection aDraw = new Selection(3, "A Draw", selection -> scores.get(TeamType.AWAY) == scores.get(TeamType.HOME));
        this.marketSelections.add(homeWins);
        this.marketSelections.add(awayWins);
        this.marketSelections.add(aDraw);
        addToAll();
    }
}