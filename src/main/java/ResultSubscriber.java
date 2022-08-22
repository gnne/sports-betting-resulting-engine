import java.util.Map;

interface ResultSubscriber {
    void update(Market market, Map<TeamType, Integer> result);
}

