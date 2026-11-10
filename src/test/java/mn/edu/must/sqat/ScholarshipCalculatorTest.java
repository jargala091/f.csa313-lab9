package mn.edu.must.sqat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Хэсэг A — зөвхөн хэвийн урсгалын (happy path) тестүүд.
 * Зорилго: эхний хамралтын хэмжилтийг авах (Line ~60%, Branch ~50% орчим хүлээгдэж байна).
 */
class ScholarshipCalculatorTest {

    private final ScholarshipCalculator calc = new ScholarshipCalculator();

    // ---------- Хэсэг A: happy path ----------

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
        // S7 (cfg.png): N3a=T, N3b=T
        assertEquals(100, calc.scholarship(3.9, 16, false));
    }

    @Test
    void scholarship_golch_hangasan_bol_50_butsna() {
        assertEquals(50, calc.scholarship(3.1, 10, false));
    }

    // ---------- Хэсэг B: branch 100%-д хүргэх нэмэлт тестүүд ----------
    // cfg.png дээрх S1..S11 тэмдэглэгээтэй яг таарч байгаа тестийн нэрс.

    @Test
    void letterGrade_sul_hyazgaaraas_gadna_exception() {
        // N: score<0 -> T
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(-1));
    }

    @Test
    void letterGrade_deed_hyazgaaraas_gadna_exception() {
        // N: score>100 -> T
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(101));
    }

    @Test
    void gpa_null_bol_exception() {
        // (scores==null || ...) -> OR-ийн зүүн тал T (богино залгалт)
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(null));
    }

    @Test
    void gpa_hooson_massiv_bol_exception() {
        // scores==null -> F, scores.length==0 -> T
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(new double[0]));
    }

    @Test
    void gpa_C_D_F_salaanuudiig_hamarna() {
        // switch(letterGrade(s))-ийн C, D, default(F) case-уудыг нэг дор хамруулна.
        // 75->C(2), 65->D(1, score>60 => T салаа), 50->F/default(0, score>60 => F салаа)
        assertEquals(1.0, calc.gpa(new double[]{75, 65, 50}), 0.0001);
    }

    @Test
    void scholarship_gpa_sul_hyazgaaraas_gadna_exception() {
        // S1 (cfg.png): N1 эхний нөхцөл (gpa<0) -> T
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(-1, 5, false));
    }

    @Test
    void scholarship_gpa_deed_hyazgaaraas_gadna_exception() {
        // S2 (cfg.png): N1 хоёрдугаар нөхцөл (gpa>4) -> T
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(4.1, 5, false));
    }

    @Test
    void scholarship_credits_sul_hyazgaaraas_gadna_exception() {
        // S3 (cfg.png): N1 гуравдугаар нөхцөл (credits<0) -> T
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(2.0, -1, false));
    }

    @Test
    void scholarship_zorchiltoi_bol_tetgeleggui() {
        // S5 (cfg.png): N2 (hasViolation) -> T
        assertEquals(0, calc.scholarship(3.9, 16, true));
    }

    @Test
    void scholarship_gpa38_credits_dutuu_50_butsna() {
        // S8 (cfg.png): N3a=T, N3b=F, N4a=T, N4b=F, N5=T
        assertEquals(50, calc.scholarship(3.9, 10, false));
    }

    @Test
    void scholarship_gpa34_credits12_70_butsna() {
        // S9 (cfg.png): N3a=F, N4a=T, N4b=T
        assertEquals(70, calc.scholarship(3.5, 20, false));
    }

    @Test
    void scholarship_gpa34_dooshoo_50_butsna() {
        // S10 (cfg.png): N3a=F, N4a=F, N5=T
        assertEquals(50, calc.scholarship(3.2, 20, false));
    }

    @Test
    void scholarship_gpa30_dooshoo_0_butsna() {
        // S11 (cfg.png): N3a=F, N4a=F, N5=F
        assertEquals(0, calc.scholarship(2.5, 5, false));
    }

    // ---------- Хэсэг C: gpa()-ийн давталтын 0 / 1 / 2+ заагийн тестүүд ----------
    // Лекц 9-ийн давталтын интервалын дүрэм. Эдгээр нь Branch/Line хамралтыг нэмэгдүүлэхгүй
    // (gpa_null_bol_exception, gpa_hooson_massiv_bol_exception, gpa_C_D_F_salaanuudiig_hamarna,
    // gpa_hoyor_dun_golchloh зэрэг Хэсэг A/B-ийн тестүүд аль хэдийн 100% хамралт хангасан) —
    // гэхдээ давталтын СУДАЛГААНЫ дүрмийн дагуу тусгайлан баталгаажуулж байна: coverage-driven
    // сонголт (аль салаа хамрагдаагүй вэ) ба interval-driven сонголт (0/1/олон удаа гүйлгэх үед
    // юу болдог вэ) нь ӨӨР ӨӨР алдааны ангиллыг олдог тул хоёуланг нь дангаар нь хэрэглэх нь зөв.

    @Test
    void davtalt_0_udaa_hooson_massiv_exception() {
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(new double[0]));
    }

    @Test
    void davtalt_0_udaa_null_exception() {
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(null));
    }

    @Test
    void davtalt_1_udaa_neg_duntei() {
        assertEquals(4.0, calc.gpa(new double[]{95}), 0.0001);
    }

    @Test
    void davtalt_2_ba_tuunees_olon_udaa() {
        assertEquals(2.5, calc.gpa(new double[]{95, 85, 75, 65}), 0.0001);
    }
}
