import java.time.LocalTime;

class CourseSection
{
   private final String prefix;
   private final String number;
   private final int enrollment;
   private final LocalTime startTime;
   private final LocalTime endTime;

   public CourseSection(final String prefix, final String number,
      final int enrollment, final LocalTime startTime, final LocalTime endTime)
   {
      this.prefix = prefix;
      this.number = number;
      this.enrollment = enrollment;
      this.startTime = startTime;
      this.endTime = endTime;
   }

   public int hashCode()
   {
        return prefix.hashCode()+number.hashCode()+enrollment+startTime.hashCode()+endTime.hashCode();

   }


 public boolean equals(Object other)
{
	if (other == null){
	
	return false;
	}
	if ( other instanceof CourseSection)
	{
	CourseSection o = (CourseSection)other;
	
	return o.hashCode() == this.hashCode();

	}
	return false;
}


}

   // additional likely methods not defined since they are not needed for testing
