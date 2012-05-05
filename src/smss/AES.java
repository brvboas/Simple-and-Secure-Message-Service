/**
 *AES.java
 * 
 * @authors 
 * Bruno Villas Boas da Costa
 * Renato Jensen Filho
 * Wagner Takeshi Obara
 * 
 */
package smss;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

/*
 * Classe que implementa a criptografia AES, com chave de 128bits, modo CFB, NoPadding.
 */
public class AES {

    private byte[] key = {63, 68, 61, 76, 65, 20, 31, 20, 64, 65, 20, 74, 65, 73, 74, 65}; //chavepadrao
    private byte[] iv;
    private IvParameterSpec ivParameterSpec;
    
    /*
     * Método que cifra uma dada mensagem. A mensagem a ser cifrada deve ser inserida no parâmetro input
     * e a chave no parâmetro key. O retorno é um array de bytes.
     */
    public byte[] encode(byte[] input, byte[] key) throws NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        //Cria a chave secreta, apartir da chave informada no parametro.
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
        //inicia o cifrador no modo de encriptação.
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(input);
        return encrypted;
    }
    
    /*
     * Método que decifra uma dada mensagem. A mensagem a ser decifrada deve ser inserida no parâmetro input
     * e a chave no parâmetro key. O retorno é um array de bytes.
     */
    public byte[] decode(byte[] input, byte[] key) throws NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        //Cria a chave secreta, apartir da chave informada no parametro.
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
        //inicia o cifrador no modo de decriptação.
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
        byte[] decrypted = cipher.doFinal(input);
        return decrypted;
    }

    public static String nullPadString(String original) {
        StringBuffer output = new StringBuffer(original);
        int remain = output.length() % 16;
        if (remain != 0) {
            remain = 16 - remain;
            for (int i = 0; i < remain; i++) {
                output.append((char) 0);
            }
        }
        return output.toString();
    }

    /*
     * Método set do parâmetro iv
     */
    public void setIv(byte[] iv) {
        this.iv = iv;
        ivParameterSpec = new IvParameterSpec(iv);
    }
}