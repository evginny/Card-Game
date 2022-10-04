// Class Player is the base class of HumanPlayer and ComputerPlayer classes


import java.util.Vector;

public class Player {
    protected Vector<Card> hand;

    // if the player is currently able to attack
    protected boolean isAttack;
    // if the player is the attacker or defender
    protected boolean isOffense;

    public Player(){
        hand = new Vector<>();
    }

    protected void setIsOffense(boolean _isOffense)
    {
        isAttack = _isOffense;
    }

    protected boolean getIsOffense()
    {
        return isOffense;
    }

    protected void setIsAttack(boolean attack){
        isAttack = attack;
    }

    protected boolean getIsAttack(){
        return isAttack;
    }

    // sorts the cards by rank from lowest to highest
    protected Vector<Card> returnSortedHand(Vector<Card> vec){
        Vector<Card> newVec = new Vector<>();

        for (int i = 0; i < vec.size(); i++){
            newVec.add(vec.elementAt(i));
        }


        // finds the lowest card and swaps it with the previous lowest card
        for (int i = 0; i < newVec.size(); i++){
            for (int j = 0; j < newVec.size() - i - 1; j++){
                if (newVec.elementAt(j).getRank() > newVec.elementAt(j + 1).getRank()){
                    Card temp = newVec.elementAt(j);
                    newVec.set(j, newVec.elementAt(j + 1));
                    newVec.set(j + 1, temp);
                }
            }
        }
        return newVec;
    }
}
