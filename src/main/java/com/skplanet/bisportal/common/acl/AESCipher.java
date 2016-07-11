package com.skplanet.bisportal.common.acl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Ho-Jin, Ha (mimul@wiseeco.com)
 *
 */
@Slf4j
public class AESCipher {
	private int encryptionMode = 0;
	public static final int AES_MODE_CBC = 0;
	public static final int AES_MODE_ECB = 1;
	//public static final String AES_NO_PADDING = "NoPadding";
	//public static final String AES_ZExRO_PADDING = "ZeroPadding";
	public static final String AES_PKCS7_PADDING = "PKCS5Padding";
	private Cipher aes = null;
	private SecretKeySpec key = null;
	private IvParameterSpec initalVector = null;

	public AESCipher(String key, String initialVector, String paddingMode) throws Exception {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(initialVector))
			throw new Exception("키와 IV 값을 확인하세요.");

		if (encryptionMode == AES_MODE_ECB)
			this.aes = Cipher.getInstance("AES/ECB/" + paddingMode);
		else
			this.aes = Cipher.getInstance("AES/CBC/" + paddingMode);

		this.key = new SecretKeySpec(this.hex2Bytes(key), "AES");
		this.initalVector = new IvParameterSpec(this.hex2Bytes(initialVector));
	}

	public String encrypt(byte[] content) {
		byte[] cipherText;
		String encrypt = null;

		try {
			if (content == null)
				return encrypt;
			if (encryptionMode == AES_MODE_ECB)
				aes.init(Cipher.ENCRYPT_MODE, this.key);
			else
				aes.init(Cipher.ENCRYPT_MODE, this.key, this.initalVector);
			cipherText = aes.doFinal(content);
			encrypt = new String(Base64.encodeBase64(cipherText), CharEncoding.UTF_8);
		} catch (Exception e) {
			log.error("encrypt {}", e);
		}
		return encrypt;

	}

	public String decrypt(byte[] content) {
		byte[] plainText;
		String decrypt = null;

		try {
			if (content == null)
				return decrypt;
			if (encryptionMode == AES_MODE_ECB) {
				aes.init(Cipher.DECRYPT_MODE, this.key);
			} else {
				aes.init(Cipher.DECRYPT_MODE, this.key, this.initalVector);
			}
			plainText = aes.doFinal(Base64.decodeBase64(content));
			decrypt = new String(plainText, CharEncoding.UTF_8);
		} catch (Exception e) {
			log.error("decrypt {}", e);
		}
		return decrypt;
	}

	private byte[] hex2Bytes(String hex) {
		byte[] b = null;
		int len;
		try {
			len = hex.length();
			if ((len % 2) != 0)
				return null;

			b = new byte[len / 2];
			for (int i = 0; i < len; i += 2) {
				b[i >> 1] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
			}
		} catch (Exception e) {
			log.error("hex2Bytes {}", e);
		}
		return b;
	}

	public void setEncryptionMode(int encryptionMode) {
		this.encryptionMode = encryptionMode;
	}
}
