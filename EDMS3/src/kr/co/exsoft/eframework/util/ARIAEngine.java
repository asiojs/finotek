package kr.co.exsoft.eframework.util;

import java.io.PrintStream;
import java.security.InvalidKeyException;

/***
 * ARIA 암호화 엔진 클래스
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class ARIAEngine {
	
	public ARIAEngine(int keySize, String privateKey) throws InvalidKeyException {
		this.keySize = 0;
		numberOfRounds = 0;
		masterKey = null;
		encRoundKeys = null;
		decRoundKeys = null;
		setKeySize(keySize);
		
		/* Constructor Extend */
		byte[] mk = new byte[32];
		byte[] p1Key = this.publicKey.getBytes();
		System.arraycopy(p1Key, 0, mk, 0, p1Key.length);
		this.setKey(mk);
		this.setupRoundKeys();
		
		byte[] mk2 = new byte[32];
		byte[] privateKeyByte = privateKey.getBytes();
		if (privateKeyByte.length > 32) {
			System.arraycopy(privateKeyByte, 0, mk2, 0, 32);
		} else {
			System.arraycopy(privateKeyByte, 0, mk2, 0, privateKeyByte.length);
		}
		byte[] makeKey = new byte[32];
		this.encrypt(mk2, makeKey, mk2.length);
		this.setKey(makeKey);
		this.setupRoundKeys();
	}	

	void reset() {
		keySize = 0;
		numberOfRounds = 0;
		masterKey = null;
		encRoundKeys = null;
		decRoundKeys = null;
	}

	int getKeySize() {
		return keySize;
	}

	public int getBufferSize(int size) {
		if (size % 16 != 0)
			size = (size / 16 + 1) * 16;
		return size;
	}

	void setKeySize(int keySize) throws InvalidKeyException {
		reset();
		if (keySize != 128 && keySize != 192 && keySize != 256)
			throw new InvalidKeyException("keySize=" + keySize);
		this.keySize = keySize;
		switch (keySize) {
		case 128:
			numberOfRounds = 12;
			break;

		case 192:
			numberOfRounds = 14;
			break;

		case 256:
			numberOfRounds = 16;
			break;
		}
	}

	public void setKey(byte masterKey[]) throws InvalidKeyException {
		if (masterKey.length * 8 < keySize) {
			throw new InvalidKeyException("masterKey size=" + masterKey.length);
		} else {
			decRoundKeys = null;
			encRoundKeys = null;
			this.masterKey = masterKey.clone();
			return;
		}
	}

	void setupEncRoundKeys() throws InvalidKeyException {
		if (keySize == 0)
			throw new InvalidKeyException("keySize");
		if (masterKey == null)
			throw new InvalidKeyException("masterKey");
		if (encRoundKeys == null)
			encRoundKeys = new int[4 * (numberOfRounds + 1)];
		decRoundKeys = null;
		doEncKeySetup(masterKey, encRoundKeys, keySize);
	}

	void setupDecRoundKeys() throws InvalidKeyException {
		if (keySize == 0)
			throw new InvalidKeyException("keySize");
		if (encRoundKeys == null) {
			if (masterKey == null)
				throw new InvalidKeyException("masterKey");
			setupEncRoundKeys();
		}
		decRoundKeys = encRoundKeys.clone();
		doDecKeySetup(masterKey, decRoundKeys, keySize);
	}

	public void setupRoundKeys() throws InvalidKeyException {
		setupDecRoundKeys();
	}

	private static void doCrypt(byte i[], int ioffset, int rk[], int nr,
			byte o[], int ooffset) {
		int j = 0;
		int t0 = toInt(i[0 + ioffset], i[1 + ioffset], i[2 + ioffset],
				i[3 + ioffset]);
		int t1 = toInt(i[4 + ioffset], i[5 + ioffset], i[6 + ioffset],
				i[7 + ioffset]);
		int t2 = toInt(i[8 + ioffset], i[9 + ioffset], i[10 + ioffset],
				i[11 + ioffset]);
		int t3 = toInt(i[12 + ioffset], i[13 + ioffset], i[14 + ioffset],
				i[15 + ioffset]);
		for (int r = 1; r < nr / 2; r++) {
			t0 ^= rk[j++];
			t1 ^= rk[j++];
			t2 ^= rk[j++];
			t3 ^= rk[j++];
			t0 = TS1[t0 >>> 24 & 0xff] ^ TS2[t0 >>> 16 & 0xff]
					^ TX1[t0 >>> 8 & 0xff] ^ TX2[t0 & 0xff];
			t1 = TS1[t1 >>> 24 & 0xff] ^ TS2[t1 >>> 16 & 0xff]
					^ TX1[t1 >>> 8 & 0xff] ^ TX2[t1 & 0xff];
			t2 = TS1[t2 >>> 24 & 0xff] ^ TS2[t2 >>> 16 & 0xff]
					^ TX1[t2 >>> 8 & 0xff] ^ TX2[t2 & 0xff];
			t3 = TS1[t3 >>> 24 & 0xff] ^ TS2[t3 >>> 16 & 0xff]
					^ TX1[t3 >>> 8 & 0xff] ^ TX2[t3 & 0xff];
			t1 ^= t2;
			t2 ^= t3;
			t0 ^= t1;
			t3 ^= t1;
			t2 ^= t0;
			t1 ^= t2;
			t1 = badc(t1);
			t2 = cdab(t2);
			t3 = dcba(t3);
			t1 ^= t2;
			t2 ^= t3;
			t0 ^= t1;
			t3 ^= t1;
			t2 ^= t0;
			t1 ^= t2;
			t0 ^= rk[j++];
			t1 ^= rk[j++];
			t2 ^= rk[j++];
			t3 ^= rk[j++];
			t0 = TX1[t0 >>> 24 & 0xff] ^ TX2[t0 >>> 16 & 0xff]
					^ TS1[t0 >>> 8 & 0xff] ^ TS2[t0 & 0xff];
			t1 = TX1[t1 >>> 24 & 0xff] ^ TX2[t1 >>> 16 & 0xff]
					^ TS1[t1 >>> 8 & 0xff] ^ TS2[t1 & 0xff];
			t2 = TX1[t2 >>> 24 & 0xff] ^ TX2[t2 >>> 16 & 0xff]
					^ TS1[t2 >>> 8 & 0xff] ^ TS2[t2 & 0xff];
			t3 = TX1[t3 >>> 24 & 0xff] ^ TX2[t3 >>> 16 & 0xff]
					^ TS1[t3 >>> 8 & 0xff] ^ TS2[t3 & 0xff];
			t1 ^= t2;
			t2 ^= t3;
			t0 ^= t1;
			t3 ^= t1;
			t2 ^= t0;
			t1 ^= t2;
			t3 = badc(t3);
			t0 = cdab(t0);
			t1 = dcba(t1);
			t1 ^= t2;
			t2 ^= t3;
			t0 ^= t1;
			t3 ^= t1;
			t2 ^= t0;
			t1 ^= t2;
		}

		t0 ^= rk[j++];
		t1 ^= rk[j++];
		t2 ^= rk[j++];
		t3 ^= rk[j++];
		t0 = TS1[t0 >>> 24 & 0xff] ^ TS2[t0 >>> 16 & 0xff]
				^ TX1[t0 >>> 8 & 0xff] ^ TX2[t0 & 0xff];
		t1 = TS1[t1 >>> 24 & 0xff] ^ TS2[t1 >>> 16 & 0xff]
				^ TX1[t1 >>> 8 & 0xff] ^ TX2[t1 & 0xff];
		t2 = TS1[t2 >>> 24 & 0xff] ^ TS2[t2 >>> 16 & 0xff]
				^ TX1[t2 >>> 8 & 0xff] ^ TX2[t2 & 0xff];
		t3 = TS1[t3 >>> 24 & 0xff] ^ TS2[t3 >>> 16 & 0xff]
				^ TX1[t3 >>> 8 & 0xff] ^ TX2[t3 & 0xff];
		t1 ^= t2;
		t2 ^= t3;
		t0 ^= t1;
		t3 ^= t1;
		t2 ^= t0;
		t1 ^= t2;
		t1 = badc(t1);
		t2 = cdab(t2);
		t3 = dcba(t3);
		t1 ^= t2;
		t2 ^= t3;
		t0 ^= t1;
		t3 ^= t1;
		t2 ^= t0;
		t1 ^= t2;
		t0 ^= rk[j++];
		t1 ^= rk[j++];
		t2 ^= rk[j++];
		t3 ^= rk[j++];
		o[0 + ooffset] = (byte) (X1[0xff & t0 >>> 24] ^ rk[j] >>> 24);
		o[1 + ooffset] = (byte) (X2[0xff & t0 >>> 16] ^ rk[j] >>> 16);
		o[2 + ooffset] = (byte) (S1[0xff & t0 >>> 8] ^ rk[j] >>> 8);
		o[3 + ooffset] = (byte) (S2[0xff & t0] ^ rk[j]);
		o[4 + ooffset] = (byte) (X1[0xff & t1 >>> 24] ^ rk[j + 1] >>> 24);
		o[5 + ooffset] = (byte) (X2[0xff & t1 >>> 16] ^ rk[j + 1] >>> 16);
		o[6 + ooffset] = (byte) (S1[0xff & t1 >>> 8] ^ rk[j + 1] >>> 8);
		o[7 + ooffset] = (byte) (S2[0xff & t1] ^ rk[j + 1]);
		o[8 + ooffset] = (byte) (X1[0xff & t2 >>> 24] ^ rk[j + 2] >>> 24);
		o[9 + ooffset] = (byte) (X2[0xff & t2 >>> 16] ^ rk[j + 2] >>> 16);
		o[10 + ooffset] = (byte) (S1[0xff & t2 >>> 8] ^ rk[j + 2] >>> 8);
		o[11 + ooffset] = (byte) (S2[0xff & t2] ^ rk[j + 2]);
		o[12 + ooffset] = (byte) (X1[0xff & t3 >>> 24] ^ rk[j + 3] >>> 24);
		o[13 + ooffset] = (byte) (X2[0xff & t3 >>> 16] ^ rk[j + 3] >>> 16);
		o[14 + ooffset] = (byte) (S1[0xff & t3 >>> 8] ^ rk[j + 3] >>> 8);
		o[15 + ooffset] = (byte) (S2[0xff & t3] ^ rk[j + 3]);
	}

	void encrypt(byte i[], int ioffset, byte o[], int ooffset)
			throws InvalidKeyException {
		if (keySize == 0)
			throw new InvalidKeyException("keySize");
		if (encRoundKeys == null) {
			if (masterKey == null)
				throw new InvalidKeyException("masterKey");
			setupEncRoundKeys();
		}
		doCrypt(i, ioffset, encRoundKeys, numberOfRounds, o, ooffset);
	}

	byte[] encrypt(byte i[], int ioffset) throws InvalidKeyException {
		byte o[] = new byte[16];
		encrypt(i, ioffset, o, 0);
		return o;
	}

	public void encrypt(byte plain[], byte cipher[], int size)
			throws InvalidKeyException {
		int len = getBufferSize(size);
		int iLoop = len / 16;
		int iMod = size % 16;
		int iCopySize = 16;
		int offset = 0;
		byte in[] = new byte[16];
		for (int i = 0; i < iLoop; i++) {
			if (i >= iLoop - 1)
				if (iMod == 0)
					iCopySize = 16;
				else
					iCopySize = iMod;
			for (int j = 0; j < 16; j++)
				in[j] = 0;

			System.arraycopy(plain, offset, in, 0, iCopySize);
			encrypt(in, 0, cipher, offset);
			offset += iCopySize;
		}

	}

	void decrypt(byte i[], int ioffset, byte o[], int ooffset)
			throws InvalidKeyException {
		if (keySize == 0)
			throw new InvalidKeyException("keySize");
		if (decRoundKeys == null) {
			if (masterKey == null)
				throw new InvalidKeyException("masterKey");
			setupDecRoundKeys();
		}
		doCrypt(i, ioffset, decRoundKeys, numberOfRounds, o, ooffset);
	}

	byte[] decrypt(byte i[], int ioffset) throws InvalidKeyException {
		byte o[] = new byte[16];
		decrypt(i, ioffset, o, 0);
		return o;
	}

	public void decrypt(byte cipher[], byte plain[], int size)
			throws InvalidKeyException {
		int iLoop = size / 16;
		int offset = 0;
		for (int i = 0; i < iLoop; i++) {
			decrypt(cipher, offset, plain, offset);
			offset += 16;
		}

	}

	private static void doEncKeySetup(byte mk[], int rk[], int keyBits) {
		int j = 0;
		int w0[] = new int[4];
		int w1[] = new int[4];
		int w2[] = new int[4];
		int w3[] = new int[4];
		w0[0] = toInt(mk[0], mk[1], mk[2], mk[3]);
		w0[1] = toInt(mk[4], mk[5], mk[6], mk[7]);
		w0[2] = toInt(mk[8], mk[9], mk[10], mk[11]);
		w0[3] = toInt(mk[12], mk[13], mk[14], mk[15]);
		int q = (keyBits - 128) / 64;
		int t0 = w0[0] ^ KRK[q][0];
		int t1 = w0[1] ^ KRK[q][1];
		int t2 = w0[2] ^ KRK[q][2];
		int t3 = w0[3] ^ KRK[q][3];
		t0 = TS1[t0 >>> 24 & 0xff] ^ TS2[t0 >>> 16 & 0xff]
				^ TX1[t0 >>> 8 & 0xff] ^ TX2[t0 & 0xff];
		t1 = TS1[t1 >>> 24 & 0xff] ^ TS2[t1 >>> 16 & 0xff]
				^ TX1[t1 >>> 8 & 0xff] ^ TX2[t1 & 0xff];
		t2 = TS1[t2 >>> 24 & 0xff] ^ TS2[t2 >>> 16 & 0xff]
				^ TX1[t2 >>> 8 & 0xff] ^ TX2[t2 & 0xff];
		t3 = TS1[t3 >>> 24 & 0xff] ^ TS2[t3 >>> 16 & 0xff]
				^ TX1[t3 >>> 8 & 0xff] ^ TX2[t3 & 0xff];
		t1 ^= t2;
		t2 ^= t3;
		t0 ^= t1;
		t3 ^= t1;
		t2 ^= t0;
		t1 ^= t2;
		t1 = badc(t1);
		t2 = cdab(t2);
		t3 = dcba(t3);
		t1 ^= t2;
		t2 ^= t3;
		t0 ^= t1;
		t3 ^= t1;
		t2 ^= t0;
		t1 ^= t2;
		if (keyBits > 128) {
			w1[0] = toInt(mk[16], mk[17], mk[18], mk[19]);
			w1[1] = toInt(mk[20], mk[21], mk[22], mk[23]);
			if (keyBits > 192) {
				w1[2] = toInt(mk[24], mk[25], mk[26], mk[27]);
				w1[3] = toInt(mk[28], mk[29], mk[30], mk[31]);
			} else {
				w1[2] = w1[3] = 0;
			}
		} else {
			w1[0] = w1[1] = w1[2] = w1[3] = 0;
		}
		w1[0] ^= t0;
		w1[1] ^= t1;
		w1[2] ^= t2;
		w1[3] ^= t3;
		t0 = w1[0];
		t1 = w1[1];
		t2 = w1[2];
		t3 = w1[3];
		q = q != 2 ? q + 1 : 0;
		t0 ^= KRK[q][0];
		t1 ^= KRK[q][1];
		t2 ^= KRK[q][2];
		t3 ^= KRK[q][3];
		t0 = TX1[t0 >>> 24 & 0xff] ^ TX2[t0 >>> 16 & 0xff]
				^ TS1[t0 >>> 8 & 0xff] ^ TS2[t0 & 0xff];
		t1 = TX1[t1 >>> 24 & 0xff] ^ TX2[t1 >>> 16 & 0xff]
				^ TS1[t1 >>> 8 & 0xff] ^ TS2[t1 & 0xff];
		t2 = TX1[t2 >>> 24 & 0xff] ^ TX2[t2 >>> 16 & 0xff]
				^ TS1[t2 >>> 8 & 0xff] ^ TS2[t2 & 0xff];
		t3 = TX1[t3 >>> 24 & 0xff] ^ TX2[t3 >>> 16 & 0xff]
				^ TS1[t3 >>> 8 & 0xff] ^ TS2[t3 & 0xff];
		t1 ^= t2;
		t2 ^= t3;
		t0 ^= t1;
		t3 ^= t1;
		t2 ^= t0;
		t1 ^= t2;
		t3 = badc(t3);
		t0 = cdab(t0);
		t1 = dcba(t1);
		t1 ^= t2;
		t2 ^= t3;
		t0 ^= t1;
		t3 ^= t1;
		t2 ^= t0;
		t1 ^= t2;
		t0 ^= w0[0];
		t1 ^= w0[1];
		t2 ^= w0[2];
		t3 ^= w0[3];
		w2[0] = t0;
		w2[1] = t1;
		w2[2] = t2;
		w2[3] = t3;
		q = q != 2 ? q + 1 : 0;
		t0 ^= KRK[q][0];
		t1 ^= KRK[q][1];
		t2 ^= KRK[q][2];
		t3 ^= KRK[q][3];
		t0 = TS1[t0 >>> 24 & 0xff] ^ TS2[t0 >>> 16 & 0xff]
				^ TX1[t0 >>> 8 & 0xff] ^ TX2[t0 & 0xff];
		t1 = TS1[t1 >>> 24 & 0xff] ^ TS2[t1 >>> 16 & 0xff]
				^ TX1[t1 >>> 8 & 0xff] ^ TX2[t1 & 0xff];
		t2 = TS1[t2 >>> 24 & 0xff] ^ TS2[t2 >>> 16 & 0xff]
				^ TX1[t2 >>> 8 & 0xff] ^ TX2[t2 & 0xff];
		t3 = TS1[t3 >>> 24 & 0xff] ^ TS2[t3 >>> 16 & 0xff]
				^ TX1[t3 >>> 8 & 0xff] ^ TX2[t3 & 0xff];
		t1 ^= t2;
		t2 ^= t3;
		t0 ^= t1;
		t3 ^= t1;
		t2 ^= t0;
		t1 ^= t2;
		t1 = badc(t1);
		t2 = cdab(t2);
		t3 = dcba(t3);
		t1 ^= t2;
		t2 ^= t3;
		t0 ^= t1;
		t3 ^= t1;
		t2 ^= t0;
		t1 ^= t2;
		w3[0] = t0 ^ w1[0];
		w3[1] = t1 ^ w1[1];
		w3[2] = t2 ^ w1[2];
		w3[3] = t3 ^ w1[3];
		gsrk(w0, w1, 19, rk, j);
		j += 4;
		gsrk(w1, w2, 19, rk, j);
		j += 4;
		gsrk(w2, w3, 19, rk, j);
		j += 4;
		gsrk(w3, w0, 19, rk, j);
		j += 4;
		gsrk(w0, w1, 31, rk, j);
		j += 4;
		gsrk(w1, w2, 31, rk, j);
		j += 4;
		gsrk(w2, w3, 31, rk, j);
		j += 4;
		gsrk(w3, w0, 31, rk, j);
		j += 4;
		gsrk(w0, w1, 67, rk, j);
		j += 4;
		gsrk(w1, w2, 67, rk, j);
		j += 4;
		gsrk(w2, w3, 67, rk, j);
		j += 4;
		gsrk(w3, w0, 67, rk, j);
		j += 4;
		gsrk(w0, w1, 97, rk, j);
		j += 4;
		if (keyBits > 128) {
			gsrk(w1, w2, 97, rk, j);
			j += 4;
			gsrk(w2, w3, 97, rk, j);
			j += 4;
		}
		if (keyBits > 192) {
			gsrk(w3, w0, 97, rk, j);
			j += 4;
			gsrk(w0, w1, 109, rk, j);
		}
	}

	private static void doDecKeySetup(byte mk[], int rk[], int keyBits) {
		int a = 0;
		int t[] = new int[4];
		int z = 32 + keyBits / 8;
		swapBlocks(rk, 0, z);
		a += 4;
		for (z -= 4; a < z; z -= 4) {
			swapAndDiffuse(rk, a, z, t);
			a += 4;
		}

		diff(rk, a, t, 0);
		rk[a] = t[0];
		rk[a + 1] = t[1];
		rk[a + 2] = t[2];
		rk[a + 3] = t[3];
	}

	private static int toInt(byte b0, byte b1, byte b2, byte b3) {
		return (b0 & 0xff) << 24 ^ (b1 & 0xff) << 16 ^ (b2 & 0xff) << 8 ^ b3
				& 0xff;
	}

	private static int m(int t) {
		return 0x10101 * (t >>> 24 & 0xff) ^ 0x1000101 * (t >>> 16 & 0xff)
				^ 0x1010001 * (t >>> 8 & 0xff) ^ 0x1010100 * (t & 0xff);
	}

	private static final int badc(int t) {
		return t << 8 & 0xff00ff00 ^ t >>> 8 & 0xff00ff;
	}

	private static final int cdab(int t) {
		return t << 16 & 0xffff0000 ^ t >>> 16 & 0xffff;
	}

	private static final int dcba(int t) {
		return (t & 0xff) << 24 ^ (t & 0xff00) << 8 ^ (t & 0xff0000) >>> 8
				^ (t & 0xff000000) >>> 24;
	}

	private static final void gsrk(int x[], int y[], int rot, int rk[],
			int offset) {
		int q = 4 - rot / 32;
		int r = rot % 32;
		int s = 32 - r;
		rk[offset] = x[0] ^ y[q % 4] >>> r ^ y[(q + 3) % 4] << s;
		rk[offset + 1] = x[1] ^ y[(q + 1) % 4] >>> r ^ y[q % 4] << s;
		rk[offset + 2] = x[2] ^ y[(q + 2) % 4] >>> r ^ y[(q + 1) % 4] << s;
		rk[offset + 3] = x[3] ^ y[(q + 3) % 4] >>> r ^ y[(q + 2) % 4] << s;
	}

	private static final void diff(int i[], int offset1, int o[], int offset2) {
		int t0 = m(i[offset1]);
		int t1 = m(i[offset1 + 1]);
		int t2 = m(i[offset1 + 2]);
		int t3 = m(i[offset1 + 3]);
		t1 ^= t2;
		t2 ^= t3;
		t0 ^= t1;
		t3 ^= t1;
		t2 ^= t0;
		t1 ^= t2;
		t1 = badc(t1);
		t2 = cdab(t2);
		t3 = dcba(t3);
		t1 ^= t2;
		t2 ^= t3;
		t0 ^= t1;
		t3 ^= t1;
		t2 ^= t0;
		t1 ^= t2;
		o[offset2] = t0;
		o[offset2 + 1] = t1;
		o[offset2 + 2] = t2;
		o[offset2 + 3] = t3;
	}

	private static final void swapBlocks(int arr[], int offset1, int offset2) {
		for (int i = 0; i < 4; i++) {
			int t = arr[offset1 + i];
			arr[offset1 + i] = arr[offset2 + i];
			arr[offset2 + i] = t;
		}

	}

	private static final void swapAndDiffuse(int arr[], int offset1,
			int offset2, int tmp[]) {
		diff(arr, offset1, tmp, 0);
		diff(arr, offset2, arr, offset1);
		arr[offset2] = tmp[0];
		arr[offset2 + 1] = tmp[1];
		arr[offset2 + 2] = tmp[2];
		arr[offset2 + 3] = tmp[3];
	}

	public static void printBlock(PrintStream out, byte b[]) {
		for (int i = 0; i < 4; i++)
			byteToHex(out, b[i]);

		out.print(" ");
		for (int i = 4; i < 8; i++)
			byteToHex(out, b[i]);

		out.print(" ");
		for (int i = 8; i < 12; i++)
			byteToHex(out, b[i]);

		out.print(" ");
		for (int i = 12; i < 16; i++)
			byteToHex(out, b[i]);

	}

	public static void printBlockEx(PrintStream out, byte b[]) {
		for (int i = 0; i < b.length; i++)
			if (b[i] != 0)
				byteToHex(out, b[i]);

	}
	
	private static void byteToHex(PrintStream out, byte b) {
		char buf[] = { HEX_DIGITS[b >>> 4 & 0xf], HEX_DIGITS[b & 0xf] };
		out.print(new String(buf));
	}
	
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static final int KRK[][] = {
			{ 0x517cc1b7, 0x27220a94, 0xfe13abe8, 0xfa9a6ee0 },
			{ 0x6db14acc, 0x9e21c820, 0xff28b1d5, 0xef5de2b0 },
			{ 0xdb92371d, 0x2126e970, 0x3249775, 0x4e8c90e } };

	private static final byte S1[];

	private static final byte S2[];

	private static final byte X1[];

	private static final byte X2[];

	private static final int TS1[];

	private static final int TS2[];

	private static final int TX1[];

	private static final int TX2[];

	private int keySize = 256;

	private int numberOfRounds;
	
	private String publicKey = "[eXsoft_Crypt_Ctrl]";

	private byte masterKey[];

	private int encRoundKeys[];

	private int decRoundKeys[];

	static {
		S1 = new byte[256];
		S2 = new byte[256];
		X1 = new byte[256];
		X2 = new byte[256];
		TS1 = new int[256];
		TS2 = new int[256];
		TX1 = new int[256];
		TX2 = new int[256];
		int exp[] = new int[256];
		int log[] = new int[256];
		exp[0] = 1;
		for (int i = 1; i < 256; i++) {
			int j = exp[i - 1] << 1 ^ exp[i - 1];
			if ((j & 0x100) != 0)
				j ^= 0x11b;
			exp[i] = j;
		}

		for (int i = 1; i < 255; i++)
			log[exp[i]] = i;

		int A[][] = { { 1, 0, 0, 0, 1, 1, 1, 1 }, { 1, 1, 0, 0, 0, 1, 1, 1 },
				{ 1, 1, 1, 0, 0, 0, 1, 1 }, { 1, 1, 1, 1, 0, 0, 0, 1 },
				{ 1, 1, 1, 1, 1, 0, 0, 0 }, { 0, 1, 1, 1, 1, 1, 0, 0 },
				{ 0, 0, 1, 1, 1, 1, 1, 0 }, { 0, 0, 0, 1, 1, 1, 1, 1 } };
		int B[][] = { { 0, 1, 0, 1, 1, 1, 1, 0 }, { 0, 0, 1, 1, 1, 1, 0, 1 },
				{ 1, 1, 0, 1, 0, 1, 1, 1 }, { 1, 0, 0, 1, 1, 1, 0, 1 },
				{ 0, 0, 1, 0, 1, 1, 0, 0 }, { 1, 0, 0, 0, 0, 0, 0, 1 },
				{ 0, 1, 0, 1, 1, 1, 0, 1 }, { 1, 1, 0, 1, 0, 0, 1, 1 } };
		for (int i = 0; i < 256; i++) {
			int t = 0;
			int p;
			if (i == 0)
				p = 0;
			else
				p = exp[255 - log[i]];
			for (int j = 0; j < 8; j++) {
				int s = 0;
				for (int k = 0; k < 8; k++)
					if ((p >>> 7 - k & 1) != 0)
						s ^= A[k][j];

				t = t << 1 ^ s;
			}

			t ^= 0x63;
			S1[i] = (byte) t;
			X1[t] = (byte) i;
		}

		for (int i = 0; i < 256; i++) {
			int t = 0;
			int p;
			if (i == 0)
				p = 0;
			else
				p = exp[(247 * log[i]) % 255];
			for (int j = 0; j < 8; j++) {
				int s = 0;
				for (int k = 0; k < 8; k++)
					if ((p >>> k & 1) != 0)
						s ^= B[7 - j][k];

				t = t << 1 ^ s;
			}

			t ^= 0xe2;
			S2[i] = (byte) t;
			X2[t] = (byte) i;
		}

		for (int i = 0; i < 256; i++) {
			TS1[i] = 0x10101 * (S1[i] & 0xff);
			TS2[i] = 0x1000101 * (S2[i] & 0xff);
			TX1[i] = 0x1010001 * (X1[i] & 0xff);
			TX2[i] = 0x1010100 * (X2[i] & 0xff);
		}

	}
	
	// byte[] to hex 
	public static String byteArrayToHex(byte[] ba) { 
	    if (ba == null || ba.length == 0) { 
	        return null; 
	    } 

	    StringBuffer sb = new StringBuffer(ba.length * 2); 
	    String hexNumber; 
	    for (int x = 0; x < ba.length; x++) { 
	        hexNumber = "0" + Integer.toHexString(0xff & ba[x]); 

	        sb.append(hexNumber.substring(hexNumber.length() - 2)); 
	    } 
	    return sb.toString(); 
	}
	public String getPublicKey(){
		return publicKey;
	}
}