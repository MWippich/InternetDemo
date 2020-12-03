public class Player {

    private int x, y, numBombs;
    private String direction;
    private boolean attacking;

    private BombIt game;

    Player(BombIt game, int x, int y){
        this.game = game;
        this.x = x;
        this.y = y;
        direction = "none";
    }

    public void move(){
        switch (direction) {
            case "down":
                if(checkWallCollision(x, y + 1) && checkPlayerCollision(x, y + 1)){
                    y++;
                }
                break;
            case "right":
                if(checkWallCollision(x + 1, y) && checkPlayerCollision(x + 1, y)){
                    x++;
                }
                break;
            case "up":
                if(checkWallCollision(x, y - 1) && checkPlayerCollision(x, y - 1)){
                    y--;
                }
                break;
            case "left":
                if(checkWallCollision(x - 1, y) && checkPlayerCollision(x - 1, y)){
                    x--;
                }
                break;
            default:
                break;
        }

        pickUpBomb();
    }

    private boolean checkWallCollision(int x, int y){
        if(x >= game.mapSize || y >= game.mapSize || x < 0 || y < 0) return false;
        return !game.walls[x][y];
    }

    public boolean removeBomb(){
        if(numBombs <= 0){
            return false;
        } else {
            numBombs--;
            return true;
        }
    }

    private boolean checkPlayerCollision(int x, int y){
        if(game.getPlayer1().getX() == x && game.getPlayer1().getY() == y) {
            return false;
        }
        if(game.getPlayer2().getX() == x && game.getPlayer2().getY() == y) {
            return false;
        }
        return true;
    }


    public void placeBomb(){
        ActiveBomb bomb = new ActiveBomb(x, y);
        game.activeBombs.add(bomb);
    }


    private void pickUpBomb(){
        if(game.bombMap[x][y]){
            numBombs++;
            game.bombMap[x][y] = false;
        }

    }

    public boolean hitByBomb(){
        for(Fire fire : game.fires){
            if(fire.getxPos() == x && fire.getyPos() == y) {
                return true;
            }
        }
        return false;
    }


    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getNumBombs() {
        return numBombs;
    }

    public boolean isAttacking() {
        return attacking;
    }
}
