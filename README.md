# Лаборатори №9: Код хамралт — JaCoCo-гоор тестээ хөтлөх

- **Нэр:** О.Оюунжаргал
- **Оюутны код:** B232270019
- **Хичээл:** F.CSA313 — Программ хангамжийн чанарын баталгаа ба тест
- 
## Орчин

```
$ java -version
openjdk version "21.0.11" 2026-04-21
OpenJDK Runtime Environment Homebrew (build 21.0.11)
OpenJDK 64-Bit Server VM Homebrew (build 21.0.11, mixed mode, sharing)

$ mvn -version
Apache Maven 3.9.16 (2bdd9fddda4b155ebf8000e807eb73fd829a51d5)
Maven home: /opt/homebrew/Cellar/maven/3.9.16/libexec
Java version: 21.0.11, vendor: Homebrew, runtime: /usr/local/Cellar/openjdk@21/21.0.11/libexec/openjdk.jdk/Contents/Home
Default locale: en_MN, platform encoding: UTF-8
OS name: "mac os x", version: "26.6", arch: "x86_64", family: "mac"
```
(Бүрэн гаралт: `results/java-version.txt`, `results/mvn-version.txt`)

## Хэсэг A — Happy path тестүүд ба эхний хамралт

5 happy-path тест (`letterGrade`-ийн A/C утга, `gpa` хоёр дүнтэй, `scholarship`-ийн 100/50 тохиолдол)
бичээд `mvn verify` ажиллуулахад (`results/mvn-verify-A.txt`, `results/jacoco-A.csv`):

| Хэмжүүр | Хамрагдсан | Нийт | % |
|---|---|---|---|
| Line | 17 | 25 | **68.0%** |
| Branch | 22 | 41 | **53.7%** |

Тайлан дээр ажиглагдсан зүйлс:
- `letterGrade`-ийн `throw IllegalArgumentException` мөр (score<0/>100 хамгаалалт) бүхэлдээ **улаан** —
  тестүүд зөвхөн хүчинтэй оноо дамжуулсан тул guard-ийн "true" салаа огт ажиллаагүй.
- `score > 60` мөр мөн **улаан**: тестүүдэд 70-с доош оноо огт байхгүй тул D/F ялгах мөрт хүрээгүй.
- `scholarship`-ийн `gpa>=3.4 && credits>=12` нөхцөл **шар ромботой** (хагас хамрагдсан) — зөвхөн
  `gpa>=3.8` салааг шалгасан тул доод давхаргын нөхцөлүүд хэсэгчлэн л ажилласан.

## Хэсэг B — CFG ба Branch 100%

`scholarship()`-ийн CFG-г гараар зурж, зангилаа/ирмэг бүрийг Лекц 9-ийн хэв маягаар тэмдэглэв
(`cfg.png`). Ирмэг бүрийн хажууд түүнийг дайрч буй тестийн нэрийг бичсэн.

13 нэмэлт тест бичсэнээр (нийт 18 тест, `results/mvn-verify-B.txt`: `Tests run: 18, Failures: 0`)
Line болон Branch хамралт хоёулаа **100%** болов (`results/jacoco-B.csv`: `BRANCH_MISSED=0,
BRANCH_COVERED=41`, `LINE_MISSED=0, LINE_COVERED=25`).

## Хэсэг C — Давталтын заагийн тестүүд

`gpa()`-ийн массив дээрх давталтад 0/1/2+ удаагийн ажиллагааны дүрмийг тусгайлан шалгасан 4 тест
нэмсэн (`davtalt_0_udaa_*`, `davtalt_1_udaa_neg_duntei`, `davtalt_2_ba_tuunees_olon_udaa`).

Эдгээр нь Branch/Line хамралтын тоог өсгөөгүй (Хэсэг B-д аль хэдийн 100% болсон байсан) — учир нь
coverage-driven сонголт зөвхөн "аль мөр/салаа ажиллаагүй вэ?" гэдгийг хардаг бол interval-driven
(0/1/олон) сонголт нь "давталт буруу тооноор ажиллавал юу болох вэ?" гэдгийг нэг нэгээр нь баталгаажуулдаг
өөр техник. Жишээ нь массивын индексийн алдаа эсвэл накопляторын анхны утгын алдаа зэрэг нь branch
хамралтаар огт илрэхгүй ч давталтын заагийн тестээр баригдана.

## Хэсэг D — Хамралт 100%, гэхдээ алдаа амьд

Спецификацийг дахин уншихад 60-69.99 оноо "D" байх ёстой гэдэг нь тодорхой байсан. Зааг утгын 7 тест
(`letterGrade(0,59.99,60,69.99,89.99,90,100)`) нэмэхэд (код хараахан засагдаагүй үед):

```
[ERROR] mn.edu.must.sqat.ScholarshipCalculatorTest.letterGrade_zaag_60_D -- Time elapsed: 0.012 s <<< FAILURE!
org.opentest4j.AssertionFailedError: expected: <D> but was: <F>
...
[ERROR]   ScholarshipCalculatorTest.letterGrade_zaag_60_D:170 expected: <D> but was: <F>
[ERROR] Tests run: 29, Failures: 1, Errors: 0, Skipped: 0
```
(`results/mvn-test-D-fail.txt`)

**Алдааны шалтгаан:** `letterGrade`-д `if (score > 60) return "D";` гэж бичигдсэн байсан бол спецификаци
`60` оноог "D" гэж тооцдог. Тусдаа commit-оор засав: `score > 60` → `score >= 60`
("Fix letterGrade: > 60 → >= 60, spec says 60 is D"). Засварын дараа бүх 29 тест ногоон, Line/Branch
100% хэвээр (`results/mvn-verify-final.txt`, `results/jacoco-final.csv`).

### Дүгнэлт

Branch хамралт 100% байхад энэ алдаа яагаад "үл үзэгдэгч" байсан бэ? Учир нь JaCoCo branch хамралт
зөвхөн аль салаа ажилласан эсэхийг хэмждэг, аль утгаар ажилласныг хэмждэггүй. `score > 60`
болон `score >= 60` хоёул яг ИЖИЛ хоёр салаа (true/false) үүсгэдэг тул Хэсэг B-ийн тестүүд (65→D,
50→F гэх мэт "дундаж" утгууд ашигласан) аль хоёр салааг ч бүрэн хамарсан хэдий ч нарийн ЗААГ дээрх
(яг 60) зөрүүг огт илрүүлээгүй. Зөвхөн спецификацийн зааг утгыг тусгайлан сорьсон (equivalence
partitioning + boundary value analysis) тест л энэ ялгааг олсон. Энэ нь хамралт бол шаардлагатай
хэрэгсэл ч хангалттай нотолгоо биш гэдгийг тодорхой харуулж байна — 100% хамралт нь зөвхөн
"код ажилласан" гэдгийг нотолдог, "код зөв" гэдгийг нотолдоггүй.
