public class Fire {

    private int timeLeft;
    private int xPos;
    private int yPos;

    Fire(int x, int y){
        timeLeft = 3;
        xPos = x;
        yPos = y;
    }


    public int getTimeLeft(){
        return timeLeft;
    }

    public void burnFire(){
        timeLeft--;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }
}
