/**
 * Dados.java
 * 
 * @authors 
 * Bruno Villas Boas da Costa
 * Renato Jensen Filho
 * Wagner Takeshi Obara
 * 
 */
package smss;

/*
 * Classe que implementa a mensagem Dados
 */
public class Dados {

    private int tipo;
    private int codigoErro;
    private int tamanho;
    private byte[] dados;

    public Dados() {
        this.tipo = TiposMensagens.DADOS;
    }

    /*
     * Método que transforma uma dada mensagem de array de bytes para objeto do tipo Dados.
     * Deve ser informado a mensagem no parametro b.
     */
    void toDados(byte[] b) {
        //Recupera o primeiro byte de b em valor binário
        String s = Util.getBynaryString(b[0], 8);
        //os 4 digitos mais significativos corresponde ao tipo e os 4 menos significativos
        //corresponde ao código de erro.
        tipo = Integer.parseInt(s.substring(0, 4), 2);
        codigoErro = Integer.parseInt(s.substring(4, 8), 2);
        
        //Recupera e concatena o segundo e o terceiro byte de b em valores binários
        s = Util.getBynaryString(b[1], 8) + Util.getBynaryString(b[2], 8);
        //Esse valor representa o tamanho
        tamanho = Integer.parseInt(s, 2);
        
        //Recupera os dados
        dados = new byte[tamanho];
        System.arraycopy(b, 3, dados, 0, tamanho);
    }

    /*
     * Método que transforma Um objeto do tipo Dados para uma mensagem em array de bytes
     * Retorna um array de bytes.
     */
    public byte[] toByte() {
        byte[] byteRetorno = new byte[tamanho + 3];
        //Concatena o valor binario do tipo e do código de erro.
        String s = Util.getBynaryString(tipo, 4) + Util.getBynaryString(codigoErro, 4);
        //Insere na primeira posição do array.
        byteRetorno[0] = Byte.parseByte(s, 2);
        
        //Recupera o valor binario de tamanho
        s = Util.getBynaryString(tamanho, 16);
        //Insere na primeira e na segunda posição do array.
        byteRetorno[1] = Byte.parseByte(s.substring(0, 8), 2);
        byteRetorno[2] = Byte.parseByte(s.substring(8, 16), 2);
        
        //Insere os dados no array
        System.arraycopy(dados, 0, byteRetorno, 3, tamanho);

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

    public byte[] getDados() {
        return dados;
    }

    public void setDados(byte[] dados) {
        this.dados = dados;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Dados{" + "tipo=" + tipo + ", codigoErro=" + codigoErro + ", tamanho=" + tamanho + ", dados=" + dados + '}';
    }
}
