package patterngenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/** Java bean class that Mocks a Pattern Regex in a very basic form.
 *  Each MockPattern is formed by MockRegexes that Mock a single Regular Expression in a basic form.
 *
 */
class MockPattern {
    /**List of MockRegex that form the MockPattern*/
    private final List<MockRegex> regexes;

    /**
     * Default consturctor
     */
    protected MockPattern() {
        regexes = new ArrayList<>();
    }

    /**
     * Constructor that creates each MockRegex to represent a given String
     *
     * @param item  String to represent in a MockPattern
     */
    protected MockPattern(String item) {
        regexes = new ArrayList<>();

        //Seleziona il placeholder a cui appartiene il carattere della stringa e lo mette nel pattern
        //Conta anche il massimo di occorrenze per una data classe
        //esempio albero -> a{1,6} ovvero un insieme di caratteri da minimo 1 e massimo 6 caratteri
        for (int i = 0; i < item.length(); ++i) {
            char selectedPH = getPlaceholder(item.charAt(i));
            if (selectedPH != 0) {
                int howMany = 1;
                for(int j = i + 1; j < item.length(); ++j){
                    char s = getPlaceholder(item.charAt(j));
                    if (selectedPH == s) ++howMany;
                    else {
                        i = j - 1;
                        break;
                    }
                    if (j == item.length() - 1) i = j;
                }
                addRegex(selectedPH, howMany, howMany);
            }
        }
    }

    /**
     * Returns a PlaceHolder for a MockRegex for the given char
     *  A represents upper case letters;
     *  a represents lower case letters;
     *  0 represents numbers;
     *  Every other character is represented by itself.
     * @param c character
     * @return  character PlaceHolder for a MockRegex
     */
    private static char getPlaceholder(char c) {
        char placeholderUpperCase = 'A';
        char placeholderLowerCase = 'a';
        char placeholderNumber = '0';

        char selectedPlaceholder;
        if (c >= 48 && c <= 57)
            selectedPlaceholder = placeholderNumber;
        else if (c >= 65 && c <= 90)
            selectedPlaceholder = placeholderUpperCase;
        else if (c >= 97 && c <= 122)
            selectedPlaceholder = placeholderLowerCase;
        else {
            selectedPlaceholder = c;
        }
        return selectedPlaceholder;
    }

    /**
     * Adds to the MockRegex list a new MockRegex
     * @param regex Placeholder character for the MockRegex
     * @param min   Minimum quantifier for the MockRegex
     * @param max   Maximum quantifier for the MockRegex
     */
    public void addRegex(char regex, int min, int max) {
        regexes.add(new MockRegex(regex, min, max));
    }

    /**
     * Returns the number of MockRegex that forms this MockPattern.
     * @return  the number of MockRegex that forms this MockPattern.
     */
    public int getSize(){
        return regexes.size();
    }

    /**
     * Returns the n Mockregex in the MockPattern.
     * @param n the position of the MockRegex in the MockPattern.
     * @return the n MockRegex in the MockPattern.
     */
    public MockRegex getRegex(int n) {
        return regexes.get(n);
    }


    /**
     * Returns true if this and other MockPattern represents the same MockPattern.
     * They can be composed by the same MockRegexes, but they could have different quantifiers
     * @param other MockPattern to confront
     * @return True if this and other MockPattern represents the same MockPattern. False otherwise.
     */
    public boolean equalsPattern(MockPattern other) {
        if (other.getSize() != getSize()) return false;
        for(int i = 0; i < getSize(); ++i) {
            if (!(regexes.get(i).equalRegex(other.getRegex(i))))
                return false;
        }
        return true;
    }

    /**
     * Equals Method
     * @param o other MockPattern
     * @return True if each MockRegex that composes this MockPattern is equal
     * to each MockRegex that composes the other MockPattern
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MockPattern other = (MockPattern) o;
        if (other.getSize() != getSize()) return false;

        for(int i = 0; i < getSize(); ++i) {
            if (!(other.getRegex(i).equals(regexes.get(i)))) return false;
        }
        return true;
    }

    /**
     * Returns the Pattern Regex represented by this MockPattern.
     * @return the Pattern Regex represented by this MockPattern.
     */
    public Pattern toPattern() {
        StringBuilder regex = new StringBuilder();
        for(MockRegex basicRegex : regexes)
            regex.append(basicRegex.toPattern());

        return Pattern.compile(regex.toString());

    }

    /**
     * Metodo toString
     * @return Ritorna l'instanza in forma di stringa.
     */
    @Override
    public String toString() {
        StringBuilder pattern = new StringBuilder();
        for(MockRegex regex : regexes) {
            String r = regex.toString();
            pattern.append(r);
        }
        return pattern.toString();
    }
}
