package patterngenerator.mockpattern;

/**
 * Java bean that Mocks a regular expression (RE) composed by a single character in a basic form.
 * Each RE is represented by a Placeholder character:
 *  -A represents upper case letters;
 *  -a represents lower case letters;
 *  -0 represents numbers;
 *  -Every other character is represented by itself.
 */
public class MockRegex {

    /** character representing the RE */
    private final char  regex;  //carattere identificativo per la regular expression

    /** Minimum number of characters for this RE */
    private final int min;      //Minimo numero di caratteri per la regular expression

   /** Maximum number of characters for this RE */
    private final int max;      //Massimo numero di caratteri per la regular expression

    /**
     * Constructor
     * @param regex Character representing the RE
     * @param min   Minimum number of characters for this RE
     * @param max   Maximum number of characters for this RE
     */
    public MockRegex(char regex, int min, int max) {
        this.regex = regex;
        this.min = min;
        this.max = max;
    }

    /**
     * Getter method for class variable regex
     * @return regex character represented by this MockRegex
     */
    public char getRegex(){
        return regex;
    }

    /**
     * Getter method for class variable min
     * @return Minumum number of characters for this RE
     */
    public int getMin() {
        return min;
    }
    /**
     * Getter method for class variable max
     * @return Maximum number of characters for this RE
     */
    public int getMax() {
        return max;
    }

    /**
     * Returns the RE represented by this instance as a compilable Pattern Regex.
     * @return The RE represented by this instance as a compilable Pattern Regex.
     */
    public String toPattern() {
        String reg;
        switch (regex) {
            case '0' ->
                reg = "\\d";

            case 'a' ->
                reg = "[a-z]";

            case 'A' ->
                reg = "[A-Z]";

            case '{', '}', '[', ']', '(', ')', '\\', '/', '+', '?', '$', '*', '^', '|', '.' ->  //escapable chars
                reg = "\\" + regex;

            default ->
                reg = regex + "";

        }
        if (min == 1 && max == 1 ) return reg;
        return reg + "{" + min + "," + max + "}";
    }

    /**
     * Compares this RE with another and returns true if they are represented by the same character.
     * They can have different quantifiers.
     * @param other MockRegex to compare.
     * @return true if they are represented by the same character.
     *      * They can have different quantifiers.
     */
    public boolean equalRegex(MockRegex other){
        return regex == other.getRegex();
    }

    /**
     * Metodo equals
     * @param other BasicRegex da confrontare
     * @return True if they represent the exact same Regular Expression.
     */
    public boolean equals(MockRegex other){
        return regex == other.getRegex() && min == other.getMin() && max == other.getMax();
    }

    /**
     * ToString method
     * @return String representation for this instance of MockRegex in the following form:
     * "RegularExpressionCharacter{Minimum,Maximum}"
     */
    public String toString() {
        if (min == 1 && max == 1) return regex + "";
        return regex + "{" + min + "," + max + "}";

    }
}
