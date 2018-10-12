import java.util.*;

final class EventScheduler
{
   public PriorityQueue<Event> eventQueue;
   public Map<Entity, List<Event>> pendingEvents;
   public double timeScale;

   public EventScheduler(double timeScale)
   {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.timeScale = timeScale;
   }

   public static void scheduleEvent(EventScheduler scheduler,
                                    Entity entity, Action action, long afterPeriod)
   {
      long time = System.currentTimeMillis() +
              (long)(afterPeriod * scheduler.timeScale);
      Event event = new Event(action, time, entity);

      scheduler.eventQueue.add(event);

      // update list of pending events for the given entity
      List<Event> pending = scheduler.pendingEvents.getOrDefault(entity,
              new LinkedList<>());
      pending.add(event);
      scheduler.pendingEvents.put(entity, pending);
   }

   public static void unscheduleAllEvents(EventScheduler scheduler,
                                          Entity entity)
   {
      List<Event> pending = scheduler.pendingEvents.remove(entity);

      if (pending != null)
      {
         for (Event event : pending)
         {
            scheduler.eventQueue.remove(event);
         }
      }
   }

   public static void removePendingEvent(EventScheduler scheduler,
                                         Event event)
   {
      List<Event> pending = scheduler.pendingEvents.get(event.entity);

      if (pending != null)
      {
         pending.remove(event);
      }
   }

   public static void updateOnTime(EventScheduler scheduler, long time)
   {
      while (!scheduler.eventQueue.isEmpty() &&
              scheduler.eventQueue.peek().time < time)
      {
         Event next = scheduler.eventQueue.poll();

         removePendingEvent(scheduler, next);

         executeAction(next.action, scheduler);
      }
   }

}
