package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import java.io.*;

import static byog.Core.World.watch;

public class CurrentGame implements Serializable {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static TERenderer ter = new TERenderer();
    TETile[][] savedworld;
    long seed;
    int getFlower;
    int zombies;
    int playerX;
    int playerY;
    public static int initialx;
    public static int initialy;
    String fatWASDstring;

    CurrentGame(){

    }

    CurrentGame(World worldObj) {
        savedworld = worldObj.world;
        seed = worldObj.seed;
        getFlower = worldObj.getFlower;
        zombies = worldObj.zombies;
        playerX = worldObj.playerX;
        playerY = worldObj.playerY;
        initialx = worldObj.initialx;
        initialy = worldObj.initialy;
        fatWASDstring = worldObj.fatWASDstring;
    }

    public void savingGame() {
        File f = new File("./world.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(this);
            os.close();
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }

    }

    public World loadingGame() {
        File f = new File("./world.ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                CurrentGame loadedworld = (CurrentGame)os.readObject();
                World worldObj = new World(loadedworld);
                return worldObj;
//                ter.renderFrame(worldObj.world);
//                watch.start();
//                Game game = new Game();
//                game.playWithKeyboardAfterLoad(worldObj);
                // add something about key reader
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        return null;
    }

}
