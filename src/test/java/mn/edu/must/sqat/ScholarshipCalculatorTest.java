package mn.edu.must.sqat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Хэсэг A — зөвхөн хэвийн урсгалын (happy path) тестүүд.
 * Зорилго: эхний хамралтын хэмжилтийг авах (Line ~60%, Branch ~50% орчим хүлээгдэж байна).
 */
class ScholarshipCalculatorTest {

    private final ScholarshipCalculator calc = new ScholarshipCalculator();

    @Test
    void letterGrade_90ba_deesh_A_butsna() {
        assertEquals("A", calc.letterGrade(95));
    }

    @Test
    void letterGrade_70_79_hooronds_C_butsna() {
        assertEquals("C", calc.letterGrade(75));
    }

    @Test
    void gpa_hoyor_dun_golchloh() {
        assertEquals(3.5, calc.gpa(new double[]{95, 85}), 0.0001);
    }

    @Test
    void scholarship_olon_tetgelegt_100_butsna() {
        assertEquals(100, calc.scholarship(3.9, 16, false));
    }

    @Test
    void scholarship_golch_hangasan_bol_50_butsna() {
        assertEquals(50, calc.scholarship(3.1, 10, false));
    }
}
