/*
 	Copyright 2012 Pedram Veisi 

    This file is part of Secure Message.

    Secure Message is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Secure Message is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Secure Message.  If not, see <http://www.gnu.org/licenses/>.

 */

package ir.secure_msg.crypt;

import java.io.IOException;

public class XOREncryptDecrypt {

	static String key = "THIS IS JUST FOR FUN";

	public static String encrypt(String message) {

		String cipher = Base64.encodeBytes(XOR(message).getBytes());

		System.err.println(cipher);

		return cipher;
	}

	public static String decrypt(String cipher) {

		String message = null;
		try {
			message = XOR(new String(Base64.decode(cipher)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.err.println(message);

		return message;
	}

	private static String XOR(String message) {

		char[] messageArray = message.toCharArray();
		char[] keyArray = key.toCharArray();

		int msgLength = messageArray.length;
		int keyLength = keyArray.length;

		char[] cipherOrTextArray = new char[msgLength];

		for (int i = 0; i < msgLength; i++) {
			cipherOrTextArray[i] = (char) (messageArray[i] ^ keyArray[i
					% keyLength]);
		}

		String cipherOrText = new String(cipherOrTextArray);
		return cipherOrText;
	}

}
