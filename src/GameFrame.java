import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class GameFrame extends JFrame {

    private KeyInput keyInput;
    private JPanel mapPanel;
    private JPanel bombPanel;

    private JLabel p1bombs, p2bombs;

    private final int GRID_SIZE = 24;

    private JLabel[][] labelGrid = new JLabel[GRID_SIZE][GRID_SIZE];

    private int[] wallsX, wallsY;

    private Image wall, fire, bomb, actBomb, p1, p2;

    GameFrame() {

        loadImages();

        keyInput = new KeyInput();
        addKeyListener(keyInput);

        mapPanel = new JPanel();
        mapPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        mapPanel.setOpaque(true);
        mapPanel.setBackground(Color.GRAY);

        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                JLabel label = new JLabel();
                labelGrid[i][j] = label;
                mapPanel.add(label);
            }
        }

        mapPanel.setPreferredSize(new Dimension(700, 700));

        getContentPane().add(mapPanel, BorderLayout.CENTER);

        bombPanel = new JPanel(new GridLayout(1, 3));
        p1bombs = new JLabel("Player 1 bombs: 0");
        p1bombs.setForeground(new Color(123, 71, 39));
        p2bombs = new JLabel("Player 2 bombs: 0");
        p2bombs.setForeground(new Color(212, 33, 159));
        bombPanel.add(p1bombs);
        bombPanel.add(new Container());
        bombPanel.add(p2bombs);

        getContentPane().add(bombPanel, BorderLayout.NORTH);

        setResizable(false);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadImages(){
        int scale = 29;

        try {
            BufferedImage img = ImageIO.read(new FileInputStream("res/wall.png"));
            wall = img.getScaledInstance(scale, -1, Image.SCALE_DEFAULT);
            img = ImageIO.read(new FileInputStream("res/p1.png"));
            p1 = img.getScaledInstance(scale, -1, Image.SCALE_DEFAULT);
            img = ImageIO.read(new FileInputStream("res/p2.png"));
            p2 = img.getScaledInstance(scale, -1, Image.SCALE_DEFAULT);
            img = ImageIO.read(new FileInputStream("res/bombUnAct.png"));
            bomb = img.getScaledInstance(scale, -1, Image.SCALE_DEFAULT);
            img = ImageIO.read(new FileInputStream("res/bomb.png"));
            actBomb = img.getScaledInstance(scale, -1, Image.SCALE_DEFAULT);
            img = ImageIO.read(new FileInputStream("res/fire.png"));
            fire = img.getScaledInstance(scale, -1, Image.SCALE_DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void quit() {
        String[] options = {"Yes", "No"};
        String result = optionDialogString("Exit the application?", "Quit", options);
        if (result.equals("Yes")) {
            System.exit(0);
        }
    }

    private String optionDialogString(String message, String label, String[] choices){
        JLabel jLabel = new JLabel(message);
        JOptionPane optionPane = new JOptionPane(jLabel, JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, choices);
        JDialog dialog = optionPane.createDialog(this, label);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);
        return optionPane.getValue().toString();
    }

    String getDirection(){
        return keyInput.getDirection();
    }

    boolean isAttacking(){
        return keyInput.isAttacking();
    }

    void update(int p1x, int p1y, int p2x, int p2y, int p1bombs, int p2bombs, String firePos, String bombPos, String actBombPos){



        clearGrid();

        this.p1bombs.setText("Player 1 bombs: " + p1bombs);
        this.p2bombs.setText("Player 2 bombs: " + p2bombs);

        try {
            drawBombs(bombPos);
            drawFire(firePos);
            drawActiveBombs(actBombPos);
            drawWalls();

            //Draw players
            labelGrid[p1y][p1x].setIcon(new ImageIcon(p1));
            labelGrid[p2y][p2x].setIcon(new ImageIcon(p2));
        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println("p1: " + p1x + "," + p1y);
            System.err.println("p2: " + p2x + "," + p2y);
        }

    }

    void viewWinner(String winner){
        optionDialogString(winner, "Game Over", new String[]{"OK"});
        System.exit(0);
    }

    private void drawWalls(){
        for(int i = 0; i < wallsX.length ; i ++){
            labelGrid[wallsY[i]][wallsX[i]].setIcon(new ImageIcon(wall));
        }
    }

    private void drawFire(String firePos){
        String[] split = firePos.replace("[", "").replace("]", "").replace(" ", "").split(",");
        if(split.length > 1) {
            for (int i = 0; i < split.length; i += 2) {
                labelGrid[Integer.parseInt(split[i + 1])][Integer.parseInt(split[i])].setIcon(new ImageIcon(fire));
            }
        }

    }

    private void drawBombs(String bombPos){
        String[] split = bombPos.replace("[", "").replace("]", "").replace(" ", "").split(",");
        if(split.length > 1) {
            for (int i = 0; i < split.length; i += 2) {
                labelGrid[Integer.parseInt(split[i + 1])][Integer.parseInt(split[i])].setIcon(new ImageIcon(bomb));
            }
        }
    }

    private void drawActiveBombs(String actBombPos){
        String[] split = actBombPos.replace("[", "").replace("]", "").replace(" ", "").split(",");
        if(split.length > 1) {
            for (int i = 0; i < split.length; i += 2) {
                labelGrid[Integer.parseInt(split[i + 1])][Integer.parseInt(split[i])].setIcon(new ImageIcon(actBomb));
            }
        }
    }

    private void clearGrid(){
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                labelGrid[i][j].setIcon(null);
            }
        }
    }

    void setWalls(String xPos, String yPos){
        String[] xs = xPos.replace("[", "").replace("]", "").replace(" ", "").split(",");
        String[] ys = yPos.replace("[", "").replace("]", "").replace(" ", "").split(",");

        wallsX = new int[xs.length];
        wallsY = new int[xs.length];

        for(int i = 0; i < xs.length ; i ++){
            wallsX[i] = Integer.parseInt(xs[i]);
            wallsY[i] = Integer.parseInt(ys[i]);
        }

    }

    private class KeyInput extends KeyAdapter{

        private boolean [] keys;
        //private boolean isAttacking;

        KeyInput(){
            keys = new boolean[256];
        }

        @Override
        public void keyPressed(KeyEvent e) {
            keys[e.getKeyCode()] = true;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keys[e.getKeyCode()] = false;
        }

        String getDirection() {
            if(keys[KeyEvent.VK_DOWN]) return "down";
            else if(keys[KeyEvent.VK_LEFT]) return "left";
            else if(keys[KeyEvent.VK_UP]) return "up";
            else if(keys[KeyEvent.VK_RIGHT]) return "right";
            else  return "none";
        }

        boolean isAttacking(){
            return keys[KeyEvent.VK_SPACE];
        }
    }
}
