// ComputerPlayer subclass
// Contains all valid moves that computer can / have to do during the game

import java.util.Vector;

public class ComputerPlayer extends Player {

    public ComputerPlayer()
    {
        hand = new Vector<>();
    }

    public int attack(Vector<Card> turnCards){
        int index = -1;
        // sort hand
        if (hand.size() > 0){
            Vector<Card> newVec = returnSortedHand(hand);
            Card lowestAttackCard = newVec.elementAt(0);

            // find lowest rank regular card
            for (int i = 0; i < newVec.size(); i++){
                if (newVec.elementAt(i).getCardSuit() != Game.trumpSuit){
                    lowestAttackCard = newVec.elementAt(i);
                    break;
                }
            }

            // finds the index of the lowest rank card in the unsorted hand
            for (int i = 0; i < hand.size(); i++){
                if (hand.elementAt(i).getCardSuit() == lowestAttackCard.getCardSuit() &&
                        hand.elementAt(i).getRank() == lowestAttackCard.getRank()){
                    index = i;
                }
            }

            // add card to the pile and remove from hand
            turnCards.add(hand.elementAt(index));
            hand.removeElementAt(index);
        }
        return index;
    }

    public int defend(Card card, Vector<Card> turnCards){
        int index = -1;
        boolean isFound = false;

        // find the lowest rank card
        Vector<Card> newVec = returnSortedHand(hand);
        Card lowestDefendCard = newVec.elementAt(0);

        // find the lowest rank card with the same suit
        // it can be a regular card and a trump card
        for (int i = 0; i < hand.size(); i++){
            if (newVec.elementAt(i).getCardSuit() == card.getCardSuit()){
                if (newVec.elementAt(i).getRank() > card.getRank()){
                    lowestDefendCard = newVec.elementAt(i);
                    isFound = true;
                    break;
                }
            }
        }
        // if the attacker card is not trump
        if (!isFound){
            if (card.getCardSuit() != Game.trumpSuit){
                // find a lowest rank trump suit
                for (int i = 0; i < newVec.size(); i ++){
                    if (newVec.elementAt(i).getCardSuit() == Game.trumpSuit){
                        lowestDefendCard = newVec.elementAt(i);
                        isFound = true;
                        break;
                    }
                }
            }
        }

        // finds the index of the lowest rank card in the unsorted hand
        if (isFound){
            for (int i = 0; i < hand.size(); i++){
                if (hand.elementAt(i).getCardSuit() == lowestDefendCard.getCardSuit() &&
                        hand.elementAt(i).getRank() == lowestDefendCard.getRank()){

                    index = i;
                }
            }

            // add card to the pile and remove from hand
            turnCards.add(hand.elementAt(index));
            hand.removeElementAt(index);
        }
        return index;
    }

    public int addExtraAttackCards(Vector<Card> turnCards, Vector<Card> opponentHand, CardDeck cd){
        int index = -1;
        if (opponentHand.size() > 0){
            // sort turn cards
            boolean isFound = false;
            Vector<Card> sortedTurnCards = returnSortedHand(turnCards);
            Vector<Card> sortedHand = returnSortedHand(hand);

            Card cardToReturn = new Card();


            if (cd.deck.size() > 4){
                // find a card that is not a trump card and its rank lower than 11 (Jack)
                for (int i = 0; i < sortedTurnCards.size(); i++){
                    for (int j = 0; j < sortedHand.size(); j++){
                        if (sortedTurnCards.elementAt(i).getRank() == sortedHand.elementAt(j).getRank()){
                            if (sortedHand.elementAt(j).getCardSuit() != Game.trumpSuit &&
                                    sortedHand.elementAt(j).getRank() < 11){
                                cardToReturn = sortedHand.elementAt(j);
                                isFound = true;
                                break;
                            }
                        }
                    }
                }
            }
            else {
                // find a card if its rank higher than 11 (Jack), still not trump
                for (int i = 0; i < sortedTurnCards.size(); i++){
                    for (int j = 0; j < sortedHand.size(); j++){
                        if (sortedTurnCards.elementAt(i).getRank() == sortedHand.elementAt(j).getRank()){
                            if (sortedHand.elementAt(j).getCardSuit() != Game.trumpSuit){
                                cardToReturn = sortedHand.elementAt(j);
                                isFound = true;
                                break;
                            }
                        }
                    }
                }
                if (!isFound){
                    // find a card even if it is a trump card
                    for (int i = 0; i < sortedTurnCards.size(); i++){
                        for (int j = 0; j < sortedHand.size(); j++){
                            if (sortedTurnCards.elementAt(i).getRank() == sortedHand.elementAt(j).getRank()){
                                cardToReturn = sortedHand.elementAt(j);
                                isFound = true;
                                break;
                            }
                        }
                    }
                }
            }

            // finds the index of the lowest rank card in the unsorted hand
            if (isFound){
                for (int i = 0; i < hand.size(); i++){
                    if (hand.elementAt(i).getCardSuit() == cardToReturn.getCardSuit() &&
                            hand.elementAt(i).getRank() == cardToReturn.getRank()){

                        index = i;
                    }
                }
                // add card to the pile and remove from hand
                turnCards.add(hand.elementAt(index));
                hand.removeElementAt(index);
            }
        }
        return index;
    }
}