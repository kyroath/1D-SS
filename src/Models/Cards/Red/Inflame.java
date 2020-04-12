package Models.Cards.Red;

import Models.Actions.AttackActions;
import Models.Actions.PowerActions;
import Models.Cards.*;
import Models.Creatures.AbstractCharacter;
import Models.Creatures.Monsters.AbstractMonster;
import Models.Dungeon.Room.Fight;
import Models.Object.Powers.Strength;
import Models.Object.Powers.Vulnerable;

public class Inflame extends AbstractCard {
    public Inflame(){
        name = "Inflame";
        description = "Gain 2(3) Strength.";
        cost = 1;
        type = CardType.POWER;
        color = CardColor.RED;
        rarity = CardRarity.UNCOMMON;
        target = CardTarget.SELF;
        baseAttr = new BaseCardAttributes();
        usable = true;
        upgradable = true;
    }

    @Override
    public boolean use(Fight f, AbstractCharacter player) {
        if (!player.changeEnergy(-cost)) return false;

        Strength s = new Strength();

        if(upgradable)
            s.stack(1);
        else
            s.stack(2);

        PowerActions.addPower(player, s);

        return true;
    }

    @Override
    public void upgrade() {
        if (upgradable) {
            upgradable = false;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        Inflame copy = new Inflame();
        copy.upgradable = this.upgradable;
        return copy;
    }
}
