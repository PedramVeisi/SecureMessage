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

import ir.secure_msg.crypt.Base64;

import java.security.PublicKey;
import javax.crypto.Cipher;

public class RSAEncrypt {

	PublicKey publickKey;
	private static int TEXT_BLOCK_SIZE = 53;
	
	public String encrypt(byte[] text, PublicKey key) throws Exception {

		byte[] cipherText = null;

		String finalCipher = "";

		// get an RSA cipher object and print the provider
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		System.out.println("nProvider is: " + cipher.getProvider().getInfo());

		int n = text.length / TEXT_BLOCK_SIZE;
		int loop = n;
	
		int cpStart = 0;

		byte[] textBlock = new byte[TEXT_BLOCK_SIZE];

		if(text.length <= TEXT_BLOCK_SIZE)
			textBlock = text.clone();
		while (loop > 0){
			System.arraycopy(text, cpStart, textBlock, 0, TEXT_BLOCK_SIZE);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = cipher.doFinal(textBlock);

			finalCipher += Base64.encodeBytes(cipherText);
			cpStart += TEXT_BLOCK_SIZE;
			loop -= 1;
		}
		
		System.arraycopy(text, cpStart, textBlock, 0, text.length - TEXT_BLOCK_SIZE * n);

		cipher.init(Cipher.ENCRYPT_MODE, key);
		cipherText = cipher.doFinal(textBlock);

		finalCipher += Base64.encodeBytes(cipherText);
		
		return finalCipher;

				
	}
	
}