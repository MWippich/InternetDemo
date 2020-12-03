import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {

    private static GameFrame frame;

    public static void main(String[] args) {

        frame = new GameFrame();

        DataOutputStream dataOut;
        DataInputStream dataIn;


        try{
            System.out.println("Väntar på annan spelare!");
            Socket sckt = new Socket("localhost",1234);

            dataOut = new DataOutputStream(sckt.getOutputStream());
            dataIn = new DataInputStream(sckt.getInputStream());

            String wallsX = dataIn.readUTF();
            dataOut.writeByte(1);
            String wallsY = dataIn.readUTF();
            dataOut.writeByte(1);

            frame.setWalls(wallsX, wallsY);


            do{

                dataIn.readByte();
                dataOut.writeByte(frame.isAttacking()?1:0);
                dataIn.readByte();
                dataOut.writeUTF(frame.getDirection());

                int p1x = dataIn.readInt();
                dataOut.writeByte(1);
                int p1y = dataIn.readInt();
                dataOut.writeByte(1);
                int p2x = dataIn.readInt();
                dataOut.writeByte(1);
                int p2y = dataIn.readInt();
                dataOut.writeByte(1);
                int p1bombs = dataIn.readInt();
                dataOut.writeByte(1);
                int p2bombs = dataIn.readInt();
                dataOut.writeByte(1);
                String firePos = dataIn.readUTF();
                dataOut.writeByte(1);
                String bombPos = dataIn.readUTF();
                dataOut.writeByte(1);
                String actBombPos = dataIn.readUTF();
                dataOut.writeByte(1);
                String winner = dataIn.readUTF();

                frame.update(p1x, p1y, p2x, p2y, p1bombs, p2bombs, firePos, bombPos, actBombPos);

                if (!winner.equals("None")){
                    frame.viewWinner(winner);
                }


            } while(true);

        }catch(IOException ioe){
            System.out.println("nagot IO fel intraffade!"+ioe);
        }
    }

}
