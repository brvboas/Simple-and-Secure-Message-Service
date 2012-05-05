/**
 * Conf.java
 * 
 * @authors 
 * Bruno Villas Boas da Costa
 * Renato Jensen Filho
 * Wagner Takeshi Obara
 * 
 */
package smss;

/*
 * Classe que implementa a mensagem Conf
 */
public class Conf{
    private int tipo;
    private int codigoErro;

    public Conf() {
        this.tipo=TiposMensagens.CONF;
    }
    
    /*
     * Método que transforma uma dada mensagem de array de bytes para objeto do tipo Conf.
     * Deve ser informado a mensagem no parametro b.
     */
    void toDados(byte[] b) {
        //Recupera o primeiro byte de b em valor binário
        String s =  Util.getBynaryString(b[0],8) ;
        //os 4 digitos mais significativos corresponde ao tipo e os 4 menos significativos
        //corresponde ao código de erro.
        tipo = Integer.parseInt(s.substring(0,4) , 2);
        codigoErro = Integer.parseInt(s.substring(4,8) , 2);
    }
    
    /*
     * Método que transforma Um objeto do tipo Conf para uma mensagem em array de bytes
     * Retorna um array de bytes.
     */
    public byte[] toByte(){
        byte [] byteRetorno = new byte[1];
        //Concatena o valor binario do tipo e do código de erro.
        String s = Util.getBynaryString(tipo,4) + Util.getBynaryString(codigoErro,4);
        //Insere na primeira posição do array.
        byteRetorno[0]=Byte.parseByte(s, 2);
        return byteRetorno;
    }
    
  
    /*
     * Métodos geters e seters
     */
    public int getCodigoErro() {
        return codigoErro;
    }

    public void setCodigoErro(int codigoErro) {
        this.codigoErro = codigoErro;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Conf{" + "tipo=" + tipo + ", codigoErro=" + codigoErro + '}';
    }
    
    
}
