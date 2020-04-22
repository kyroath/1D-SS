package Models.Dungeon.Room;

import Models.Actions.PowerActions;
import Models.Cards.AbstractCard;
import Models.Cards.Deck;
import Models.Creatures.AbstractCharacter;
import Models.Creatures.Monsters.AbstractMonster;
import Models.Creatures.Monsters.Cultist;
import Models.Creatures.Monsters.JawWorm;
import Models.Creatures.Monsters.Temp;
import Models.Dungeon.AbstractRoom;
import Models.Main;
import Models.Object.AbstractRelic;
import Models.Object.Powers.Strength;
import Models.Object.Powers.Vulnerable;
import javafx.scene.control.Control;
import sts.Controller;

import java.util.ArrayList;

import static sts.Main.fight;
import static sts.Main.game;

public class Fight extends AbstractRoom {

    ArrayList<AbstractMonster> monsters;

    private FightState state;
    private Deck draw;
    private Deck discard;
    private Deck exhaust;
    private Deck hand;
    private int turn;
    private boolean isElite;

    private AbstractCharacter player;

    int drawAmount;

    int goldAmount;

    public Fight(AbstractRoom c) {
        type = RoomType.FIGHT;
        children = c;
        done = false;
        monsters = new ArrayList<>();
        generate();
    }
    public void nextState()
    {
        switch(state) {

            case PREFIGHT:preTurn(); break;
            case PRETURN:turn(); break;
            case TURN:postTurn();break;
            case POSTTURN:monsterPreTurn();break;
            case MONSTERPRETURN:monsterTurn();break;
            case MONSTERTURN:monsterPostTurn();break;
            case MONSTERPOSTTURN:preTurn();break;

        }
    }

    @Override
    public void start() {
        System.out.println("I AM IN START");
        player = Main.game.getPlayer();

        draw = new Deck();
        draw.copyCards(player.masterDeck);
        draw.shuffle();

        discard = new Deck();
        exhaust = new Deck();

        drawAmount = 5;

        turn = 1;
        fight.initialize(draw,player,monsters.get(0));


        PowerActions.addPower(player, new Vulnerable());
        PowerActions.addPower(player, new Strength());

        preFight();


       // postFight();
    }

    private void preFight() {
        System.out.println("I AM IN PREFIGHT");
        state = FightState.PREFIGHT;
        for (AbstractRelic r : player.relics) {
            r.onFightStart(this, player);
        }
        nextState();
    }

    private void preTurn() {
        System.out.println("I AM IN PRETURN");
        state = FightState.PRETURN;
        for (AbstractRelic r : player.relics) {
            r.onTurnStart(this);
            r.onTurnStart(player);
        }

        player.recharge();
        player.resetBlock();

        if (turn != 1) {
            PowerActions.turnEndDecrease(player);
        }
        nextState();
    }

    private void turn() {
        System.out.println("I AM IN TURN");
        state = FightState.TURN;
        hand = Deck.drawCard(draw, discard, drawAmount);
        fight.setHandDeck(hand);
        fight.draw();
        System.out.println("MONSTER HP IN FIGHT IS " + monsters.get(0).getCurrentHP());





        /*

        while (true) {
            Controller.displayFightInfo(this);

            System.out.println();
            System.out.println();

            Controller.displayDeck(hand, "Hand");
            System.out.print("Choose your card: ");

            AbstractCard card = Controller.getCardInput();
            boolean nextTurn = Controller.getNextTurn();

            if(nextTurn) break;

            if(card != null) {

                if (!card.use(this, player)) {
                    System.out.println("You do not have enough energy to use this card.");
                } else {
                    hand.removeCard(card);
                    discard.addCard(card);
                }

                monsters.removeIf(m -> m.getCurrentHP() <= 0);
                if (monsters.isEmpty()) {
                    done = true;
                    return;
                }
                System.out.println("---------------------------");
            }
        }

        discard.addDeck(hand);*/

    }
    public boolean useCard(AbstractCard card)
    {
        if (!card.use(this, player)) {
            System.out.println("You do not have enough energy to use this card.");
            return false;
        } else {
            hand.removeCard(card);
            discard.addCard(card);
        }

        monsters.removeIf(m -> m.getCurrentHP() <= 0);
        if (monsters.isEmpty()) {
            done = true;
        }
        return true;
    }

    private void postTurn() {
        System.out.println("I AM IN POSTTURN");
        state = FightState.POSTTURN;
        nextState();

    }

    private void monsterPreTurn() {
        System.out.println("I AM IN MOSNTERPRETURN ");
        state = FightState.MONSTERPRETURN;
        for (AbstractMonster m : monsters) {
            m.resetBlock();
            PowerActions.turnEndDecrease(m);
        }
        nextState();

    }

    private void monsterTurn() {
        System.out.println("I AM IN MOSNTERTURN");
        state = FightState.MONSTERTURN;
        for (AbstractMonster m : monsters) {
            m.act(this, player);
        }

        if (player.getCurrentHP() <= 0) done = true;
        nextState();
    }

    private void monsterPostTurn() {
        System.out.println("I AM IN MONSTERPOSTTURN");
        state = FightState.MONSTERPOSTTURN;
        nextState();

    }


    private void postFight() {
        System.out.println("I AM IN POSTFIGHT");
        state = FightState.POSTFIGHT;
        player.changeGold(goldAmount);
        for (AbstractRelic r : player.relics) {
            r.onFightEnd(player);
        }
        nextState();
    }

    private void generate() {
        /**
        int act = Main.game.getDungeon().getAct();
        if (act == 1) {
            // change this
         */
        monsters.add(new Cultist());
        //}

        isElite = false;
        generateRewards();
    }

    private void generateRewards() {
        goldAmount = (int) (Math.random() * 100) + 10;
    }

    // Getters and setters.
    // --------------------------------------------------------------------------------------------------------

    @Override
    public RoomType getType() {
        return type;
    }

    @Override
    public AbstractRoom getChildren() {
        return children;
    }

    @Override
    public boolean getDone() {
        return done;
    }

    public ArrayList<AbstractMonster> getMonsters() {
        return monsters;
    }

    public Deck getDraw(){ return draw; }

    public Deck getDiscard(){ return discard; }

    public Deck getExhaust(){ return exhaust; }

    public Deck getHand() { return hand; }

    public boolean getIsElite() { return isElite; }

}
