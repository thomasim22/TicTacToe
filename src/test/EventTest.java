package test;
import model.Event;
import model.Event.EventStatus;

public class EventTest {

    public static void main(String[] args) {
        testDefaultConstructor();
        testParameterizedConstructor();
        testGettersAndSetters();
        testEqualsMethod();
    }

    public static void testDefaultConstructor() {
        Event event = new Event();
        System.out.println("Testing Default Constructor:");
        System.out.println("Event ID: " + event.getEventID());
        System.out.println("Sender: " + event.getSender());
        System.out.println("Opponent: " + event.getOpponent());
        System.out.println("Status: " + event.getStatus());
        System.out.println("Turn: " + event.getTurn());
        System.out.println("Move: " + event.getMove());
    }

    public static void testParameterizedConstructor() {
        Event event = new Event(1, "Sender1", "Opponent1", EventStatus.PENDING, "Turn1", 3);
        System.out.println("Testing Parameterized Constructor:");
        System.out.println("Event ID: " + event.getEventID());
        System.out.println("Sender: " + event.getSender());
        System.out.println("Opponent: " + event.getOpponent());
        System.out.println("Status: " + event.getStatus());
        System.out.println("Turn: " + event.getTurn());
        System.out.println("Move: " + event.getMove());
    }

    public static void testGettersAndSetters() {
        Event event = new Event();
        event.setEventID(2);
        event.setSender("Sender2");
        event.setOpponent("Opponent2");
        event.setStatus(EventStatus.COMPLETED);
        event.setTurn("Turn2");
        event.setMove(2);

        System.out.println("Testing Getters and Setters:");
        System.out.println("Event ID: " + event.getEventID());
        System.out.println("Sender: " + event.getSender());
        System.out.println("Opponent: " + event.getOpponent());
        System.out.println("Status: " + event.getStatus());
        System.out.println("Turn: " + event.getTurn());
        System.out.println("Move: " + event.getMove());
    }

    public static void testEqualsMethod() {
        Event event1 = new Event(1, "Sender1", "Opponent1", EventStatus.PENDING, "Turn1", 42);
        Event event2 = new Event(1, "Sender2", "Opponent2", EventStatus.COMPLETED, "Turn2", 7);
        Event event3 = new Event(3, "Sender3", "Opponent3", EventStatus.ABORTED, "Turn3", 15);

        System.out.println("Testing Equals Method:");
        System.out.println("Event1 equals Event2: " + event1.equals(event2));
        System.out.println("Event1 equals Event3: " + event1.equals(event3));
    }
}