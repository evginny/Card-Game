// CardDeck class creates a deck of 36 unique cards, shuffles them,
// and initializes the top card as the trump card and puts it at the 
// bottom of the deck

import java.util.Vector;

public class CardDeck{
    public Vector<Card> deck;
    private final int DECK_SIZE = 36;

    public CardDeck(){
        deck = new Vector<Card>();
        
        // start at 6 to match card ranks with the back-end
        int rank = 6;
        for (int i = 0; i < DECK_SIZE; i++){
            switch (i % 4) {
                case 0 -> deck.addElement(new Card(rank, Card.Suit.clubs));
                case 1 -> deck.addElement(new Card(rank, Card.Suit.hearts));
                case 2 -> deck.addElement(new Card(rank, Card.Suit.spades));
                case 3 -> {
                    deck.addElement(new Card(rank, Card.Suit.diamonds));
                    rank++;
                }
            }
        }
        // shuffle
        for (int i = 0; i < DECK_SIZE; i++){
            int random = i + (int)(Math.random() * (DECK_SIZE - i));
            Card temp = deck.elementAt(random);
            deck.set(random, deck.elementAt(i));
            deck.set(i, temp);
        }
    }

    public Card.Suit getTrumpSuit()
    {
        // moves card from top of the deck to the bottom
        // and sets its suit as the trump suit
        Card.Suit returnSuit = deck.lastElement().getCardSuit();
        deck.insertElementAt(deck.lastElement(), 0);
        deck.removeElementAt(deck.size() - 1);
        return returnSuit;
    }
}