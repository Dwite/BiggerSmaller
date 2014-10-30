package Core;
/*
 * Copyright (C) 2011 Vit <vitalker@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 */
public abstract class AbstractNumeral extends NumberConverter {

    public static final BigInteger MAX_SUPPORTED = new BigInteger("1000000000000000000000000000000000000").subtract(BigInteger.ONE);
    //10^33

    protected void checkSupported(Number number) {
        if (number instanceof Integer
                || number instanceof Long
                || number instanceof Short
                ||number instanceof Byte) {
        } else if (number instanceof BigInteger) {
            BigInteger bi = (BigInteger) number;
            if (bi.abs().compareTo(MAX_SUPPORTED) > 0) {
                throw new IllegalArgumentException("Max supported number:" + MAX_SUPPORTED);
            }
        } else {
            throw new IllegalArgumentException("Support only Integer numbers: BigInteger, Integer, Long and Short."
                    + "Floating-point is not supported");
        }
    }

    public abstract String convert(Number number);

    public abstract String amount(BigDecimal amount);

    public String amount(Number amount) {
        return amount(Util.toBigDecimal(amount));
    }
}

