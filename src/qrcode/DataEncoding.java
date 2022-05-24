package qrcode;

import java.nio.charset.StandardCharsets;
import reedsolomon.ErrorCorrectionEncoding;

public final class DataEncoding {

	/**
	 * @param input
	 * @param version
	 * @return
	 */
	
	//This method uses all other methods in order to encode a string.
	public static boolean[] byteModeEncoding(String input, int version) {
		int[] v1 = encodeString(input, QRCodeInfos.getMaxInputLength(version));
		int[] v2 = addInformations(v1);
		int[] v3 = fillSequence(v2, QRCodeInfos.getCodeWordsLength(version));
		int[] v4 = addErrorCorrection(v3, QRCodeInfos.getECCLength(version));
		return bytesToBinaryArray(v4);
	}

	/**
	 * @param input
	 *            The string to convert to ISO-8859-1
	 * @param maxLength
	 *          The maximal number of bytes to encode (will depend on the version of the QR code) 
	 * @return A array that represents the input in ISO-8859-1. The output is
	 *         truncated to fit the version capacity
	 */
	
	//This method uses encodes a string into bits and stores it into an integer array as an integer.
	public static int[] encodeString(String input, int maxLength) {
		byte[] tabByte = input.getBytes(StandardCharsets.ISO_8859_1);
		
		int[] tabEncoded = new int[maxLength];
		int i;
		if (tabByte.length < maxLength) {
			for (i = 0; i < tabByte.length ; i++) {
				tabEncoded[i] = tabByte[i] & 0xFF;
			}	
		} else {
			for (i = 0; i < maxLength ; i++) {
				tabEncoded[i] = tabByte[i] & 0xFF;
			}
		}
		
 		return tabEncoded;
	}


	/**
	 * Add the 12 bits information data and concatenate the bytes to it
	 * 
	 * @param inputBytes
	 *            the data byte sequence
	 * @return The input bytes with an header giving the type and size of the data
	 */
	
	//This method takes in bits as a string and adds 0 until the string represents a byte.
	public static String byter(String a) {
		do {
			a = "0" + a;
		} while (a.length() < 8);
		
		return a;
		
	}
	
	//This method adds information to the input bytes by treating them as a string. The string is then transformed back into integers.
	public static int[] addInformations(int[] inputBytes) {
		int[] tabInfo = new int[inputBytes.length + 2];
		
		String Length = byter(Integer.toBinaryString(inputBytes.length));		
		int i;
		String allBytes = byter(Integer.toBinaryString(inputBytes[0]));
		for (i = 1; i < inputBytes.length; i++) {
			allBytes += byter(Integer.toBinaryString(inputBytes[i]));
		}
		String completeString = "0100" + Length + allBytes + "0000";
		int a = 0;
		for (i = 0; i < completeString.length(); i +=  8) {
			tabInfo[a] = Integer.parseInt(completeString.substring(i, i+8), 2);
			a++;
		}
		return tabInfo;
	}
	
	/**
	 * Add padding bytes to the data until the size of the given array matches the
	 * finalLength
	 * 
	 * @param encodedData
	 *            the initial sequence of bytes
	 * @param finalLength
	 *            the minimum length of the returned array
	 * @return an array of length max(finalLength,encodedData.length) padded with
	 *         bytes 236,17
	 */
	
	//Adds a predetermined sequence at the end of the already encoded data. The length of the fill sequence varies according to the final length (input).
	public static int[] fillSequence(int[] encodedData, int finalLength) {
		int encodedLength = encodedData.length;
		if (finalLength <= encodedLength) {
			return encodedData;
		} else {
			int[] newEncodedData = new int[finalLength];
			int i;
			for (i = 0; i < encodedLength; i++) {
				newEncodedData[i] = encodedData[i];
			}
			i = finalLength - encodedLength;
			int a = 0;
			while (i > 0) {
				if (i > 0) {
					newEncodedData[encodedLength + a] = 236;
					i--;
					a++;
				}
				
				if (i > 0) {
					newEncodedData[encodedLength + a] = 17;
					i--;
					a++;
				}
			}
			return newEncodedData;
		}
	}

	private static void If(boolean b) {
		// TODO Auto-generated method stub
		
	}

	private static void While(boolean b) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Add the error correction to the encodedData
	 * 
	 * @param encodedData
	 *            The byte array representing the data encoded
	 * @param eccLength
	 *            the version of the QR code
	 * @return the original data concatenated with the error correction
	 */
	
	//Adds a error correction sequence to the encoded data. The length of said error correction data is an input parameter. 
	//The error correction data is provided by a method in ErrrorCorrectionEncoding. 
	public static int[] addErrorCorrection(int[] encodedData, int eccLength) {
		int[] eccBytes = ErrorCorrectionEncoding.encode(encodedData, eccLength);
		int[] newEncodedData = new int[encodedData.length + eccLength];
		int i;
		for (i = 0; i < encodedData.length; i++) {
			newEncodedData[i] = encodedData[i];
		}
		for (i = 0; i < eccBytes.length; i++) {
			newEncodedData[encodedData.length + i] = eccBytes[i];
		}
		return newEncodedData;
	}

	/**
	 * Encode the byte array into a binary array represented with boolean using the
	 * most significant bit first.
	 * 
	 * @param data
	 *            an array of bytes
	 * @return a boolean array representing the data in binary
	 */
	//This method takes an integer array as input and converts it to a boolean array which is eight times longer, that it then returns.
	public static boolean[] bytesToBinaryArray(int[] data) {
		boolean[] binaryData = new boolean[8*data.length];
		int i;
		int a;
		int b;
		int c = 0;
		String binaryString;
		for (i = 0; i < data.length; i++) {
			binaryString = Integer.toBinaryString(data[i]);
			if (binaryString.length() < 8) {
				do {
					binaryString = "0" + binaryString;
				} while (binaryString.length() < 8);
			}
			b = 0;
			for (a = c; a < c + 8 && a < binaryData.length; a++) {
				if (binaryString.charAt(b) == '0') {
					binaryData[a] = Boolean.parseBoolean("false");
				} else {
					binaryData[a] = Boolean.parseBoolean("true");
				}		
				b++;	
			}
			c += b;
		}
		return binaryData;
	}

}
