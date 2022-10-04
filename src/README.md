COP3252 Final Project: Durak
======
Made by [Nicholas Pena](https://github.com/Nick-Pena) & [Evgeniya Kalashnikova](https://github.com/evginny).


We chose to create a Java version of Durak, a Russian card game. We exclusively used a mix of the Swing and AWT Java libraries for the game's GUI and event handling.

[Click here to the rules of the game.](https://gathertogethergames.com/durak)


At the center of the screen you can see the currently played cards. At the bottom of the screen is your hand, at the top of the screen is your opponent's hand. To the left is the trump card displayed underneath the remaining deck. At the bottom left there is a button to pass your turn. There is a menu bar at the top for customization.

If there is a card in play at the start of the hand, you are defending. If there is no card in play at the start of the hand, you are attacking.

Click on the card you would like to play, if the move you are trying to make is invalid it won't be added to the center of the screen.

If you click the pass button as an attacker, the turn is over and you become the defender.

If you click the pass button as a defender, and you can't beat the attack then you take all the cards in play and you remain the defender.

The source files and resources used by the source files are under the /src/ file.

For the extra credit opportunities we chose to implement customization of the card backs and background colors of the panels within the game. All images used in this game are open source.

The following is the separation of work:

Nicholas
- Implemented event handling for the cards, menu options, and for the pass button.
- Added logic for validation of the human player's moves.
Evgeniya
- Set up overall game UI (panel, positions within panels, elements within panels, etc).
- Added logic for the computer player to use its cards in accordance to the game rules.

Everything else was a collaborative effort between us. There is no actual separation of work as we worked together on one computer after a point in time.