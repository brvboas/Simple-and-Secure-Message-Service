/**
 * Par_req.java
 * 
 * @authors 
 * Bruno Villas Boas da Costa
 * Renato Jensen Filho
 * Wagner Takeshi Obara
 * 
 */
package smss;

import java.nio.ByteBuffer;

/*
 * Classe que implementa a mensagem Par_req
 */
public class Par_req  {
    
    private int tipo;
    private int reservado;
    private int origem;
    private int destino;
    private int algoritmo;
    private int padding;
    private int modo;

    public Par_req() {
        //Par_req padrão
        this.tipo=TiposMensagens.PAR_REQ;
        this.reservado=0;
        this.origem=0;
        this.destino=65535; //65535
        this.algoritmo=Algoritmos.AES128;
        this.padding=Paddings.NOPADDING;
        this.modo=ModoOperacao.CFB128;
    }
    
    /*
     * Método que transforma uma dada mensagem de array de bytes para objeto do tipo Par_req.
     * Deve ser informado a mensagem no parametro b.
     */
    void toPar_req(byte[] b) {
        ByteBuffer bf = ByteBuffer.wrap(b);
        //Recupera o primeiro byte de b em valor binário
        String s =  Util.getBynaryString(b[0],8) ;
        //os 4 digitos mais significativos corresponde ao tipo e os 4 menos significativos
        //corresponde ao reservado.
        tipo = Integer.parseInt(s.substring(0,4) , 2);
        reservado = Integer.parseInt(s.substring(4,8) , 2);
        
        //Recupera a origem, da segunda posição do array
        short val = bf.getShort(1);
        origem = (int) val;
        if (origem<0)
            origem = 65536+origem;
        
        //Recupera o destino, da quarta posição do array
        val = bf.getShort(3);
        destino = (int) val;
        if (destino<0)
            destino = 65536+destino;
                
        //Recupera o sexto byte de b em valor binário
        s =  Util.getBynaryString(b[5],8) ;
        //os 4 digitos mais significativos corresponde ao algoritmo e os 4 menos significativos
        //corresponde ao padding.
        algoritmo = Integer.parseInt(s.substring(0,4) , 2);
        padding = Integer.parseInt(s.substring(4,8) , 2);
        
        //Recupera o sétimo byte de b em valor binário
        s =  Util.getBynaryString(b[6],8);
        //O valor corresponde ao modo.
        modo = Integer.parseInt(s , 2);
    }
    
    /*
     * Método que transforma Um objeto do tipo Par_req para uma mensagem em array de bytes
     * Retorna um array de bytes.
     */
    public byte[] toByte(){
        byte [] byteRetorno = new byte[7];
        ByteBuffer bf = ByteBuffer.wrap(byteRetorno);
        
        //Concatena o valor binario do tipo e do reservado.
        String s = Util.getBynaryString(tipo,4) + Util.getBynaryString(reservado,4);
        //e insere na primeira posição do array.
        byteRetorno[0]=Byte.parseByte(s, 2);
        
        //Insere a origem na segunda posição do array.
        short val = (short)origem;
        bf.putShort(1, val);
        
        //Insere o destino na quarta posição do array.
        val = (short)destino;
        bf.putShort(3, val); 
        
        //Concatena o valor binario do algoritmo e do padding.
        s = Util.getBynaryString(algoritmo, 4) + Util.getBynaryString(padding,4);
        //e insere na sexta posição do array.
        byteRetorno[5]=Byte.parseByte(s, 2);
        
        //Revupera o valor binário de modo
        s = Util.getBynaryString(modo, 8);
        //e insere na sétima posição do array.
        byteRetorno[6]=Byte.parseByte(s, 2);
        
        
        
        return byteRetorno;
    }
    
    /*
     * Métodos geters e seters
     */
    public int getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(int algoritmo) {
        this.algoritmo = algoritmo;
    }

    public int getDestino() {
        return destino;
    }

    public void setDestino(int destino) {
        this.destino = destino;
    }

    public int getModo() {
        return modo;
    }

    public void setModo(int modo) {
        this.modo = modo;
    }

    public int getOrigem() {
        return origem;
    }

    public void setOrigem(int origem) {
        this.origem = origem;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public int getReservado() {
        return reservado;
    }

    public void setReservado(int reservado) {
        this.reservado = reservado;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Par_req{" + "tipo=" + tipo + ", reservado=" + reservado + ", origem=" + origem + ", destino=" + destino + ", algoritmo=" + algoritmo + ", padding=" + padding + ", modo=" + modo + '}';
    }

    

    
    
    
}
