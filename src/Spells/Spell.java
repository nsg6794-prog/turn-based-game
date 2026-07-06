package spells;

import game.GameCharacter;

public class Spell {
    String name;
    String description;
    int damage;
    int intelligenceScaling;
    int actionPointCost;
    int bonusActionPointCost;
    int healingAmount;

    public Spell(String name, String description, int damage, int intelligenceScaling, int actionPointCost, int bonusActionPointCost, int healingAmount) {
        this.name = name;
        this.description = description;
        this.damage = damage;
        this.intelligenceScaling = intelligenceScaling;
        this.actionPointCost = actionPointCost;
        this.bonusActionPointCost = bonusActionPointCost;
        this.healingAmount = healingAmount;
    }
    public int damageCalculation(GameCharacter caster) {
        return damage + (intelligenceScaling * caster.getIntelligence());
    }
    public void cast(GameCharacter caster, GameCharacter target) {
        int damageDealt = damageCalculation(caster);
        target.takeDamage(damageDealt);
    }

    public void castSpell(GameCharacter caster, GameCharacter target) {
        cast(caster, target);
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getActionPointCost() {
        return actionPointCost;
    }
    public int getBonusActionPointCost() {
        return bonusActionPointCost;
    }
    public int getHealingAmount() {
        return healingAmount;
    }

    public static final Spell FIRE_BOLT = new Spell("Fire Bolt", "A small fireball that deals 5 damage to a single target.", 5, 1, 1, 0, 0);
    public static final Spell SHADOW_BOLT = new Spell("Shadow Bolt", "A bolt of dark energy that deals 7 damage to a single target.", 7, 1, 1, 0, 0);
    public static final Spell MINOR_HEAL = new Spell("Minor Heal", "A gentle healing spell that restores a small 5 health points.", 0, 1, 1, 0, 5);
}
