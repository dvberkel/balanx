package nl.dvberkel.game;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static nl.dvberkel.game.Sorted.sorted;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Parameterized.class)
public class ValueComparisonTest {
    @Parameterized.Parameters(name = "{index}: {0} is less than {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{Heuristic.Value.of(1), Heuristic.Value.of(2)},
                new Object[]{Heuristic.Value.of(2), Heuristic.Value.of(3)},
                new Object[]{Heuristic.Value.of(-1), Heuristic.Value.of(1)},
                new Object[]{Heuristic.Value.of(-3), Heuristic.Value.of(-2)}
        );
    }

    private final List<Heuristic.Value> values;
    public ValueComparisonTest(Heuristic.Value left, Heuristic.Value right) {
        this.values = Arrays.asList(left, right);
    }

    @Test
    public void compareValues() {
        assertThat(values, is(sorted()));
    }
}

class Sorted extends TypeSafeMatcher<List<Heuristic.Value>> {
    public static Sorted sorted() {
        return new Sorted();
    }

    private Sorted(){
        super();
    }

    @Override
    protected boolean matchesSafely(List<Heuristic.Value> values) {
        if (values.size() > 1) {
            for (int index = 0; index < (values.size() - 1); index++) {
                if (values.get(index).compareTo(values.get(index + 1)) > 0) {
                    return false;
                }
            }

        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("sorted");
    }
}
