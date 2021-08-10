/*
 * Created on 5 Jan 2014 02:13:50 
 */
package bank.util;

import java.util.Random;

/**
 * This class will efficiently generate insecure random
 * alpha-numeric Strings.
 * 
 * The code is on 
 * <a href="https://gist.github.com/twaddington/3252334">GitHub</a>. 
 * 
 * <p>It is largely based on
 * <a href="http://stackoverflow.com/a/41156/772122">this example</a>
 * from Stack Overflow by erickson.</p>
 *
 * <p><a href="http://creativecommons.org/licenses/by-sa/3.0/">
 *   Licensed under an Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0)
 * </a></p>
 *
 * @author ingrid
 * 
 */
public class RandomString {
	
	private static final char[] symbols = new char[36];

	static {
		for (int idx = 0; idx < 10; ++idx)
			symbols[idx] = (char) ('0' + idx);
		for (int idx = 10; idx < 36; ++idx)
			symbols[idx] = (char) ('a' + idx - 10);
	}

	private final char[] buf;

	private final Random random = new Random();

	public RandomString(int length) {
		if (length < 1)
			throw new IllegalArgumentException("length < 1: " + length);
		buf = new char[length];
	}

	public String nextString() {
		for (int idx = 0; idx < buf.length; ++idx)
			buf[idx] = symbols[random.nextInt(symbols.length)];
		return new String(buf);
	}
	
}
