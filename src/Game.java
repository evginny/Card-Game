// Game class creates all UI for the game, and implements the game logic

import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import java.awt.event.*;

public class Game extends JFrame
{
    // players
    private static HumanPlayer human = new HumanPlayer();
    private static ComputerPlayer cpu = new ComputerPlayer();

    // card deck and trump suit
    private CardDeck cd = new CardDeck();
    static Card.Suit trumpSuit;

    // trump card's label
    private JLabel trumpCard;

    //cards currently in play
    private Vector<Card> turnCards = new Vector<>();

    // players hands as labels
    private Vector<JLabel> humanHandLabels = new Vector<>();
    private Vector<JLabel> computerHandLabels = new Vector<>();

    // visual representation of cards currently in play
    private Vector<JLabel> attackCards = new Vector<>(6);
    private Vector<JLabel> defenceCards = new Vector<>(6);

    // iterators for the above
    private int attackCardPosition = 0;
    private int defenceCardPosition = 0;

    private JMenuBar frameBar = new JMenuBar();

    // main container for currently played cards
    private JLayeredPane battlePanel = new JLayeredPane();

    // panels for players' cards
    private JPanel computerHandPanel = new JPanel();
    private JPanel humanHandPanel = new JPanel();

    // path to card icon resources
    private String cardBackPath = "/resources/card_backs/default.png";
    private String deckCardBackPath = "/resources/card_backs/default_rot.png";

    // labels for card backs
    private JLabel defaultCardBack = new JLabel(new ImageIcon(this.getClass().getResource(cardBackPath)));
    private JLabel deckCardBack = new JLabel(new ImageIcon(this.getClass().getResource(deckCardBackPath)));

    // text showing number of remaining cards in the deck
    private JLabel remainingCards = new JLabel();

    public static void main(String [] args){

        Game mainGame = new Game();

        // if cpu is attacking first, force it to attack
        if(cpu.isOffense)
        {
            mainGame.computerMove();
        }
    }

    public Game()
    {
        // setting up the main game frame
        super("Durak Game");
        this.setSize(900, 650);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // setting up panels in game
        computerHandPanel.setLayout(new FlowLayout());
        computerHandPanel.setBackground(Color.GRAY);

        humanHandPanel.setLayout(new FlowLayout());
        humanHandPanel.setBackground(Color.GRAY);

        battlePanel.setOpaque(true);
        battlePanel.setBackground(Color.LIGHT_GRAY);

        // deal players initial cards and display them
        drawPlayerHands();

        // get trump suit after cards are dealt
        trumpSuit = cd.getTrumpSuit();

        // find which player goes first
        getTurnOrder();

        this.setUpGameBoard();
        this.setUpMenuBar();

        // add panels to the main game frame
        this.add(computerHandPanel, BorderLayout.NORTH);
        this.add(humanHandPanel, BorderLayout.SOUTH);
        this.getContentPane().add(battlePanel, BorderLayout.CENTER);
    }

    public void setUpMenuBar()
    {
        // new button group for backgrounds
        ButtonGroup buttons = new ButtonGroup();
        JMenu backgroundMenu = new JMenu("Background");
        backgroundMenu.setMnemonic(KeyEvent.VK_B);

        // backgrounds are flags, currently there are 10

        // initialize buttons, add listeners,
        // add them to their group, then add to the background menu
        JRadioButtonMenuItem defaultFlag = new JRadioButtonMenuItem("Default", true);
        addFlagListeners(defaultFlag, Color.GRAY, Color.LIGHT_GRAY, Color.GRAY);
        buttons.add(defaultFlag);
        backgroundMenu.add(defaultFlag);
        JRadioButtonMenuItem germanyFlag = new JRadioButtonMenuItem("Germany");
        addFlagListeners(germanyFlag, Color.BLACK, Color.RED, Color.YELLOW);
        buttons.add(germanyFlag);
        backgroundMenu.add(germanyFlag);
        JRadioButtonMenuItem estoniaFlag = new JRadioButtonMenuItem("Estonia");
        addFlagListeners(estoniaFlag, Color.CYAN, Color.BLACK, Color.WHITE);
        buttons.add(estoniaFlag);
        backgroundMenu.add(estoniaFlag);
        JRadioButtonMenuItem netherlandsFlag = new JRadioButtonMenuItem("Netherlands");
        addFlagListeners(netherlandsFlag, Color.RED, Color.WHITE, Color.BLUE);
        buttons.add(netherlandsFlag);
        backgroundMenu.add(netherlandsFlag);
        JRadioButtonMenuItem colombiaFlag = new JRadioButtonMenuItem("Colombia");
        addFlagListeners(colombiaFlag, Color.YELLOW, Color.BLUE, Color.RED);
        buttons.add(colombiaFlag);
        backgroundMenu.add(colombiaFlag);
        JRadioButtonMenuItem austriaFlag = new JRadioButtonMenuItem("Austria");
        addFlagListeners(austriaFlag, Color.BLACK, Color.RED, Color.YELLOW);
        buttons.add(austriaFlag);
        backgroundMenu.add(austriaFlag);
        JRadioButtonMenuItem luxembourgFlag = new JRadioButtonMenuItem("Luxembourg");
        addFlagListeners(luxembourgFlag, Color.RED, Color.WHITE, Color.CYAN);
        buttons.add(luxembourgFlag);
        backgroundMenu.add(luxembourgFlag);
        JRadioButtonMenuItem hungaryFlag = new JRadioButtonMenuItem("Hungary");
        addFlagListeners(hungaryFlag, Color.RED, Color.WHITE, Color.GREEN);
        buttons.add(hungaryFlag);
        backgroundMenu.add(hungaryFlag);
        JRadioButtonMenuItem armeniaFlag = new JRadioButtonMenuItem("Armenia");
        addFlagListeners(armeniaFlag, Color.RED, Color.BLUE, Color.ORANGE);
        buttons.add(armeniaFlag);
        backgroundMenu.add(armeniaFlag);
        JRadioButtonMenuItem ossetiaFlag = new JRadioButtonMenuItem("Ossetia");
        addFlagListeners(ossetiaFlag, Color.WHITE, Color.RED, Color.YELLOW);
        buttons.add(ossetiaFlag);
        backgroundMenu.add(ossetiaFlag);
        JRadioButtonMenuItem komiFlag = new JRadioButtonMenuItem("Komi");
        addFlagListeners(komiFlag, Color.BLUE, Color.GREEN, Color.WHITE);
        buttons.add(komiFlag);
        backgroundMenu.add(komiFlag);
        JRadioButtonMenuItem elSalvadorFlag = new JRadioButtonMenuItem("El Salvador");
        addFlagListeners(elSalvadorFlag, Color.BLUE, Color.WHITE, Color.BLUE);
        buttons.add(elSalvadorFlag);
        backgroundMenu.add(elSalvadorFlag);


        // new button group for card backs
        ButtonGroup cardBackGroup = new ButtonGroup();
        JMenu cardBackMenu = new JMenu("Card Backs");
        cardBackMenu.setMnemonic(KeyEvent.VK_C);

        // initialize buttons, add listeners,
        // add them to their group, then add to the card back menu
        JRadioButtonMenuItem defaultBack = new JRadioButtonMenuItem("Default", true);
        addCardBackListeners(defaultBack, "default");
        cardBackGroup.add(defaultBack);
        cardBackMenu.add(defaultBack);
        JRadioButtonMenuItem rabbitBack = new JRadioButtonMenuItem("Rabbit");
        addCardBackListeners(rabbitBack, "rabbit");
        cardBackMenu.add(rabbitBack);
        cardBackGroup.add(rabbitBack);
        JRadioButtonMenuItem greenBack = new JRadioButtonMenuItem("Green");
        addCardBackListeners(greenBack, "green");
        cardBackMenu.add(greenBack);
        cardBackGroup.add(greenBack);
        JRadioButtonMenuItem strangerBack = new JRadioButtonMenuItem("Stranger");
        addCardBackListeners(strangerBack, "stranger");
        cardBackMenu.add(strangerBack);
        cardBackGroup.add(strangerBack);

        frameBar.add(cardBackMenu);
        frameBar.add(backgroundMenu);

        setJMenuBar(frameBar);
    }

    public void addFlagListeners(JRadioButtonMenuItem flag, Color c1, Color c2, Color c3)
    {
        flag.addActionListener(new ActionListener() {

            private Color color1 = c1, color2 = c2, color3 = c3;
            public void actionPerformed(ActionEvent e) {
                computerHandPanel.setBackground(color1);
                battlePanel.setBackground(color2);
                humanHandPanel.setBackground(color3);

                revalidate();
                repaint();
            }
        });
    }

    public void addCardBackListeners(JRadioButtonMenuItem cardBack, String name)
    {
        cardBack.addActionListener(new ActionListener() {
            private String backName = name;
            public void actionPerformed(ActionEvent e) {
                deckCardBackPath = "/resources/card_backs/" + backName + "_rot.png";
                cardBackPath = "/resources/card_backs/" + backName + ".png";

                deckCardBack.setIcon(new ImageIcon(this.getClass().getResource(deckCardBackPath)));
                defaultCardBack.setIcon(new ImageIcon(this.getClass().getResource(cardBackPath)));

                for(JLabel cardLabel : computerHandLabels)
                {
                    cardLabel.setIcon(defaultCardBack.getIcon());
                }

                revalidate();
                repaint();
            }
        });
    }

    public void setUpGameBoard(){
        int width, height;

        // displays trump card
        trumpCard = new JLabel(cd.deck.firstElement().getIcon());
        trumpCard.setOpaque(true);
        trumpCard.setBounds(50, 50,
                trumpCard.getIcon().getIconWidth(),
                trumpCard.getIcon().getIconHeight());
        battlePanel.add(trumpCard,  JLayeredPane.DEFAULT_LAYER);

        // displays the deck as a single card back
        deckCardBack.setOpaque(true);
        deckCardBack.setBounds(30, 100,
                deckCardBack.getIcon().getIconWidth(),
                deckCardBack.getIcon().getIconHeight());
        battlePanel.add(deckCardBack, JLayeredPane.PALETTE_LAYER);

        remainingCards.setText("<html>Remaining cards: " + cd.deck.size() + "<html>");
        remainingCards.setBounds(70, 170, 100, 100);
        battlePanel.add(remainingCards, JLayeredPane.DEFAULT_LAYER);

        JButton passButton = new JButton("Pass");
        passButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(turnCards.size() > 0)
                {
                    playerPass();
                }
            }
        });
        passButton.setOpaque(true);
        passButton.setBounds(50, 250, 100, 30);
        battlePanel.add(passButton, JLayeredPane.DEFAULT_LAYER);

        height = 50;
        width = 530;
        int cardWidth = 100;
        int cardHeight = 145;

        // initializes attack card positions in battlePanel
        for (int i = 0; i < 6; i ++){
            attackCards.add(new JLabel());

            switch (i) {
                case 0 -> attackCards.elementAt(i).setBounds(width, height, cardWidth, cardHeight);
                case 1 -> attackCards.elementAt(i).setBounds(width + 120, height, cardWidth, cardHeight);
                case 2 -> attackCards.elementAt(i).setBounds(width - 120, height, cardWidth, cardHeight);
                case 3 -> attackCards.elementAt(i).setBounds(width + 240, height, cardWidth, cardHeight);
                case 4 -> attackCards.elementAt(i).setBounds(width - 240, height, cardWidth, cardHeight);
                case 5 -> attackCards.elementAt(i).setBounds(width + 360, height, cardWidth, cardHeight);
            }

            battlePanel.add(attackCards.elementAt(i), JLayeredPane.DEFAULT_LAYER);
        }

        height = 110;

        // initializes defense card positions in battlePanel
        for (int i = 0; i < 6; i ++){
            defenceCards.add(new JLabel());

            switch (i) {
                case 0 -> defenceCards.elementAt(i).setBounds(width, height, cardWidth, cardHeight);
                case 1 -> defenceCards.elementAt(i).setBounds(width + 120, height, cardWidth, cardHeight);
                case 2 -> defenceCards.elementAt(i).setBounds(width - 120, height, cardWidth, cardHeight);
                case 3 -> defenceCards.elementAt(i).setBounds(width + 240, height, cardWidth, cardHeight);
                case 4 -> defenceCards.elementAt(i).setBounds(width - 240, height, cardWidth, cardHeight);
                case 5 -> defenceCards.elementAt(i).setBounds(width + 360, height, cardWidth, cardHeight);
            }

            battlePanel.add(defenceCards.elementAt(i), JLayeredPane.POPUP_LAYER);
        }
    }

    private void drawPlayerHands(){

        // each player should start with 6 cards
        // the human player has MouseListeners on each of their cards
        // the computer player does not have any listeners

        // take last card from the deck
        // then and add it to the player's hand
        // alternating each card draw
        for(int i = 0; i < 12; i++)
        {
            if(i % 2 == 0)
            {
                human.hand.add(cd.deck.lastElement());
                cd.deck.removeElementAt(cd.deck.size() - 1);

                humanHandLabels.add(new JLabel(human.hand.lastElement().getIcon()));

                initHumanListeners(human.hand.lastElement(), humanHandLabels.lastElement());

                humanHandPanel.add(humanHandLabels.lastElement());
            }
            else
            {
                cpu.hand.add(cd.deck.lastElement());
                cd.deck.removeElementAt(cd.deck.size() - 1);

                computerHandLabels.add(new JLabel(defaultCardBack.getIcon()));
                computerHandPanel.add(computerHandLabels.lastElement());
            }
        }

        humanHandPanel.revalidate();
        humanHandPanel.repaint();

        computerHandPanel.revalidate();
        computerHandPanel.repaint();
    }

    private void refillHand(Player p)
    {
        // if the player or cpu's hands have less than 6 cards
        // we draw them up to 6. otherwise we skip them if they have more

        if(cd.deck.size() > 0)
        {
            if(p instanceof HumanPlayer && p.hand.size() < 6)
            {
                for(int i = p.hand.size(); i < 6; i++)
                {
                    if(cd.deck.size() == 0)
                    {
                        break;
                    }
                    p.hand.add(cd.deck.lastElement());
                    cd.deck.removeElementAt(cd.deck.size() - 1);

                    humanHandLabels.add(new JLabel(p.hand.lastElement().getIcon()));

                    initHumanListeners(p.hand.lastElement(), humanHandLabels.lastElement());

                    humanHandPanel.add(humanHandLabels.lastElement());
                }

                humanHandPanel.revalidate();
                humanHandPanel.repaint();
            }
            else if(p instanceof ComputerPlayer && p.hand.size() < 6)
            {
                for(int i = p.hand.size(); i < 6;  i++)
                {
                    if(cd.deck.size() == 0)
                    {
                        break;
                    }
                    p.hand.add(cd.deck.lastElement());
                    cd.deck.removeElementAt(cd.deck.size() - 1);
                    computerHandLabels.add(i, new JLabel(defaultCardBack.getIcon()));
                    computerHandPanel.add(computerHandLabels.lastElement());
                }

                computerHandPanel.revalidate();
                computerHandPanel.repaint();
            }
        }
        // checks if we need to remove the deck's label
        else if(cd.deck.size() == 1)
        {
            battlePanel.remove(deckCardBack);
        }
        // checks if we need to remove the trump card's label
        else
        {
            if(deckCardBack != null)
            {
                battlePanel.remove(deckCardBack);
            }
            battlePanel.remove(trumpCard);
        }
    }

    private void takeTurnCards(Player p)
    {
        // if either player can't beat the attack
        // or if they pass as a defender
        // then have the defending player take all cards
        // that are currently in play

        if (p instanceof HumanPlayer){

            while(!turnCards.isEmpty()){
                p.hand.add(turnCards.lastElement());
                turnCards.removeElementAt(turnCards.size() - 1);

                humanHandLabels.add(new JLabel(p.hand.lastElement().getIcon()));

                initHumanListeners(p.hand.lastElement(), humanHandLabels.lastElement());

                humanHandPanel.add(humanHandLabels.lastElement());
            }

            humanHandPanel.revalidate();
            humanHandPanel.repaint();
        }
        else if (p instanceof ComputerPlayer){
            while(!turnCards.isEmpty()){
                p.hand.add(turnCards.lastElement());
                turnCards.removeElementAt(turnCards.size() - 1);

                computerHandLabels.add(new JLabel(defaultCardBack.getIcon()));
                computerHandPanel.add(computerHandLabels.lastElement());
            }
            computerHandPanel.revalidate();
            computerHandPanel.repaint();
        }
    }

    private void initHumanListeners(Card _thisCard, JLabel _thisLabel)
    {
        humanHandLabels.lastElement().addMouseListener(new MouseAdapter() {
            // store each label's matching card
            // and itself inside their own listener
            // for later use
            private final Card thisCard = _thisCard;
            private final JLabel thisLabel = _thisLabel;
            public void mouseClicked(MouseEvent e)
            {
                if(human.isAttack)
                {
                    if (validMoveCheck(thisCard, turnCards)){

                        winningCheck();

                        turnCards.add(thisCard);

                        // adds the card the human played to
                        // its respective position on battlePanel
                        if (human.isOffense){
                            attackCards.elementAt(attackCardPosition).setIcon(turnCards.lastElement().getIcon());
                            attackCardPosition ++;
                        }
                        else {
                            defenceCards.elementAt(defenceCardPosition).setIcon(turnCards.lastElement().getIcon());
                            defenceCardPosition ++;
                        }
                        human.hand.remove(thisCard);

                        humanHandLabels.remove(thisLabel);
                        humanHandPanel.remove(thisLabel);

                        human.isAttack = false;
                        cpu.isAttack = true;

                        // after our move, we tell the computer to play a card
                        computerMove();

                        winningCheck();
                    }
                }

                repaint();
                revalidate();
            }
        });
    }

    private void clearTurnLabels()
    {
        for(JLabel label : attackCards)
        {
            label.setIcon(null);
        }

        attackCardPosition = 0;

        for(JLabel label : defenceCards)
        {
            label.setIcon(null);
        }

        defenceCardPosition = 0;
    }
    private void playerPass()
    {
        clearTurnLabels();

        // if the human passes as offense, both players draw cards up to 6 (if possible)
        // if the human passes as defense, they must take the cards currently in play
        if(human.isOffense)
        {
            refillHand(human);
            refillHand(cpu);

            turnCards.removeAllElements();
        }
        else
        {
            takeTurnCards(human);
            refillHand(cpu);
        }

        // swap the human player and computer player's roles
        human.isAttack = false;
        human.isOffense = false;

        cpu.isAttack = true;
        cpu.isOffense = true;
        remainingCards.setText("<html>Remaining cards: " + cd.deck.size() + "<html>");

        computerMove();

        repaint();
        revalidate();
    }

    public void winningCheck()
    {
        // if the human and computer run out of cards in the same turn, it's a draw
        // else if the human or computer player runs out of cards, they win
        if(cd.deck.size() == 0 && cpu.hand.size() == 0 && human.hand.size() == 0)
        {
            JOptionPane.showMessageDialog(battlePanel, "It's a draw!");
        }
        else if(cd.deck.size() == 0 && human.hand.size() == 0)
        {
            JOptionPane.showMessageDialog(battlePanel, "Human player wins");
        }
        else if(cd.deck.size() == 0 && cpu.hand.size() == 0)
        {
            JOptionPane.showMessageDialog(battlePanel, "Computer player wins");
        }
    }

    private void computerMove(){

        winningCheck();

        int index;

        // if the computer can currently attack
        if(cpu.isAttack)
        {
            if(cpu.isOffense)
            {
                // if it's the first move in the current turn
                if(turnCards.size() == 0)
                {
                    index = cpu.attack(turnCards);
                    // if there is no card to play
                    // (shouldn't happen but just in case)
                    if(index == -1)
                    {
                        clearTurnLabels();

                        refillHand(cpu);

                        cpu.isAttack = false;
                        cpu.isOffense = false;
                        human.isAttack = true;
                        human.isOffense = true;

                        remainingCards.setText("<html>Remaining cards: " + cd.deck.size() + "<html>");

                        return;
                    }
                }
                // if it's the not the first move in the current turn
                else
                {
                    index = cpu.addExtraAttackCards(turnCards, human.hand, cd);
                    if(index == -1)
                    {
                        clearTurnLabels();

                        refillHand(human);
                        refillHand(cpu);

                        turnCards.removeAllElements();

                        cpu.isAttack = false;
                        cpu.isOffense = false;
                        human.isAttack = true;
                        human.isOffense = true;

                        remainingCards.setText("<html>Remaining cards: " + cd.deck.size() + "<html>");

                        return;
                    }
                }

                // display the card played in battlePanel
                JLabel cardLabel = new JLabel(turnCards.lastElement().getIcon());
                attackCards.elementAt(attackCardPosition).setIcon(cardLabel.getIcon());
                attackCardPosition++;

                computerHandPanel.remove(computerHandLabels.elementAt(index));
                computerHandLabels.removeElementAt(index);

                computerHandPanel.revalidate();
                computerHandPanel.repaint();

                this.revalidate();

                // set the cpu to not be able to attack
                cpu.isAttack = !cpu.isAttack;
                human.isAttack = !human.isAttack;
            }
            // if the computer player is defending
            else
            {
                // if there are cards currently in play
                if (turnCards.size() > 0){

                    // get the index of the card the computer wants to defend with
                    index = cpu.defend(turnCards.lastElement(), turnCards);

                    // if the cpu would like to defend, play the card
                    if (index != -1){

                        // display the card the computer played
                        defenceCards.elementAt(defenceCardPosition)
                                .setIcon(turnCards.lastElement().getIcon());
                        defenceCardPosition++;

                        computerHandPanel.remove(computerHandLabels.elementAt(index));
                        computerHandLabels.removeElementAt(index);
                        computerHandPanel.revalidate();
                        computerHandPanel.repaint();

                        this.revalidate();

                        // set the computer to not be able to attack yet
                        cpu.isAttack = !cpu.isAttack;
                        human.isAttack = !human.isAttack;
                    }
                    // if the cpu would like to pass, take all cards currently in play
                    else {
                        takeTurnCards(cpu);
                        refillHand(human);

                        remainingCards.setText("<html>Remaining cards: " + cd.deck.size() + "<html>");

                        human.isAttack = true;
                        cpu.isAttack = false;

                        clearTurnLabels();
                    }
                }
            }
        }
        winningCheck();
    }

    private void getTurnOrder()
    {
        Card humanBestCard = new Card(14, trumpSuit),
                cpuBestCard = new Card(14, trumpSuit);

        // finds both players' lowest rank cards
        // which are of the trump suit
        for(int i = 0; i < 6; i++)
        {
            if(human.hand.elementAt(i).getCardSuit() == trumpSuit
                    && humanBestCard.getRank() > human.hand.elementAt(i).getRank())
            {
                humanBestCard = human.hand.elementAt(i);
            }
            if(cpu.hand.elementAt(i).getCardSuit() == trumpSuit
                    && cpuBestCard.getRank() > cpu.hand.elementAt(i).getRank())
            {
                cpuBestCard = cpu.hand.elementAt(i);
            }
        }

        // compare their best cards
        if(humanBestCard.getRank() < cpuBestCard.getRank())
        {
            human.isAttack = true;
            human.isOffense = true;

            cpu.isAttack = false;
            cpu.isOffense = false;
        }
        else
        {
            human.isAttack = false;
            human.isOffense = false;

            cpu.isAttack = true;
            cpu.isOffense = true;
        }
    }

    private boolean validMoveCheck(Card cardToPlay, Vector<Card> turnCards)
    {
        if(turnCards.isEmpty())
        {
            return true;
        }

        Card lastPlayedCard = turnCards.lastElement();

        // we're defending
        if(turnCards.size() % 2 == 1)
        {
            if(lastPlayedCard.getCardSuit() == cardToPlay.getCardSuit() 
                    && lastPlayedCard.getRank() < cardToPlay.getRank())
            {
                return true;
            }
            else if(lastPlayedCard.getCardSuit() != trumpSuit
                    && cardToPlay.getCardSuit() == trumpSuit)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        // we're attacking
        else
        {
            for(Card turnCard : turnCards)
            {
                if(turnCard.getRank() == cardToPlay.getRank())
                {
                    return true;
                }
            }
            return false;
        }
    }
}