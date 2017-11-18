package com.example.pranaab.playtime_android_app.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Event> EVENTS = new ArrayList<Event>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Event> EVENT_MAP = new HashMap<String, Event>();

    private static final int COUNT = 15;

    static {
        // Add some sample items.
        addItem(createDummyEvent(1, "Running", "Waterloo Park", "17:00", 10, 4));
        addItem(createDummyEvent(2, "Badminton Game", "CIF", "17:00", 8, 6));
        addItem(createDummyEvent(3, "Basketball Indoors", "PAC", "13:30", 15, 11));
        addItem(createDummyEvent(4, "Biking", "Waterloo Laurel Trail", "08:00", 15, 6));
        addItem(createDummyEvent(5, "Hackathon-Fundraiser", "E5-Second Floor", "19:00", 60, 6));
        addItem(createDummyEvent(6, "Gym Session", "CIF", "10:00", 8, 4));
        addItem(createDummyEvent(7, "Debating Session", "MC-2067", "17:00", 20, 12));
        addItem(createDummyEvent(8, "Board Games Session", "Bubble Tea Place", "17:00", 12, 8));
        addItem(createDummyEvent(9, "Zelda: Online Session", "SLC Ground Floor", "18:00", 16, 9));
        addItem(createDummyEvent(10, "Maxwell Concert", "Waterloo Park Hall", "17:00", 8, 6));
        addItem(createDummyEvent(11, "Running", "Laurel Trail", "20:00", 10, 4));
        addItem(createDummyEvent(12, "Biology Intern Event", "B1-2089", "12:00", 40, 34));

    }

    private static void addItem(Event item) {
        EVENTS.add(item);
        EVENT_MAP.put(item.getUId().toString(), item);
    }

    private static Event createDummyEvent(Integer position, String name, String location, String time, int max, int joined ) {
        Event event = new Event(position.toString());
        event.setName(name);
        event.setLocation(location);
        event.setMax_subscribers(max);
        event.setNum_joined_members(joined);
        event.setStart_time(time);
        event.setDetails();
        return event;

    }

}
