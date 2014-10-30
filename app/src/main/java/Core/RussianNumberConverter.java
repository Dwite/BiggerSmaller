package Core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static Core.Util.toUpperCaseFirstLetter;

/**
 * Created by dwite_000 on 26.10.2014.
 */
public class RussianNumberConverter extends AbstractNumeral {

    private static final String EDINICHI[] = {"ноль", "один", "два", "три", "четыре",
            "пять", "шесть", "семь", "восемь", "девять"};
    private static final String DESYAT[] = {"десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать",
            "пятнадцать", "шестнадцать", "семнадцать", "восемьнадцать", "девятнадцать"};
    private static final String DESYATKI[] = {"", "десять", "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят",
            "семьдесят", "восемьдесят", "девяносто"};
    private static final String SOTNI[] = {"", "сто", "двести", "триста", "четыреста", "пятьсот", "шестьсот",
            "семьсот", "восемьсот", "девятьсот"};
    private static final String LIONS[] = {"", "тысяча", "миллион",
            "миллиард", "триллион", "квадриллион", "квинтиллион", "секстиллион", "септиллион",
            "октиллион", "нониллион", "дециллион"
    };

    @Override
    public String convert(Number number) {
        // check number type
        checkSupported(number);
        return formatImpl(number.toString());
    }

    private String formatImpl(String text) {
        if ("0".equals(text)) {
            return EDINICHI[0];
        }
        StringBuilder sb = new StringBuilder();
        if (text.startsWith("-")) {
            sb.append("минус ");
            text = text.substring(1);
        }

        byte n[][] = Util.groups(text, 3);


        for (int i = 0; i < n.length; ++i) {
            // 1 = 1000, 2 = 1 000 000
            int k = n.length - i - 1;

            int h = n[i][0]; // сотни
            int t = n[i][1]; // десятки
            int u = n[i][2]; // единицы
            if (h == 0 && t == 0 && u == 0) {
                // этих вобще
                continue;
            }
            // есть сотенные...
            if (h > 0) {
                String sotni = SOTNI[h];
                sb.append(sotni);
                sb.append(" ");
            }
            // десяток нет
            if (t == 0) {
                //
                if (u > 0) {
                    String txt = EDINICHI[u];
                    if (k == 1) {
                        switch (u) {
                            case 1:
                                txt = "одна";
                                break;
                            case 2:
                                txt = "две";
                                break;
                        }
                    }
                    sb.append(txt);
                    sb.append(" ");
                }
                //
            } else if (t == 1) {
                sb.append(DESYAT[u]);
                sb.append(" ");
            } else if (t > 1) {
                // 21 - двадцать один и больше
                sb.append(DESYATKI[t]);
                if (u > 0) {
                    sb.append(" ");
                    String ed =  EDINICHI[u];
                    if (k == 1) {
                        switch (u) {
                            case 1:
                                ed = "одна";
                                break;
                            case 2:
                                ed = "две";
                                break;
                            default:
                        }
                    }
                    sb.append(ed);
                }
                sb.append(" ");
            }
            // одна две три четыре пять шесть семь восемь девять десять
            if (k > 0 && (h + t + u > 0)) {
                if (k == 1) {
                    sb.append(tisyachi(h, t, u));
                } else {
                    sb.append(lions(h, t, u, k));
                }
                //
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    private static char lastNonWhitespace(CharSequence sb) {
        for (int i = sb.length() - 1; i >= 0; i--) {
            char ch = sb.charAt(i);
            if (!Character.isWhitespace(ch)) {
                return ch;
            }
        }
        return 0;
    }

    static String lions(int h, int t, int u, int k) {
        StringBuilder sb = new StringBuilder();
        sb.append(LIONS[k]);

        if (t == 0 || t > 1) {
            switch (u) {
                case 1:
                    break;

                case 2:
                case 3:
                case 4:
                    sb.append("а");
                    break;

                default:
                    sb.append("ов");
                    break;
            }
        } else {
            sb.append("ов");
        }
        return sb.toString();
    }

    static String tisyachi(int h, int t, int u) {
        String result = "тысяч";
        // от 0 до 9 или h*100
        if (t == 0 || t > 1) {
            switch (u) {
                case 1:
                    result = "тысяча";
                    break;
                case 2:
                case 3:
                case 4:
                    result = "тысячи";
                    break;
            }
        }
        return result;
    }

    /**
     * Сумма прописью: Семьсот семьдесят семь рублей 77 копеек
     * @param bi
     * @return
     */
    @Override
    public String amount(BigDecimal bi) {
        String txt = bi.toPlainString();

        int point = txt.indexOf('.');
        StringBuilder sb = new StringBuilder();
        String rubli = txt;
        if (point > 0) {
            rubli = txt.substring(0, point);
        }
        String celaya = formatImpl(rubli);
        sb.append(celaya);
        sb.append(" ");
        String currency = rubley(bi);

        sb.append(currency);
        sb.append(" ");
        int k = roundKopeyki(bi);
        assert (k >= 0 && k < 100);

        if (k < 10) {
            sb.append("0");
            sb.append(k);
        } else {
            sb.append(k);
        }
        sb.append(" ");
        sb.append(kopeyki(k));
        // to upper case
        toUpperCaseFirstLetter(sb);
        return sb.toString();
    }

    private static int roundKopeyki(BigDecimal b) {
        b = b.abs();
        int k = b.multiply(BigDecimal.valueOf(100)).remainder(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).intValue();
        return k;
    }

    private static String kopeyki(int k) {
        String result = "копеек";
        if (k > 10 && k < 20) {
            result = "копеек";
        } else {
            int last = k % 10;
            switch (last) {
                case 1:
                    result = "копейка";
                    break;
                case 2:
                case 3:
                case 4:
                    result = "копейки";
                    break;
                default:
                    result = "копеек";
            }
        }
        return result;
    }

    private static String rubley(BigDecimal amount) {
        BigInteger r = amount.setScale(0, RoundingMode.DOWN).toBigInteger();
        String result = "рублей";
        r = r.remainder(BigInteger.valueOf(100));
        if (r.compareTo(BigInteger.TEN) > 0 && r.compareTo(BigInteger.valueOf(20)) < 0) {
            result = "рублей";
        } else {
            int last = r.remainder(BigInteger.TEN).intValue();
            switch (last) {
                case 1:
                    result = "рубль";
                    break;
                case 2:
                case 3:
                case 4:
                    result = "рубля";
                    break;
                default:
                    result = "рублей";
            }
        }
        return result;
    }

    @Override
    public String convert(long number) {
        checkSupported(number);
        return formatImpl(String.valueOf(number));
    }
}
