package combat;

public class ActionEconomy {
    private int actionPoints;
    private int bonusActionPoints;

    public void startTurn() {
        actionPoints = 1;
        bonusActionPoints = 1;
    }

    public boolean canTakeAction() {
        return hasActionPoints();
    }

    public boolean spendActionPoint() {
        if (!hasActionPoints()) {
            return false;
        }

        actionPoints--;
        return true;
    }

    public boolean BonusAction() {
        return spendBonusActionPoint();
    }

    public boolean spendBonusActionPoint() {
        if (!hasBonusActionPoints()) {
            return false;
        }

        bonusActionPoints--;
        return true;
    }

    public void endTurn() {
        actionPoints = 0;
        bonusActionPoints = 0;
    }

    public boolean hasActionPoints() {
        return actionPoints > 0;
    }

    public boolean hasBonusActionPoints() {
        return bonusActionPoints > 0;
    }

    public boolean isTurnFinished() {
        return actionPoints <= 0 && bonusActionPoints <= 0;
    }

    public int getActionPoints() {
        return actionPoints;
    }

    public int getBonusActionPoints() {
        return bonusActionPoints;
    }
}
