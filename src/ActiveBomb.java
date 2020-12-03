public class ActiveBomb {

    private int timeLeft;
    private int xPos;
    private int yPos;
    private int range;

    ActiveBomb(int x, int y) {
        this.xPos = x;
        this.yPos = y;
        this.timeLeft = 10;
        this.range = 3;
    }


    public boolean exploading() {
        return timeLeft <= 0;
    }

    public void tick() {
        timeLeft--;
    }

    public int getRange() {
        return range;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }
}
