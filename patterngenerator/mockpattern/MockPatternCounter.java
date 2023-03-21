package patterngenerator.mockpattern;

import java.util.*;

/**
 * Support class to count MockPattern and sort them by number of occurrences
 */
public class MockPatternCounter {
    /**Container of MockPatterns and their counters*/
    private final Map<MockPattern, Integer> counter;

    /** Initialize the MockCounter
     */
    public MockPatternCounter() {
        counter = new HashMap<>();
    }

    /**
     * Returns the list of MockPatterns counted till this invocation.
     * @return List of the current counter MockPatterns.
     */
    public List<MockPattern> getEntriesKey() {
        List<MockPattern> list = new ArrayList<>();
        counter.forEach((k, v) -> list.add(k));
        return list;
    }

    /**
     * Adds the new MockPattern into the counter or increases the counter in case it was already added
     * or if the new MockPattern has the same structure but different quantifiers
     * @param mockPattern
     */
    public void put(MockPattern mockPattern) {
        if (counter.containsKey(mockPattern)){
            int oldValue = counter.remove(mockPattern);
            counter.put(mockPattern, oldValue + 1);
            return;
        }

        List<Map.Entry<MockPattern, Integer>> entries = new ArrayList<>(counter.entrySet());
        for(Map.Entry<MockPattern, Integer> entry : entries) {
            if (entry.getKey().equalsPattern(mockPattern)){
                int oldValue = counter.remove(entry.getKey());
                counter.put(mockPattern, oldValue + 1);
                return;
            }
        }

        counter.put(mockPattern, 1);
    }

    /**
     * Replaces the key for the entry with key oldMock with newMock or creates a new entry with newMock
     * @param oldMock
     * @param newMock
     */
    public void replace(MockPattern oldMock, MockPattern newMock) {
        if (counter.containsKey(oldMock)) {
            int value = counter.remove(oldMock);
            counter.put(newMock, value);
        } else counter.put(newMock, 1);
    }

    /**
     * Sorts the MockPatterns by counter and returns them
     * @return Ordered list of MockPatterns
     */
    public List<MockPattern> sort() {
        List<Map.Entry<MockPattern, Integer>> entries = new ArrayList<>(counter.entrySet());
        entries.sort(Map.Entry.comparingByValue());
        List<MockPattern> mockPatterns = new ArrayList<>();
        for(Map.Entry<MockPattern, Integer> e : entries)
            mockPatterns.add(e.getKey());
        Collections.reverse(mockPatterns);
        return mockPatterns;
    }
}
