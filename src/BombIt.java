import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class BombIt implements GameInterface {

    boolean isOn = true;

    int mapSize = 24;

    boolean[][] walls = new boolean[mapSize][mapSize];

    boolean[][] bombMap = new boolean[mapSize][mapSize];
    ArrayList<ActiveBomb> activeBombs = new ArrayList<>();
    ArrayList<Fire> fires = new ArrayList<>();

    private Player player1, player2;

    String winner = "None";


    int bombRandomizer;
    Random r = new Random();


    public boolean isOn() {
        return isOn;
    }

    public void play() {

        bombRandomizer = r.nextInt(100);

        if(bombRandomizer % 20 == 0) {
            addBomb();
        }

        player1.move();
        player2.move();

        Iterator<ActiveBomb> iterator = activeBombs.iterator();
        while(iterator.hasNext()){
            ActiveBomb bomb = iterator.next();
            bomb.tick();
            if(explodingBomb(bomb)){
                iterator.remove();
            }
        }

        Iterator<Fire> fireIterator = fires.iterator();
        while (fireIterator.hasNext()){
            Fire fire = fireIterator.next();
            fire.burnFire();
            if(fire.getTimeLeft()<=0){
                fireIterator.remove();
            }
        }


        if (player1.isAttacking()) {
            if(player1.removeBomb()) {
                player1.placeBomb();
            }
        }

        if (player2.isAttacking()) {
            if(player2.removeBomb()) {
                player2.placeBomb();
            }
        }

        checkAlive();
    }


    private void checkAlive() {
        if (player1.hitByBomb() && !player2.hitByBomb()) {
            winner = "Player 2 won!";
            isOn = false;
        }
        if (player2.hitByBomb() && !player1.hitByBomb()) {
            winner = "Player 1 won!";
            isOn = false;
        }
        if (player1.hitByBomb() && player2.hitByBomb()) {
            winner = "The game ended in a tie";
            isOn = false;
        }
    }


    private void addBomb(){
        int x = r.nextInt(mapSize);
        int y = r.nextInt(mapSize);

        if(!walls[x][y] && (player1.getX() != x) && (player2.getX() != x && (player1.getY() != y) && (player2.getY() != y))){
            bombMap[x][y] = true;
        }
    }


    private boolean explodingBomb(ActiveBomb bomb) {
        if (bomb.exploading()) {
            for (int i = 0; i < bomb.getRange(); i++){
                if (bomb.getxPos() + i <= mapSize - 1) {
                    if (walls[bomb.getxPos() + i][bomb.getyPos()]) {
                        break;
                    }
                    Fire fire = new Fire(bomb.getxPos() + i, bomb.getyPos());
                    fires.add(fire);
                }
            }

            for (int i = 0; i < bomb.getRange(); i++){
                if (bomb.getyPos() + i <= mapSize - 1) {
                    if (walls[bomb.getxPos()][bomb.getyPos() + i]) {
                        break;
                    }
                    Fire fire = new Fire(bomb.getxPos(), bomb.getyPos() + i);
                    fires.add(fire);
                }
            }

            for (int i = 0; i < bomb.getRange(); i++) {
                if (bomb.getyPos() - i >= 0) {
                    if (walls[bomb.getxPos()][bomb.getyPos() - i]) {
                        break;
                    }
                    Fire fire = new Fire(bomb.getxPos(), bomb.getyPos() - i);
                    fires.add(fire);
                }
            }

            for (int i = 0; i < bomb.getRange(); i++) {
                if (bomb.getxPos() - i >= 0) {
                    if (walls[bomb.getxPos() - i][bomb.getyPos()]) {
                        break;
                    }
                    Fire fire = new Fire(bomb.getxPos() - i, bomb.getyPos());
                    fires.add(fire);
                }
            }
        }
        return bomb.exploading();
    }


    @Override
    public void setDirection(PlayerNum player, String dir) {
        if (player == PlayerNum.Player1) {
            player1.setDirection(dir);
        } else if (player == PlayerNum.Player2) {
            player2.setDirection(dir);
        }
    }

    @Override
    public void setAttacking(PlayerNum player, boolean isAttacking) {
        if (player == PlayerNum.Player1) {
            player1.setAttacking(isAttacking);
        } else if (player == PlayerNum.Player2) {
            player2.setAttacking(isAttacking);
        }
    }


    @Override
    public int numBombs(PlayerNum player) {
        if (player == PlayerNum.Player1) {
            return player1.getNumBombs();
        } else {
            return player2.getNumBombs();
        }
    }


    @Override
    public int XPos(PlayerNum player) {
        if (player == PlayerNum.Player1) {
            return player1.getX();
        } else {
            return player2.getX();
        }
    }


    @Override
    public int YPos(PlayerNum player) {
        if (player == PlayerNum.Player1) {
            return player1.getY();
        } else {
            return player2.getY();
        }
    }

    @Override
    public String wallsXPos() {
        return "";
    }

    @Override
    public String wallsYPos() {
        return "";
    }


    @Override
    public String bombsPos() {
        StringBuilder bombsPos = new StringBuilder("[");
        for(int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                if(bombMap[i][j]){
                    bombsPos.append(i).append(",").append(j).append(",");
                }
            }
        }
        bombsPos.deleteCharAt(bombsPos.length() - 1);
        return bombsPos.append("]").toString();
    }

    public String firePos(){
        StringBuilder firePos = new StringBuilder("[");
        for(Fire fire : fires){
            firePos.append(fire.getxPos()).append(",").append(fire.getyPos()).append(",");
        }
        firePos.deleteCharAt(firePos.length() - 1);
        return firePos.append("]").toString();
    }

    public String activeBombPos(){
        StringBuilder actBombPos = new StringBuilder("[");
        for(ActiveBomb bomb : activeBombs){
            actBombPos.append(bomb.getxPos()).append(",").append(bomb.getyPos()).append(",");
        }
        actBombPos.deleteCharAt(actBombPos.length() - 1);
        return actBombPos.append("]").toString();
    }

    @Override
    public void setup(int p1x, int p1y, int p2x, int p2y, int[] wallsXPos, int[] wallsYPos) {
        for (int i = 0; i < wallsXPos.length; i++) {
           walls[wallsXPos[i]][wallsYPos[i]] = true;
        }

        player1 = new Player(this, p1x, p1y);
        player2 = new Player(this, p2x, p2y);
    }

    @Override
    public String winner() {
        return winner;
    }


    public Player getPlayer1(){
        return player1;
    }

    public Player getPlayer2(){
        return player2;
    }


}
