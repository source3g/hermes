package com.source3g.hermes.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',

	'A', 'B', 'C', 'D', 'E', 'F' };

	public static void main(String[] args)

	{

		System.out.println("md5");
		System.out.println(md5sum("/dht.cfg"));
	}

	public static String toHexString(byte[] b) {

		StringBuilder sb = new StringBuilder(b.length * 2);

		for (int i = 0; i < b.length; i++) {

			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);

			sb.append(HEX_DIGITS[b[i] & 0x0f]);

		}

		return sb.toString();

	}

	public static String md5sum(String filename) {

		InputStream fis;

		byte[] buffer = new byte[1024];

		int numRead = 0;

		MessageDigest md5;

		try {

			fis = new FileInputStream(filename);

			md5 = MessageDigest.getInstance("MD5");

			while ((numRead = fis.read(buffer)) > 0) {

				md5.update(buffer, 0, numRead);

			}

			fis.close();

			return toHexString(md5.digest());

		} catch (Exception e) {

			System.out.println("error");

			return null;

		}

	}

	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}
}