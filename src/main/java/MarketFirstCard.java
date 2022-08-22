import java.util.*;

class MarketFirstCard extends Market implements ResultSubscriber {
    final static MarketFirstCard INSTANCE = new MarketFirstCard(4);

    private MarketFirstCard(int id) {
        marketIncidentType = IncidentType.CARD;
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
        Selection homeWins = new Selection(9, "Home Wins", condition -> scores.get(TeamType.NONE) == 0 && scores.get(TeamType.HOME) == 1 );
        Selection awayWins = new Selection(10, "Away Wins", condition -> scores.get(TeamType.NONE) == 0 && scores.get(TeamType.AWAY) == 1 );
        Selection noCardInGame = new Selection(11, "No card in game", condition -> scores.get(TeamType.NONE) == 1 );
        this.marketSelections.add(homeWins);
        this.marketSelections.add(awayWins);
        this.marketSelections.add(noCardInGame);
        addToAll();
    }
}
