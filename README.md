# Description
An engine to play [Evolution](https://en.wikipedia.org/wiki/Evolution:_The_Origin_of_Species)
board game by Dmitry Knorre.

# Game model
As of today, all rules, cards and game mechanics from the vanilla version
are implemented.

Game state is represented by `Game` class.

Game logic is not separated from models and is implemented in model
classes as well as game cards / properties (see `inego.evo.cards`
and `inego.evo.properties` packages).

# UI

A rudimentary Swing UI is available. To start, run the
`inego.evo.ui.MainFrame` class. A game vs two engine-controlled players
will be started. Currently, the engine makes moves by picking statistically
most promising variants which are randomly played out using the
classic MCTS formula to select the next playout.

# Engines / AI
Due to the non-deterministic nature of the game, Monte Carlo Tree
Search can only be partially applied here (namely, in the feeding
phase of turn where all information involved in making decisions
is open). Instead, a capable engine must rely on making quick and sound
decisions in a massive number of playouts.

# Work in progress

Support serializing game states to protobuf. This will be used to
collect auto-played games to train engines.

# Future plans

Apply self-play to train engines.