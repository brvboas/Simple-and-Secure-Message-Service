/**
 * Util.java
 * 
 * @authors 
 * Bruno Villas Boas da Costa
 * Renato Jensen Filho
 * Wagner Takeshi Obara
 * 
 */
package smss;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Classe que implementa os métodos úteis 
 */
public class Util {
    
    /*
     * Método que gera uma chave para cifrar/decifrar, de acordo com um id, que deve ser informado
     * no parâmetro id. Este método consiste em uma função hash que mapeia um dado id a uma mesma chave.
     */
    public static byte[] hash(String id) {
        String sen = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD2");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger hash = new BigInteger(1, md.digest(id.getBytes()));
        sen = hash.toString(16);
        byte[] b = sen.getBytes();
        byte[] c = new byte[16];
        for (int i = 0; i < 16; i++) {
            c[i] = (byte) (b[i] + b[i + 16]);
        }
        return c;
    }
    
    /*
     * Método que retorna o valor binário em String de um dado valor inteiro, com precisão de n bits
     * Os parâmetro de entrada são o valor inteiro que deve ser indicado no parâmetro valor e a precisão 
     * em nBits.
     */
    public static String getBynaryString(int valor, int nBits) {
        String s="";
        s = Integer.toBinaryString(valor);
        if (s.length()<nBits){
            int n= nBits-(s.length()%nBits);
            for (int i=0 ; i<n ;i++)
                s="0".concat(s);
        }
        return s;
    }
    
    /*
     * Método que gera um IV aleatório.
     */
    public static byte[] geraIV() {
        byte [] iv = new byte[16];
        int aStart=-127;
        int aEnd=128;
        Random random = new Random();
        
        for (int i=0;i<16;i++){
            //get the range, casting to long to avoid overflow problems
            long range = (long)aEnd - (long)aStart + 1;
            // compute a fraction of the range, 0 <= frac < range
            long fraction = (long)(range * random.nextDouble());
            int randomNumber =  (int)(fraction + aStart);
            iv[i] = (byte) randomNumber;
        }
        return iv;
    }
    
    public static String fromHex(byte[] hex) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hex.length; i++) {
            sb.append(Integer.toString((hex[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static byte[] toHex(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
