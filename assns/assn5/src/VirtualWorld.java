import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import processing.core.*;

public final class VirtualWorld
   extends PApplet
{
   private static final int TIMER_ACTION_PERIOD = 100;

   private static final int VIEW_WIDTH = 640;
   private static final int VIEW_HEIGHT = 480;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 2;
   private static final int WORLD_HEIGHT_SCALE = 2;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private static final String IMAGE_LIST_FILE_NAME = "imagelist";
   private static final String DEFAULT_IMAGE_NAME = "background_default";
   private static final int DEFAULT_IMAGE_COLOR = 0x808080;

   private static final String LOAD_FILE_NAME = "gaia.sav";

   private static final String FAST_FLAG = "-fast";
   private static final String FASTER_FLAG = "-faster";
   private static final String FASTEST_FLAG = "-fastest";
   private static final double FAST_SCALE = 0.5;
   private static final double FASTER_SCALE = 0.25;
   private static final double FASTEST_SCALE = 0.10;
   public static final Random rand = new Random();

   public static final int COLOR_MASK = 0xffffff;


   public static final int PROPERTY_KEY = 0;

   public static final String BGND_KEY = "background";
   public static final int BGND_NUM_PROPERTIES = 4;
   public static final int BGND_ID = 1;
   public static final int BGND_COL = 2;
   public static final int BGND_ROW = 3;



   public static final String MINER_KEY = "miner";
   public static final int MINER_NUM_PROPERTIES = 7;
   public static final int MINER_ID = 1;
   public static final int MINER_COL = 2;
   public static final int MINER_ROW = 3;
   public static final int MINER_LIMIT = 4;
   public static final int MINER_ACTION_PERIOD = 5;
   public static final int MINER_ANIMATION_PERIOD = 6;

   public static final String OBSTACLE_KEY = "obstacle";
   public static final int OBSTACLE_NUM_PROPERTIES = 4;
   public static final int OBSTACLE_ID = 1;
   public static final int OBSTACLE_COL = 2;
   public static final int OBSTACLE_ROW = 3;

   public static final String ORE_KEY = "ore";
   public static final int ORE_NUM_PROPERTIES = 5;
   public static final int ORE_ID = 1;
   public static final int ORE_COL = 2;
   public static final int ORE_ROW = 3;
   public static final int ORE_ACTION_PERIOD = 4;

   public static final String SMITH_KEY = "blacksmith";
   public static final int SMITH_NUM_PROPERTIES = 4;
   public static final int SMITH_ID = 1;
   public static final int SMITH_COL = 2;
   public static final int SMITH_ROW = 3;

   public static final String VEIN_KEY = "vein";
   public static final int VEIN_NUM_PROPERTIES = 5;
   public static final int VEIN_ID = 1;
   public static final int VEIN_COL = 2;
   public static final int VEIN_ROW = 3;
   public static final int VEIN_ACTION_PERIOD = 4;


   private static final String PARTICLE_ACCELERATOR_KEY = "particleaccelerator";
   private static final String DR_WELLS_KEY = "drwells";
   private static final String FLASH_KEY = "flash";

   private static final String REVERSE_FLASH_KEY = "reverseflash";

   private static double timeScale = 1.0;
//dont need setters or getter because virtual world is the main class
   private ImageStore imageStore;
   private WorldModel world;
   private WorldView view;
   private EventScheduler scheduler;
   private boolean clicked;
   private long next_time;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
      clicked = false;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         this.scheduler.updateOnTime( time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      this.view.drawViewport();
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }
         this.view.shiftView(dx,dy);
      }
   }

   // When button is pressed
   public void mousePressed() {
      if(!clicked){  //if clicked dont change all blacksmiths into dr wells
         everyClick(1,1);
      clicked = true;
      }
      //hasnt been clicked
      else {
         spawnDrWells();
         everyClick(1,1);
      }
      redraw();
   }

   private  void spawnDrWells(){
      List<Entity> smith = world.getEntities().stream().collect(Collectors.toList());
      List <Blacksmith> realSmith = new ArrayList<>();
      smith.forEach(E-> { if ( E instanceof  Blacksmith){ realSmith.add((Blacksmith)E); }});
      List <Blacksmith> drWells = new ArrayList<>();
      realSmith.forEach(B-> { drWells.add(B); });
      drWells.forEach(B-> B.setImages(imageStore.getImageList(DR_WELLS_KEY)));

   }
   private void everyClick(int flashAnimationPeriod, int flashActionPeriod){

      ReverseFlash Eobard = ReverseFlash.createReverseFlash(REVERSE_FLASH_KEY, getPressedPoint(), 0, 0, imageStore.getImageList(REVERSE_FLASH_KEY));
      world.addEntity(Eobard);
      (Eobard).scheduleActions(scheduler, world, imageStore);
      //end of creating him in the gameB
      Background particleAccelerator = new Background(PARTICLE_ACCELERATOR_KEY, imageStore.getImageList(PARTICLE_ACCELERATOR_KEY));

      aroundPressedPoints(1).forEach(p -> {
         world.setBackground(p, particleAccelerator);
         if (isMinearAtPA(p) && world.getOccupant(p).isPresent()) {
            Miner minerOnPA = (Miner) (world.getOccupant(p).get());
            minerOnPA.setImages(imageStore.getImageList(FLASH_KEY));
            minerOnPA.setAnimationPeriod(flashAnimationPeriod);
            minerOnPA.setActionPeriod(flashActionPeriod);
         }});
   }

   ///check if miner is at the PA
   private boolean isMinearAtPA(Point pos){ return world.withinBounds(pos) && (world.getOccupancyCell(pos) instanceof MinerNotFull ||
                                                   world.getOccupancyCell(pos) instanceof MinerFull); }

   //sets and offset to a point
   private Point offSetPressedPoint(int i, int j){return new Point(getPressedPoint().getX()+i,getPressedPoint().getY()+j);}
   private Point getPressedPoint(){return new Point(mouseX/TILE_WIDTH , mouseY/TILE_HEIGHT);}

   //###
   //#*#
   //###
   private List<Point> aroundPressedPoints(int disAwayFromPressedPoint){

      return Arrays.asList(offSetPressedPoint(0,disAwayFromPressedPoint),
                      offSetPressedPoint(disAwayFromPressedPoint,0),
                      offSetPressedPoint(disAwayFromPressedPoint,disAwayFromPressedPoint),
                      offSetPressedPoint(0,-disAwayFromPressedPoint),
                      offSetPressedPoint(-disAwayFromPressedPoint,0),
                      offSetPressedPoint(-disAwayFromPressedPoint,-disAwayFromPressedPoint),
                      offSetPressedPoint(disAwayFromPressedPoint,-disAwayFromPressedPoint),
                      offSetPressedPoint(-disAwayFromPressedPoint,disAwayFromPressedPoint));
   }

   public static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
              imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   public static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         ImageStore.loadImages(in, imageStore, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         load(in, world, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.getEntities()) {
          if (entity instanceof Actioned) {
              ((Actioned) entity).scheduleActions(scheduler, world, imageStore);
          }
      }
   }

   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }


   /*
    Called with color for which alpha should be set and alpha value.
    setAlpha(img, color(255, 255, 255), 0));
  */
   public static void setAlpha(PImage img, int maskColor, int alpha) {
      int alphaValue = alpha << 24;
      int nonAlpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++) {
         if ((img.pixels[i] & COLOR_MASK) == nonAlpha) {
            img.pixels[i] = alphaValue | nonAlpha;
         }
      }
      img.updatePixels();
   }


   public static void load(Scanner in, WorldModel world, ImageStore imageStore) {
      int lineNumber = 0;
      while (in.hasNextLine()) {
         try {
            if (!processLine(in.nextLine(), world, imageStore)) {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         } catch (NumberFormatException e) {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         } catch (IllegalArgumentException e) {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   public static boolean processLine(String line, WorldModel world,
                                     ImageStore imageStore) {
      String[] properties = line.split("\\s");
      if (properties.length > 0) {
         switch (properties[PROPERTY_KEY]) {
            case BGND_KEY:
               return parseBackground(properties, world, imageStore);
            case MINER_KEY:
               return parseMiner(properties, world, imageStore);
            case OBSTACLE_KEY:
               return parseObstacle(properties, world, imageStore);
            case ORE_KEY:
               return parseOre(properties, world, imageStore);
            case SMITH_KEY:
               return parseSmith(properties, world, imageStore);
            case VEIN_KEY:
               return parseVein(properties, world, imageStore);
         }
      }

      return false;
   }

   public static boolean parseBackground(String[] properties,
                                         WorldModel world, ImageStore imageStore) {
      if (properties.length == BGND_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         world.setBackground(pt, new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   public static boolean parseMiner(String[] properties, WorldModel world,
                                    ImageStore imageStore) {
      if (properties.length == MINER_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
                 Integer.parseInt(properties[MINER_ROW]));
         Entity entity = MinerNotFull.createMinerNotFull(properties[MINER_ID],
                 Integer.parseInt(properties[MINER_LIMIT]),
                 pt,
                 Integer.parseInt(properties[MINER_ACTION_PERIOD]),
                 Integer.parseInt(properties[MINER_ANIMATION_PERIOD]),
                 imageStore.getImageList(MINER_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == MINER_NUM_PROPERTIES;
   }

   public static boolean parseObstacle(String[] properties, WorldModel world,
                                       ImageStore imageStore) {
      if (properties.length == OBSTACLE_NUM_PROPERTIES) {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         Entity entity = Obstacle.createObstacle(properties[OBSTACLE_ID],
                 pt, imageStore.getImageList(OBSTACLE_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   public static boolean parseOre(String[] properties, WorldModel world,
                                  ImageStore imageStore) {
      if (properties.length == ORE_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
                 Integer.parseInt(properties[ORE_ROW]));
         Entity entity = Ore.createOre(properties[ORE_ID],
                 pt, Integer.parseInt(properties[ORE_ACTION_PERIOD]),
                 imageStore.getImageList(ORE_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == ORE_NUM_PROPERTIES;
   }

   public static boolean parseSmith(String[] properties, WorldModel world,
                                    ImageStore imageStore) {

         if (properties.length == SMITH_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
                    Integer.parseInt(properties[SMITH_ROW]));
            Entity entity = Blacksmith.createBlacksmith(properties[SMITH_ID],
                    pt, imageStore.getImageList(SMITH_KEY));
            world.tryAddEntity(entity);

      }
      return properties.length == SMITH_NUM_PROPERTIES;
   }

   public static boolean parseVein(String[] properties, WorldModel world,
                                   ImageStore imageStore) {
      if (properties.length == VEIN_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
                 Integer.parseInt(properties[VEIN_ROW]));
         Entity entity = Vein.createVein(properties[VEIN_ID],
                 pt,
                 Integer.parseInt(properties[VEIN_ACTION_PERIOD]),
                 imageStore.getImageList(VEIN_KEY));

         world.tryAddEntity(entity);
      }

      return properties.length == VEIN_NUM_PROPERTIES;
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
