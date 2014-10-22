package Core;

import java.util.Random;

/**
 * Created by dwite_000 on 23.10.2014.
 */
public class NumberGenerator {
    private String numberString;

    public NumberGenerator() {
        numberString = "";
    }
    public int genNumber() {
        Random r = new Random();
        int number;
        int type = r.nextInt(4);// 0 - simple number 1 - string number 2 - operation
        switch (type) {
            case 0 :
                number = generateRandom();
                break;
            default: number = generateRandom();
        }
        return number;
    }

    private int generateRandom() {
        String number = "";
        Random r = new Random();
        int genNumber = r.nextInt(1000 - 1) + 1;
        number = String.valueOf(genNumber);
        numberString = number;
        return genNumber;
    }

    public String getNumberString() { return numberString; }
}
