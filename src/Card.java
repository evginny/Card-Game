// Card class contains the info about a card (suit and rank), and
// initializes the icon for a card based on those two attributes

import javax.swing.*;

public class Card {
    private int rank;
    private Suit cardSuit;

    public enum Suit {
        clubs, diamonds, hearts, spades
    }

    public Card(){

    }

    public Card(int n, Suit s){
        rank = n;
        cardSuit = s;
    }
    public int getRank(){
        return rank;
    }
    public Suit getCardSuit(){
        return cardSuit;
    }

    public Icon getIcon(){
        String output = switch (rank) {
            case 11 -> "jack_of_" + cardSuit + ".png";
            case 12 -> "queen_of_" + cardSuit + ".png";
            case 13 -> "king_of_" + cardSuit + ".png";
            case 14 -> "ace_of_" + cardSuit + ".png";
            default -> rank + "_of_" + cardSuit + ".png";
        };

        return new ImageIcon(this.getClass().getResource("/resources/cards/default/" + output));
    }

    public String printCard()
    {
        String output = switch (rank) {
            case 11 -> "Jack of " + cardSuit;
            case 12 -> "Queen of " + cardSuit;
            case 13 -> "King of " + cardSuit;
            case 14 -> "Ace of " + cardSuit;
            default -> rank + " of " + cardSuit;
        };
        return output;
    }
}
