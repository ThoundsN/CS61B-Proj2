package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

import java.util.Random;

import static byog.Core.World.watch;


public class Game {

    private static TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    Random seedRandomizer ;
    long seedvalue;
    String fatWASDstring = "";

    private long seedKeeper(String input){
        String seedval = "";
        for(int i = 0; i < input.length(); i++){
            char x = input.charAt(i);
            if( x >= 48 && x < 58){
                seedval += x;
            }
        }
        seedvalue = Long.parseLong(seedval);
        return seedvalue;
    }

    private Random seedMaker(String input){
        String seedval = "";
        for(int i = 0; i < input.length(); i++){
            char x = input.charAt(i);
            if( x >= 48 && x < 58){
                seedval += x;
            }
        }
        seedvalue = Long.parseLong(seedval);
        return new Random(seedvalue);
    }

    private String[] stringReader(String input){
       String[] movementstring = new String[input.length()];
       boolean s = true;
       int index = 0;
       for(int i = 0; i < input.length(); i++){
           char x = input.charAt(i);
           if(x == 65|| x== 97||  x== 68||x == 100||x == 83 ||x== 115
           || x==87|| x== 119||  x== 113|| x == 81 || x== 58 ){
               if( x == 83 || x== 115 && s){
                   s = false;
               }
               else {
                   movementstring[index] = Character.toString(x);
                   index ++;
               }
           }
       }
       int notnull = 0;
       for(int i = 0; i <= index; i++){
           if(movementstring[i] != null){
               notnull ++;
           }
       }
       String[] movementstringnuonull = new String[notnull];
       for (int i = 0; i <= notnull; i++){
           movementstringnuonull[i] = movementstring[i];
       }
       return  movementstringnuonull;

    }

//    public Random nSeedReadKeyboard(){
//        String s;
//        String seedstring = "";
//        StdDraw.pause(100);
//        while (true){
//            if (StdDraw.hasNextKeyTyped()){
//                s = Character.toString(StdDraw.nextKeyTyped());
//                if (s.charAt(0) <= 48 && s.charAt(0) <58){
//                    seedstring += s;
//                    while (true) {
//                        drawFrame(seedstring);
//                        if (StdDraw.hasNextKeyTyped()){
//                            s = Character.toString(StdDraw.nextKeyTyped());
//                            if (s.equals("s")|| s.equals("S")){
//                                StdDraw.pause(100);
//                                gameStart();
//                                break;
//                            }
//                            StdDraw.pause(10);
//                            seedstring += s;
//                        }
//                    }
//                }
//            }
//            this.seedvalue = seedKeeper(seedstring);
//            return seedMaker(seedstring);
//        }
//
//    }

    public Random nSeedReadKeyboard(){
        String seed = "";
        char userInput;
        while (true){
            if (StdDraw.hasNextKeyTyped()){
                drawFrame(seed);
                userInput = StdDraw.nextKeyTyped();
                if (userInput == 's' || userInput == 'S'){
                    gameStart();
                    break;
                }
                seed = seed + userInput;
            }
        }
        this.seedvalue = seedKeeper(seed);
        return seedMaker(seed);

    }

    public void mouseRead(World worldobj, String time){
        while (true){
            int x = (int)StdDraw.mouseX();
            int y = (int)StdDraw.mouseY();

            if (x< 49 && y <49){
                TETile mouseTile = worldobj.world[x][y];
                Font font = new Font("Monaco", Font.BOLD, 10);
                StdDraw.setFont(font);
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.text(5, HEIGHT + 0.5, time);
                StdDraw.text(47, HEIGHT + 0.5, "Flower count: " + worldobj.getFlower);
                StdDraw.text(40, HEIGHT + 0.5, "Zombies remaining: " + worldobj.zombies + " |");
                if (mouseTile.equals(Tileset.WALL)) {
                    StdDraw.text(25, HEIGHT + 0.5, "Wall: Built from logs.");
                }
                if (mouseTile.equals(Tileset.NOTHING)) {
                    StdDraw.text(25, HEIGHT + 0.5, "Stone: Simple as that.");
                }
                if (mouseTile.equals(Tileset.ENEMY)) {
                    StdDraw.text(25, HEIGHT + 0.5, "Zombie: Raaaahhhh.");
                }
                if (mouseTile.equals(Tileset.FLOWER)) {
                    StdDraw.text(25, HEIGHT + 0.5, "Flower: Cute lil' red petals!");
                }
                if (mouseTile.equals(Tileset.PLAYER)) {
                    StdDraw.text(25, HEIGHT + 0.5, "Player: His name is Steven.");
                }
                StdDraw.show();

            }
            break;
        }
    }

    private void liveKeyReader(World worldObj){
        String[] sa = new String[1];
        int index = 0;
        String s;
        Keyreader reader = new Keyreader();
        while (true){
            mouseRead(worldObj, watch.TimeRecorder());
            if (StdDraw.hasNextKeyTyped()){
                //Single character string of typed input
                s = Character.toString(StdDraw.nextKeyTyped());
                //records the player movement into a single array
                sa[index] = s;
                boolean b =reader.reader(sa,worldObj);
                worldObj.fatWASDstring += s;
                if (s == "q" || s =="Q" || !b){
                    break;
                }
                ter.renderFrame(worldObj.world);
            }
        }
    }

    public void gameInterface() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH * 16);
        StdDraw.setYscale(0, HEIGHT * 16);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }
    private void drawFrame(String s){
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(25 * 16, 40 * 16, s);
        StdDraw.text(25 * 16, 35 * 16, "Type in S after you are done typing the seed.");
        StdDraw.show();
    }

    private void gameStart() {
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.text(25 * 16, 40 * 16, "GAME LOADING...");
        StdDraw.show();
    }
    public void drawMenu() {
        //Take the string and display it in the center of the screen
        //If game is not over, display relevant game information at the top of the screen
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.text(25 * 16, 40 * 16, "CS61B: THE GAME");
        Font smallfont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(smallfont);
        StdDraw.text(25 * 16, 30 * 16, "New Game (N)");
        StdDraw.text(25 * 16, 28 * 16, "Load Game (L)");
        StdDraw.text(25 * 16, 26 * 16, "Quit (Q)");
        Font smallerfont = new Font("Monaco", Font.BOLD, 16);
        StdDraw.setFont(smallerfont);
        StdDraw.text(25 * 16, 23 * 16, "=== R u l e s ===");
        StdDraw.text(25 * 16, 22 * 16, "1. Don't touch zombies until you eat at least 6 flowers, otherwise you'll lose.");
        StdDraw.text(25 * 16, 21 * 16, "2. Touching walls will put you back at the start.");
        StdDraw.text(25 * 16, 20 * 16, "3. Eat all 10 zombies to win. Have fun!");
        StdDraw.show();
    }
    private void afterNmenu() {
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.text(25 * 16, 40 * 16, "TYPE IN YOUR SEED");
        StdDraw.show();
    }

    public void gameSaved(String time) {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH * 16);
        StdDraw.setYscale(0, HEIGHT * 16);
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(25 * 16, 35 * 16, "Congrats! Your game is saved.");
        StdDraw.text(25 * 16, 15 * 16, time);
        StdDraw.show();
    }


    public void gameOver(long seed, String time) {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH * 16);
        StdDraw.setYscale(0, HEIGHT * 16);
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(25 * 16, 28 * 16, "YOU DIED!(ೆ௰ೆ๑)");
        Font font2 = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font2);
        StdDraw.text(25 * 16, 25 * 16, "Try seed " + seed + " again!");
        StdDraw.text(25 * 16, 20 * 16, time);
        StdDraw.show();
    }

    public void win(long seed, String time) {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH * 16);
        StdDraw.setYscale(0, HEIGHT * 16);
        StdDraw.clear();
        Font font = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.CYAN);
        StdDraw.text(25 * 16, 28 * 16, "YOU WIN!ヾ(´▽｀*)ﾉ☆");
        Font font2 = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font2);
        StdDraw.text(25 * 16, 25 * 16, "Seed defeated: " + seed);
        StdDraw.text(25 * 16, 20 * 16, time);
        StdDraw.show();
    }



    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        gameInterface();
        drawMenu();
        while (true){
            if (StdDraw.hasNextKeyTyped()){
            String potentialInput = Character.toString(StdDraw.nextKeyTyped());
            if (potentialInput.equals("n") || potentialInput.equals("N")){
                afterNmenu();
                StdDraw.pause(100);
                Random seedrandom = nSeedReadKeyboard();
                StdDraw.pause(100);
                World worldObj = new World();
                worldObj.seed = this.seedvalue;
                worldObj.world = worldObj.worldgenetator(seedrandom);
                ter.renderFrame(worldObj.world);
                StdDraw.pause(100);
                watch.start();
                while (true){
                    liveKeyReader(worldObj);
                    mouseRead(worldObj, watch.TimeRecorder());
                    this.fatWASDstring = worldObj.fatWASDstring;
                }
            }
            if (potentialInput.equals("L")||potentialInput.equals("l")){
                CurrentGame loadgame = new CurrentGame();
                World worldObj = loadgame.loadingGame();
                ter.initialize(WIDTH,HEIGHT);
                ter.renderFrame(worldObj.world);
                StdDraw.pause(100);
                watch.start();
                while (true){
                    liveKeyReader(worldObj);
                    mouseRead(worldObj, watch.TimeRecorder());
                    this.fatWASDstring = worldObj.fatWASDstring;
                }
            }
            }
        }
    }


    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        if (input.charAt(0) == 76|| input.charAt(0) == 108){
            CurrentGame loadgame = new CurrentGame();
            loadgame.loadingGame();
        }

        return internalPlayWithInputString(input).world;
    }

    public World internalPlayWithInputString(String input){
        ter.initialize(50,50 );
        seedvalue = seedKeeper(input);
        seedRandomizer = seedMaker(input);
        World worldobj = new World();
        worldobj.world = worldobj.worldgenetator(seedRandomizer);
        worldobj.seed = seedvalue;
        ter.renderFrame(worldobj.world);
        return worldobj;
    }
}
