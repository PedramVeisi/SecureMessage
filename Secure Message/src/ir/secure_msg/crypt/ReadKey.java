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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class ReadKey {

	public static PublicKey readPublicKeyFromFile(String keyFilePath) {

		ObjectInputStream oin = null;

		try {

			FileInputStream fis = new FileInputStream(keyFilePath);
			DataInputStream in = new DataInputStream(fis);
			oin = new ObjectInputStream(new BufferedInputStream(in));
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PublicKey pubKey = fact.generatePublic(keySpec);
			return pubKey;

		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			try {
				oin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static PrivateKey readPrivateKeyFromFile(String keyFilePath) throws InvalidKeyException {

		ObjectInputStream oin = null;

		try {

			FileInputStream fis = new FileInputStream(keyFilePath);
			DataInputStream in = new DataInputStream(fis);
			oin = new ObjectInputStream(new BufferedInputStream(in));
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = fact.generatePrivate(keySpec);
			return privateKey;

		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			try {
				oin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			catch (NullPointerException e) {
				throw new InvalidKeyException();
			}
		}
	}
}
