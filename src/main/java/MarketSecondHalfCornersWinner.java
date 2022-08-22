import java.util.*;

class MarketSecondHalfCornersWinner extends Market implements ResultSubscriber {
    final static MarketSecondHalfCornersWinner INSTANCE = new MarketSecondHalfCornersWinner(3);

    private MarketSecondHalfCornersWinner(int id) {
        marketIncidentType = IncidentType.CORNER;
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
        Selection none = this.new Selection(0, "None", condition -> scores.get(TeamType.NONE) == 1);  // added to avoid NPE (There's probably a better way if had time)
        Selection homeWins = this.new Selection(7, "Home Wins", condition -> scores.get(TeamType.NONE) == 0 && scores.get(TeamType.HOME) > scores.get(TeamType.AWAY));
        Selection awayWins = this.new Selection(8, "Away Wins", condition -> scores.get(TeamType.NONE) == 0 && scores.get(TeamType.AWAY) > scores.get(TeamType.HOME));
        this.marketSelections.add(homeWins);
        this.marketSelections.add(awayWins);
        this.marketSelections.add(none);
        addToAll();
    }
}