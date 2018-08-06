package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.io.Serializable;
import java.util.Random;

public class World extends Game implements Serializable{
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static TERenderer ter = new TERenderer();
    TETile[][] world = new TETile[WIDTH][HEIGHT];
    long seed;
    int getFlower = 0;
    int zombies = 10;
    int playerX;
    int playerY;
    public static int initialx;
    public static int initialy;
    String fatWASDstring;

    public World(){

    }

    public World(CurrentGame loadingGame){
        world = loadingGame.savedworld;
        seed = loadingGame.seed;
        getFlower = loadingGame.getFlower;
        zombies = loadingGame.zombies;
        playerX = loadingGame.playerX;
        playerY = loadingGame.playerY;
        initialx = loadingGame.initialx;
        initialy = loadingGame.initialy;
        fatWASDstring = loadingGame.fatWASDstring;
    }

    private void base() {
        ter.initialize(WIDTH, HEIGHT);
        System.out.println("initialize1");
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void rooms(Random randomseed) {
        int n = 60;

        while (n > 0) {
            int roomw = randomseed.nextInt(5) + 1;
            int roomh = randomseed.nextInt(5) + 1;
            int roomx = randomseed.nextInt(WIDTH - 2 - roomw) + 1;
            int roomy = randomseed.nextInt(HEIGHT - 2 -roomh) + 1;

            for (int x = roomx; x < roomx + roomw; x++) {
                for (int y = roomy; y < roomy + roomh; y++) {
                    world[x][y] = Tileset.FLOOR;
                }
            }
            n--;
        }
    }

    private void halls() {
        int range = 15;

        for (int x = 0; x < WIDTH -1 ; x++) {
            for (int y = 0; y < HEIGHT -1; y++) {
                if (world[x][y] == Tileset.FLOOR) {
                    if (world[x + 1][y] == Tileset.NOTHING && world[x][y + 1] == Tileset.NOTHING) {
                        int room1x = x;
                        int room1y = y;
                        for (int x1 = room1x; x1 < room1x + range && x1 < WIDTH && x1 > 0; x1++) {
                            for (int y1 = room1y; y1 < room1y + range && y1 < HEIGHT && y1 > 0; y1++) {
                                if (world[x1][y1] == Tileset.FLOOR) {
                                    int room2x = x1;
                                    int room2y = y1;
                                    while (room1x < room2x) {
                                        world[room1x][room1y] = Tileset.FLOOR;
                                        room1x++;
                                    }
                                    while (room1y < room2y) {
                                        world[room2x][room1y] = Tileset.FLOOR;
                                        room1y++;
                                    }
                                }
                            }
                        }
                        for (int x2 = room1x; x2 < room1x + range && x2 < WIDTH && x2 > 0; x2++) {
                            for (int y2 = room1y; y2 > room1y - range && y2 > 0 && y2 <HEIGHT; y2--) {
                                if (world[x2][y2] == Tileset.FLOOR) {
                                    int room3x = x2;
                                    int room3y = y2;
                                    while (room1y > room3y) {
                                        world[room3x][room1y] = Tileset.FLOOR;
                                        room1y--;

                                    }
                                    while (room1x < room3x) {
                                        world[room1x][room1y] = Tileset.FLOOR;
                                        room1x++;

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private  void walls(){
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if(world[x][y] == Tileset.FLOOR){
                    if(world[x+1][y] == Tileset.NOTHING){
                        world[x+1][y] = Tileset.WALL;
                    }
                    if(world[x+1][y+1] == Tileset.NOTHING){
                        world[x+1][y+1] = Tileset.WALL;
                    }
                    if(world[x][y+1] == Tileset.NOTHING){
                        world[x][y+1] = Tileset.WALL;
                    }
                    if(world[x-1][y+1] == Tileset.NOTHING){
                        world[x-1][y+1] = Tileset.WALL;
                    }
                    if(world[x-1][y] == Tileset.NOTHING){
                        world[x-1][y] = Tileset.WALL;
                    }
                    if(world[x-1][y-1] == Tileset.NOTHING){
                        world[x-1][y-1] = Tileset.WALL;
                    }
                    if(world[x][y-1] == Tileset.NOTHING){
                        world[x][y-1] = Tileset.WALL;
                    }
                    if(world[x+1][y-1] == Tileset.NOTHING){
                        world[x+1][y-1] = Tileset.WALL;
                    }

                }
            }
        }

    }

    public int startingtileX() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (world[x][y] == Tileset.FLOOR) {
                    initialx = x;
                    return x;
                }
            }
        }
        return 0;
    }

    public int startingtileY() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (world[x][y] == Tileset.FLOOR) {
                    initialy = y;
                    return y;
                }
            }
        }
        return 0;
    }

    public void player(){
        this.playerX = startingtileX();
        this.playerY = startingtileY();
        world[playerX][playerY] = Tileset.PLAYER;
    }

    public static Time watch = new Time();

    public boolean playerUp(){
        if (world[playerX][playerY+1] == Tileset.FLOOR){
            world[playerX][playerY] = Tileset.FLOOR;
            playerY += 1;
            world[playerX][playerY] = Tileset.PLAYER;
            return true;
        }
        if (world[playerX][playerY+1] == Tileset.WALL){
            world[playerX][playerY] = Tileset.FLOOR;
            playerY = initialy;
            playerX = initialx;
            world[playerX][playerY] = Tileset.PLAYER;
            return true;
        }
        if (world[playerX][playerY+1] == Tileset.FLOWER){
            world[playerX][playerY] = Tileset.FLOOR;
            playerY ++;
            world[playerX][playerY] = Tileset.PLAYER;
            getFlower++;
            return true;
        }
        if (world[playerX][playerY+1] == Tileset.ENEMY){
            if (getFlower>= 5) {
                world[playerX][playerY] = Tileset.FLOOR;
                playerY++;
                world[playerX][playerY ] = Tileset.PLAYER;
                zombies --;
                if (zombies == 0){
                    StdDraw.clear();
                    win(seed, watch.TimeRecorder());
                    StdDraw.show();
                    return false;
                }
                return true;
            }
            else {
                StdDraw.clear();
                gameOver(seed,watch.TimeRecorder());
                StdDraw.show();
                StdDraw.pause(4000);
                return false;
            }
            }
            return true;
    }
    //move player down "S"
    public boolean playerDown() {
        if (world[playerX][playerY - 1] == Tileset.FLOOR) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerY -= 1;
            world[playerX][playerY] = Tileset.PLAYER;
            return true;
        }
        if (world[playerX][playerY - 1] == Tileset.WALL) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerX = initialx;
            playerY = initialy;
            world[playerX][playerY] = Tileset.PLAYER;
            return true;
        }
        if (world[playerX][playerY - 1] == Tileset.FLOWER) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerY -= 1;
            getFlower += 1;
            world[playerX][playerY] = Tileset.PLAYER;
            return true;
        }
        if (world[playerX][playerY - 1] == Tileset.ENEMY) {
            if (getFlower > 5) {
                world[playerX][playerY] = Tileset.FLOOR;
                playerY -= 1;
                world[playerX][playerY] = Tileset.PLAYER;
                zombies -= 1;
                if (zombies == 0) {
                    StdDraw.clear();
                    win(seed, watch.TimeRecorder());
                    StdDraw.show();
                    return false;
                }
                return true;
            } else {
                StdDraw.clear();
                gameOver(seed, watch.TimeRecorder());
                StdDraw.show();
                StdDraw.pause(5000);
                return false;
            }
        }
        return true;
    }

    //move player right "D"
    public boolean playerRight() {
        if (world[playerX + 1][playerY] == Tileset.FLOOR) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerX += 1;
            world[playerX][playerY] = Tileset.PLAYER;
            return true;
        }
        if (world[playerX + 1][playerY] == Tileset.WALL) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerX = initialx;
            playerY = initialy;
            world[playerX][playerY] = Tileset.PLAYER;
            return true;
        }
        if (world[playerX + 1][playerY] == Tileset.FLOWER) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerX += 1;
            getFlower += 1;
            world[playerX][playerY] = Tileset.PLAYER;
            return true;
        }
        if (world[playerX + 1][playerY] == Tileset.ENEMY) {
            if (getFlower > 5) {
                world[playerX][playerY] = Tileset.FLOOR;
                playerX += 1;
                world[playerX][playerY] = Tileset.PLAYER;
                zombies -= 1;
                if (zombies == 0) {
                    StdDraw.clear();
                    win(seed, watch.TimeRecorder());
                    StdDraw.show();
                    return false;
                }
                return true;
            } else {
                StdDraw.clear();
                gameOver(seed, watch.TimeRecorder());
                StdDraw.show();
                StdDraw.pause(5000);
                return false;
            }
        }
        return true;
    }

    //move player left "A"
    public boolean playerLeft() {
        if (world[playerX - 1][playerY] == Tileset.FLOOR) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerX -= 1;
            world[playerX][playerY] = Tileset.PLAYER;
            return true;
        }
        if (world[playerX - 1][playerY] == Tileset.WALL) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerX = initialx;
            playerY = initialy;
            world[playerX][playerY] = Tileset.PLAYER;
            return true;
        }
        if (world[playerX - 1][playerY] == Tileset.FLOWER) {
            world[playerX][playerY] = Tileset.FLOOR;
            playerX -= 1;
            getFlower += 1;
            world[playerX][playerY] = Tileset.PLAYER;
            return true;
        }
        if (world[playerX - 1][playerY] == Tileset.ENEMY) {
            if (getFlower > 5) {
                world[playerX][playerY] = Tileset.FLOOR;
                playerX -= 1;
                world[playerX][playerY] = Tileset.PLAYER;
                zombies -= 1;
                if (zombies == 0) {
                    StdDraw.clear();
                    win(seed, watch.TimeRecorder());
                    StdDraw.show();
                    return false;
                }
                return true;
            } else {
                StdDraw.clear();
                gameOver(seed, watch.TimeRecorder());
                StdDraw.show();
                StdDraw.pause(5000);
                return false;
            }
        }
        return true;
    }

    public void zombieSpawn(Random r) {
        int count = zombies;
        while (true) {
            int x = r.nextInt(49);
            int y = r.nextInt(49);
            if (world[x][y] == Tileset.FLOOR && count > 0) {
                world[x][y] = Tileset.ENEMY;
                count = count - 1;
                if (count == 0) {
                    break;
                }
            }
        }
    }
    public void flowers(Random random){
        int count = random.nextInt(15) + 15;
        while (true){
            int x = random.nextInt(49);
            int y = random.nextInt(49);
            if (world[x][y] == Tileset.FLOOR && count >0){
                world[x][y] = Tileset.FLOWER;
                count -- ;
                if (count == 0){
                    break;
                }
            }

        }

    }

    public TETile[][] worldgenetator(Random seedRandomizer){
        base();
        rooms(seedRandomizer);
        halls();
        walls();
        player();
        flowers(seedRandomizer);
        zombieSpawn(seedRandomizer);

        return world;

    }
}
