import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {

    private static GameInterface game;

    public static void main(String[] args) throws IOException{

        ServerSocket serverSckt = new ServerSocket(1234);

        while (true){

            int[] wallsX = new int[]{0, 0, 5}, wallsY = new int[]{0, 1, 5};


            game = new BombIt();
            game.setup(1, 1, 22, 22, wallsX, wallsY);

            //socket 1 = player 1
            Socket sckt1 = serverSckt.accept();

            DataInputStream dataIn = new DataInputStream(sckt1.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(sckt1.getOutputStream());
            dataOut.writeUTF(Arrays.toString(wallsX));
            dataIn.readByte();
            dataOut.writeUTF(Arrays.toString(wallsY));
            dataIn.readByte();

            //socket 2 = player 2
            Socket sckt2 = serverSckt.accept();

            dataIn = new DataInputStream(sckt2.getInputStream());
            dataOut = new DataOutputStream(sckt2.getOutputStream());
            dataOut.writeUTF(Arrays.toString(wallsX));
            dataIn.readByte();
            dataOut.writeUTF(Arrays.toString(wallsY));
            dataIn.readByte();

            long lastTime = System.nanoTime();
            double delta = 0;
            long now;

            do{

                now = System.nanoTime();
                delta += (now - lastTime) / (1000000000D/8);
                lastTime = now;



                if (delta >= 1) {
                    //reset delta
                    delta %= 1;

                    recieve(sckt1, GameInterface.PlayerNum.Player1);
                    recieve(sckt2, GameInterface.PlayerNum.Player2);

                    //spellogik
                    game.play();

                    send(sckt1);
                    send(sckt2);
                }

            } while (game.isOn());

            recieve(sckt1, GameInterface.PlayerNum.Player1);
            recieve(sckt2, GameInterface.PlayerNum.Player2);
            //spellogik
            game.play();

            send(sckt1);
            send(sckt2);

        }
    }

    private static void recieve(Socket sckt, GameInterface.PlayerNum player){
        boolean attacking = false;
        String direction = "none";

        try {

            DataInputStream dataIn = new DataInputStream(sckt.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(sckt.getOutputStream());
            dataOut.writeByte(1);
            attacking = dataIn.readByte() == 1;
            dataOut.writeByte(1);
            direction = dataIn.readUTF();
        }catch(IOException ioe){
            System.out.println("nagot IO fel intraffade!"+ ioe);
            System.exit(-1);
        }

        game.setDirection(player, direction);
        game.setAttacking(player, attacking);
    }

    private static void send(Socket sckt){

        try {
            DataInputStream dataIn = new DataInputStream(sckt.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(sckt.getOutputStream());

            dataOut.writeInt(game.XPos(GameInterface.PlayerNum.Player1));
            dataIn.readByte();
            dataOut.writeInt(game.YPos(GameInterface.PlayerNum.Player1));
            dataIn.readByte();
            dataOut.writeInt(game.XPos(GameInterface.PlayerNum.Player2));
            dataIn.readByte();
            dataOut.writeInt(game.YPos(GameInterface.PlayerNum.Player2));
            dataIn.readByte();
            dataOut.writeInt(game.numBombs(GameInterface.PlayerNum.Player1));
            dataIn.readByte();
            dataOut.writeInt(game.numBombs(GameInterface.PlayerNum.Player2));
            dataIn.readByte();
            dataOut.writeUTF(game.firePos());
            dataIn.readByte();
            dataOut.writeUTF(game.bombsPos());
            dataIn.readByte();
            dataOut.writeUTF(game.activeBombPos());
            dataIn.readByte();
            dataOut.writeUTF(game.winner());

        }catch(IOException ioe){
            System.out.println("nagot IO fel intraffade!"+ ioe);
            System.exit(-1);
        }

    }

}
