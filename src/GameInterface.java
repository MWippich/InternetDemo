public interface GameInterface {

    enum PlayerNum {
        Player1,
        Player2
    }

    void play();

    boolean isOn();

    //Client to server

    void setDirection(PlayerNum player, String dir);

    void setAttacking(PlayerNum player, boolean isAttacking);

    //Server to client

    int numBombs(PlayerNum player);

    int XPos(PlayerNum player);
    int YPos(PlayerNum player);

    String wallsXPos();
    String wallsYPos();

    String bombsPos();
    String firePos();
    String activeBombPos();

    void setup(int p1x, int p1y, int p2x, int p2y, int[] wallsXPos, int[] wallsYPos);

    String winner();
}

