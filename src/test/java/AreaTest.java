import Validators.RValidator;
import Validators.YValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.*;

public class AreaTest {
    private Entry entry;
    private final String in = "INCLUDED";
    private final String out = "NOT_INCLUDED";

    @Before
    public void start() {
        entry = new Entry();
    }

    @After
    public void end() {
        entry = null;
    }

    private void setEntry(int x, double y, double r) {
        entry.setX(x);
        entry.setY(y);
        entry.setR(r);
    }

    @Test
    public void trash(){
        ArrayList<ArrayList<Integer>> list = new ArrayList<>(5);
        for (int i = 0; i < 5; i++){
            list.add(new ArrayList<>());
        }
    }

    private void setEntryWithVerdict(int x, double y, double r) {
        setEntry(x, y, r);
        entry.setVerdict(entry.processVerdict(x, y, r));
    }

    private void setEntryWithVerdict(int x, double y) {
        entry.setX(x);
        entry.setY(y);
        entry.setVerdict(entry.processVerdict(x, y, entry.getR()));
    }

    private String formatEntry(String s) {
        return "x=" + entry.getX() + ", y=" + entry.getY() + ", r=" + entry.getR() + "verdict=" + s;
    }

    @Test
    public void checkValidityOfEntries() {
        helpValidateNegative(new RValidator(), "1");
        helpValidateNegative(new RValidator(), 1);
        helpValidateNegative(new RValidator(), -1d);
        helpValidateNegative(new RValidator(), 5d);
        helpValidateNegative(new RValidator(), 0d);
        helpValidateNegative(new RValidator(), null);
        helpValidatePositive(new RValidator(), 2.5d);
        helpValidatePositive(new RValidator(), 4d);

        helpValidateNegative(new YValidator(),"1");
        helpValidateNegative(new YValidator(),null);
        helpValidateNegative(new YValidator(), 7d);
        helpValidateNegative(new YValidator(),-6d);
        helpValidatePositive(new YValidator(), 3.4d);
        helpValidatePositive(new YValidator(), -2.8d);
    }

    private void helpValidateNegative(Validator v, Object o){
        Throwable thrown = catchThrowable(() -> {
            v.validate(null, null, o);
        });
        assertThat(thrown).isInstanceOfAny(ValidatorException.class, NullPointerException.class);
    }

    private void helpValidatePositive(Validator v, Object o){
        Throwable thrown = catchThrowable(() -> {
            v.validate(null, null, o);
        });
        assertThat(thrown).doesNotThrowAnyException();
    }


    @Test
    public void checkBottomRightIn() {
        double r = 4;
        entry.setR(r);

        for (int x = 0; x <= 4; x++) {
            for (int y = 0; x*x + y*y <= r*r; y-= 1) {
                setEntryWithVerdict(x,y);
                assertEquals(entry.toString(), formatEntry(in));
            }
        }
    }

    @Test
    public void checkBottomRightOut() {
        double r = 3;
        entry.setR(r);

        for (int x = 1; x <= r; x++) {
            for (int y = -1; (x*x + y*y > r*r) && (-y <= r); y-= 1) {
                setEntryWithVerdict(x,y);
                assertEquals(entry.toString(), formatEntry(out));
            }
        }
    }


    @Test
    public void checkBottomLeftIn() {
        double r = 4;
        entry.setR(r);
        for (int x = -4; x <= 0; x++) {
            for (double y = 0; y >= -0.5 * x - r / 2; y -= 0.01) {
                setEntryWithVerdict(x, y);
                assertEquals(entry.toString(), formatEntry(in));
            }
        }
    }

    @Test
    public void checkBottomLeftOut() {
        double r = 3;
        entry.setR(r);
        for (int x = -((int) r); x < 0; x++) {
            for (double y = 0; (y < -0.5 * x - r / 2) && (r >= -y); y -= 0.01) {
                setEntryWithVerdict(x, y);
                assertEquals(entry.toString(), formatEntry(in));
            }
        }
    }

    @Test
    public void checkTopLeft() {
        entry.setR(4);
        for (int x = -4; x < 0; x++) {
            for (double y = 0.01; y <= 5; y += 0.01) {
                setEntryWithVerdict(x, y);
                assertEquals(entry.toString(), formatEntry(out));
            }
        }
    }

    @Test
    public void borderTopLeftIn() {
        entry.setR(4);
        entry.setX(0);
        for (double y = 0; y <= 4; y += 0.01) {
            entry.setY(y);
            entry.setVerdict(entry.processVerdict());
            assertEquals(entry.toString(), formatEntry(in));
        }

        entry.setY(0);
        for (int x = -4; x <= 0; x++) {
            entry.setX(x);
            entry.setVerdict(entry.processVerdict());
            assertEquals(entry.toString(), formatEntry(in));
        }
    }

    @Test
    public void checkTopRightIn() {
        entry.setR(4);
        for (int x = 0; x <= 4; x++) {
            for (double y = 0; y <= 4; y += 0.01) {
                setEntryWithVerdict(x, y);
                assertEquals(entry.toString(), formatEntry(in));
            }
        }
    }

    @Test
    public void checkTopRightOut() {
        double r = 3;
        entry.setR(r);
        for (int x = (int) r + 1; x <= 5; x++) {
            for (double y = 0; y <= 5; y += 0.01) {
                setEntryWithVerdict(x, y);
                assertEquals(entry.toString(), formatEntry(out));
            }
        }

        for (int x = 0; x <= 5; x++) {
            for (double y = r + 0.01; y <= 5; y += 0.01) {
                setEntryWithVerdict(x, y);
                assertEquals(entry.toString(), formatEntry(out));
            }
        }
    }
}
