package Logic;

public class Pair<T1, T2> {

    // Variables
    private T1 first;
    private T2 second;

    // Constructors
    public Pair(Pair<T1, T2> pair) {
        first = pair.first;
        second = pair.second;
    }
    public Pair(T1 firstValue, T2 secondValue) {
        first = firstValue;
        second = secondValue;
    }
    public Pair() {}

    // Methods
    public void setFirst(T1 value) {
        first = value;
    }
    public void setSecond(T2 value) {
        second = value;
    }
    public void setLast(T2 value) {
        setSecond(value);
    }
    public T1 getFirst() {
        return first;
    }
    public T2 getSecond() {
        return second;
    }
    public T2 getLast() {
        return getSecond();
    }
}
