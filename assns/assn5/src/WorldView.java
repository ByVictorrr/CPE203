import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

final class WorldView
{
   private PApplet screen;
   private WorldModel world;
   private int tileWidth;
   private int tileHeight;
   private Viewport viewport;

   public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
      int tileWidth, int tileHeight)
   {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.viewport = new Viewport(numRows, numCols);
   }


   public PApplet getScreen(){return screen;}

   public WorldModel getWorld(){return world;}

   public int getTileWidth(){return tileWidth;}

   public int getTileHeight(){return tileHeight;}

   public Viewport getViewPort(){return viewport;}


   public static int clamp(int value, int low, int high)
   {
      return Math.min(high, Math.max(value, low));
   }

   public void shiftView( int colDelta, int rowDelta)
   {
      int newCol = clamp(this.viewport.getCol() + colDelta, 0,
              this.world.getNumCols() - this.viewport.getNumCols());
      int newRow = clamp(this.viewport.getRow() + rowDelta, 0,
              this.world.getNumRows() - this.viewport.getNumRows());

      viewport.shift(newCol, newRow);
   }

   public void drawBackground()
   {
      for (int row = 0; row <this.viewport.getNumRows(); row++)
      {
         for (int col = 0; col < this.viewport.getNumCols(); col++)
         {
            Point worldPoint = this.viewport.viewportToWorld(col, row);
            Optional<PImage> image = world.getBackgroundImage(worldPoint);
            if (image.isPresent())
            {
               this.screen.image(image.get(), col * this.tileWidth,
                       row * this.tileHeight);
            }
         }
      }
   }

   public void drawEntities()
   {
      for (Entity entity : this.getWorld().getEntities())
      {
         Point pos = entity.getPosition();
         if (this.viewport.contains(pos))
         {
            Point thisPoint = this.viewport.worldToViewport(pos.getX(), pos.getY());
            this.screen.image(entity.getCurrentImage(), thisPoint.getX() * this.tileWidth, thisPoint.getY() * this.tileHeight);
         }
      }
   }

   public void drawViewport()
   {
      drawBackground();
      drawEntities();
   }

}
