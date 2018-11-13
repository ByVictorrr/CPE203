import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import processing.core.PImage;
import processing.core.PApplet;

final class Functions {
   public static final Random rand = new Random();

   public static final int COLOR_MASK = 0xffffff;
   public static final int KEYED_IMAGE_MIN = 5;
   private static final int KEYED_RED_IDX = 2;
   private static final int KEYED_GREEN_IDX = 3;
   private static final int KEYED_BLUE_IDX = 4;

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




   public static List<PImage> getImageList(ImageStore imageStore, String key) {
      return imageStore.images.getOrDefault(key, imageStore.defaultImages);
   }



   public static void loadImages(Scanner in, ImageStore imageStore,
                                 PApplet screen) {
      int lineNumber = 0;
      while (in.hasNextLine()) {
         try {
            processImageLine(imageStore.images, in.nextLine(), screen);
         } catch (NumberFormatException e) {
            System.out.println(String.format("Image format error on line %d",
                    lineNumber));
         }
         lineNumber++;
      }
   }


   public static void processImageLine(Map<String, List<PImage>> images,
                                       String line, PApplet screen) {
      String[] attrs = line.split("\\s");
      if (attrs.length >= 2) {
         String key = attrs[0];
         PImage img = screen.loadImage(attrs[1]);
         if (img != null && img.width != -1) {
            List<PImage> imgs = getImages(images, key);
            imgs.add(img);

            if (attrs.length >= KEYED_IMAGE_MIN) {
               int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
               int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
               int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
               setAlpha(img, screen.color(r, g, b), 0);
            }
         }
      }
   }


   public static List<PImage> getImages(Map<String, List<PImage>> images, String key) {
      List<PImage> imgs = images.get(key);
      if (imgs == null) {
         imgs = new LinkedList<>();
         images.put(key, imgs);
      }
      return imgs;
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
         world.setBackground(pt, new Background(id, getImageList(imageStore, id)));
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
                 getImageList(imageStore, MINER_KEY));
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
                 pt, getImageList(imageStore, OBSTACLE_KEY));
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
                 getImageList(imageStore, ORE_KEY));
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
                 pt, getImageList(imageStore, SMITH_KEY));
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
                 getImageList(imageStore, VEIN_KEY));

         world.tryAddEntity(entity);
      }

      return properties.length == VEIN_NUM_PROPERTIES;
   }

}


