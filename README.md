# Soccer resulting engine

A small Java program that processes a feed of soccer match incidents - goals, cards, corners - and settles betting-market selections after each incident, as a resulting system for a sportsbook would.

I wrote this over a few days in 2022 as a take-home assignment during a job interview process with a sports-betting company. It's published here unchanged: a snapshot of my Java around 1 year after the OCP cert, and the last Java I wrote before moving in a different direction.

## What it does

Incidents are read from `resulting_incidents.json` (Jackson, with a custom deserializer) and replayed one at a time, simulating a live feed. An incident processor keeps running tallies per team and per period, and notifies a result generator, which in turn notifies four markets - match winner, total goals, second-half corners winner, first card - covering eleven selections. After every incident, each selection's current result is printed and written to a CSV file.

## Design notes

- Two-stage observer pattern: incident processor → result generator → markets, mirroring how resulting responds to a live feed.
- An abstract `Fixture` with a `SoccerGame` subclass; the enums (periods, teams, incident types) are deliberately wider than soccer needs, so other fixture types could slot in.
- Market selections are defined as `Predicate`s - each selection carries its own settlement condition as data.
- Unit tests with JUnit 5 (19 tests; two around file-not-found handling were left incomplete for time, and are marked as such in comments).

## What I'd do differently now

- `setFirstCard` in `ResultGenerator` has a copy-paste bug - both ternary branches test the home team, so an away first card would resolve incorrectly. The sample data happens to start with a home card, so the output is right for the assignment.
- There's too much static mutable state (singleton markets, static selection registries), which makes the design harder to test.
- The market update dispatch notifies every market with every update and filters by class name. Direct dispatch would be cleaner.
- The JSON reader swallows `IOException`, which surfaces later as a null-pointer error (Completing the tests would probably have surfaced this).

## Build and run

Requires Java 17+ and Maven. From the project root:

```
mvn clean compile
mvn exec:java -Dexec.mainClass="Main"
```

The output spans several screens (one block per incident); pipe through `more` or `less` to page it. Per-incident CSV files are written to the project root.


## License 

MIT — see [LICENSE](LICENSE). 

The sample incident and selection data files were supplied as part of the original exercise brief.