import java.util.Objects;

final class Song
{
   private final String artist;
   private final String title;
   private final int year;

   public Song(final String artist, final String title, final int year)
   {
      this.artist = artist;
      this.title = title;
      this.year = year;
   }

   public String getArtist()
   {
      return artist;
   }

   public String getTitle()
   {
      return title;
   }

   public int getYear()
   {
      return year;
   }

   public String toString()
   {
      return "\"" + title + "\" by " + artist + " (" + year + ")";
   }

   public boolean equals(final Object other)
   {
      if (this == other)
      {
         return true;
      }

      return other != null
         && other.getClass() == getClass()
         && Objects.equals(artist, ((Song)other).artist)
         && Objects.equals(title, ((Song)other).title)
         && year == ((Song)other).year;
   }

   public int hashCode()
   {
      return Objects.hash(artist, title, year);
   }
}

