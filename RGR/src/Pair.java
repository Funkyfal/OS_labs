import java.util.Objects;

public class Pair<T extends Comparable<T>>
{
    T first;
    int second;
    Pair(T first, int second){
        this.first = first;
        this.second = second;
    }
    @Override
    public String toString()
    {
        return first + " " + second;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pair<?> pair = (Pair<?>) obj;
        if (second != pair.second) return false;
        return Objects.equals(first, pair.first);
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + second;
        return result;
    }
    public boolean hasCommon(Pair<Integer> obj)
    {
        Pair<Integer> temp = (Pair<Integer>) this;
        return temp.first == obj.first || temp.first == obj.second || temp.second == obj.first || temp.second == obj.second;
    }
}
