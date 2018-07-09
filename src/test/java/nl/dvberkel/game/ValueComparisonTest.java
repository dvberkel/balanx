package nl.dvberkel.game;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static nl.dvberkel.game.Sorted.sorted;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;;
;
public class ValueComparisonTest {
    @Test
    public void compareValues() {
        List<Heuristic.Value> list = Arrays.asList(Heuristic.Value.of(1), Heuristic.Value.of(2));
        assertThat(list, is(sorted()));
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
