package patterngenerator;

import java.util.*;
import java.util.regex.Pattern;


public class PatternGenerator {
    private static final boolean debug = false;
    /**
     *
     * Returns a List of Pattern Regex which matches all the strings in a given list.
     * Every string has at least matching Pattern and every Pattern has at least a matching String.
     *
     * @param items String list.
     * @return List of Pattern regex which matches all strings.
     */
    public static List<Pattern> getPatterns(List<String> items)  {
        long start = System.currentTimeMillis(); //Timer for debug
        if(items == null || items.size() == 0)
            return new ArrayList<>();

        MockPatternCounter mockPatternCounter = new MockPatternCounter();
        MockPattern found = null;
        MockPattern mockPattern = null;
        try {
            TreeSet<String> strings = new TreeSet<>(items); //List that will contain every string, without copies
            int howMany = strings.size();
            while (strings.size() > 0) {
                String s = strings.last();
                strings.remove(s);
                mockPattern = new MockPattern(s);

                if (!mockPatternCounter.getEntriesKey().contains(mockPattern)) {
                    /*
                        If the exact same MockPattern was not already found, it will search for a similiar one and merge
                        their quantifiers
                     */
                    boolean equal = false;
                    //Finds the same MockPattern
                    for (MockPattern pat : mockPatternCounter.getEntriesKey()) {
                        if (mockPattern.equalsPattern(pat)) {
                            found = pat;
                            equal = true;
                            break;
                        }
                    }
                    if (equal) {
                        //Checks if the found MockPattern's MockRegexes have the same quantifiers
                        if (!mockPattern.equals(found)) {

                            //Create a empty MockPattern
                            MockPattern combined = new MockPattern();
                            //Confronts each MockRegex's quantifiers and merges their quantifiers
                            for (int i = 0; i < mockPattern.getSize(); ++i) {

                                MockRegex b1 = mockPattern.getRegex(i);
                                MockRegex b2 = found.getRegex(i);

                                int min = Math.min(b1.getMin(), b2.getMin());
                                int max = Math.max(b1.getMax(), b2.getMax());
                                combined.addRegex(b1.getRegex(), min, max);
                            }


                            //replaces the found MockPattern if the new one has better quantifiers
                            if (!found.equals(combined))
                                mockPatternCounter.replace(found, combined);
                            //Adds the new one
                            mockPatternCounter.put(combined);
                        }
                    } else
                        //If its not equal then its a new pattern
                        mockPatternCounter.put(mockPattern);

                } else
                    //Doesnt add mockPattern to the list but it just increments its counter
                    mockPatternCounter.put(mockPattern);

            }

            long t = System.currentTimeMillis() - start;
            if (debug)
                System.out.println("\nI have found a total of " + mockPatternCounter.getEntriesKey().size() + " in a total of " + howMany + " different strings on a total of " + items.size() + " after " + t/1000 + " sec." + "\n");

            //Gets the MockPatterns sorted by String matched
            List<MockPattern> mockPatterns = mockPatternCounter.sort();
            List<Pattern> patterns = new ArrayList<>();

            //Transforms MockPatterns in Pattern Regex objects
            for (MockPattern pattern : mockPatterns) {
                Pattern p = pattern.toPattern();
                patterns.add(p);
            }
            if (debug) {
                long time = System.currentTimeMillis();
                checkPatterns(items, patterns);
                time = System.currentTimeMillis() - time;
                System.out.println("Time to check pattern correctness: " + (double) (System.currentTimeMillis() - time) / 1000 + " sec.");
                System.out.println("Execution time: " + (double) (System.currentTimeMillis() - start) / 1000 + " sec.");
            }
            return patterns;
        } catch(Exception e){
            e.printStackTrace();
            System.out.println(mockPattern + " e " + found);
            System.exit(0);
            return null;
        }
    }

    /**
     * Debug method that checks every strings used to create the list of Pattern regex to find if the final list doesn't
     * contain a matching pattern.
     *
     * @param items     String list
     * @param patterns  Generated Pattern regex list
     */
    private static void checkPatterns(List<String> items, List<Pattern> patterns) {
        System.out.println("Checking which pattern matches with which string");
        TreeSet<String> itemsSet = new TreeSet<>(items);
        List<Pattern> patternWithoutMatch = new ArrayList<>(patterns);
        List<String> stringWithoutMatch = new ArrayList<>();
        while(itemsSet.size() > 0) {
            boolean matched = false;
            String current = itemsSet.last();
            for(Pattern p : patterns) {
                if (p.matcher(current).matches()){
                    matched = true;
                    patternWithoutMatch.remove(p);
                    break;
                }
            }
            itemsSet.remove(current);
            if (!matched) {
                stringWithoutMatch.add(current);
            }
        }
        System.out.println("Check if there is any string without a matching pattern:\n");

        if (stringWithoutMatch.size() != 0){
            System.out.println("these string have no matching pattern: ");
            for(String s : stringWithoutMatch) {
                System.out.println("\t" + s);
            }
        } else {
            System.out.println("Every string has a matching pattern");
        }

        if (patternWithoutMatch.size() != 0) {
            System.out.println("these patterns have no matching string:");

            for(Pattern s : patternWithoutMatch) {
                System.out.println("\t" + s.pattern());
            }
        }else {
            System.out.println("Every pattern has at least matching string");
        }

    }
}
