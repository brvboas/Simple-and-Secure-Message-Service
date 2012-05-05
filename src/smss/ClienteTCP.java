/**
 * ClienteTCP.java
 * 
 * @authors 
 * Bruno Villas Boas da Costa
 * Renato Jensen Filho
 * Wagner Takeshi Obara
 * 
 */
package smss;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/*
 * Classe que implementa o cliente
 */
public class ClienteTCP {
    
    //declaração da chave padrão
    private  byte[] key = {63, 68, 61, 76, 65, 20, 31, 20, 64, 65, 20, 74, 65, 73, 74, 65}; 
    
    public ClienteTCP() throws Exception {
        Socket echoSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        
        //abre uma nova conexao
        try {
            echoSocket = new Socket("localhost", 50000);
            out = new ObjectOutputStream(echoSocket.getOutputStream());
            in = new ObjectInputStream(echoSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Não foi possível conectar ao Servidor");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Não foi possivel acessar o Servidor.");
            System.exit(1);
        }
        
        //Aguarda pela obtencao dos dados (origem e destino), pela entrada padrao
        Scanner leitor = new Scanner(System.in);
        
        System.out.println("Entre com a origem (Pressione <[Enter]> para padrão): ");
        String msg = leitor.nextLine();
        int origem=0,destino=65535;
        try {
            origem = Integer.parseInt(msg);
        }catch(NumberFormatException e){
            System.out.println("O valor da origem foi ajustado para 0 (Padrão)");
        }
        
        System.out.println("Entre com o destino (Pressione <[Enter]> para padrão): ");
        msg = leitor.nextLine();
        try {
            destino = Integer.parseInt(msg);
        }catch(NumberFormatException e){
            System.out.println("O valor do destino foi ajustado para 65535 (Padrão)");
        }
        
        //Cria a mensagem Par_req
        Par_req solicitacao = new Par_req();
        solicitacao.setDestino(destino);
        solicitacao.setOrigem(origem);
        
        byte[] b = solicitacao.toByte();
        
        //envia a mensagem Par_req para o servidor
        try{
            out.write(b);
            out.flush();
        } catch(SocketException se){
            System.out.println("A conexão foi fechada");
        }
        //Cria a mensagem Par_conf
        Par_conf parConfig = new Par_conf();
        b = new byte[17];
        try {   
        //Recebe a mensagem Par_conf do servidor
            in.read(b);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        parConfig.toPar_conf(b);
        //System.out.println(parConfig);
        
        //Cria a mensagem Dados
        Dados dados = new Dados();
        int tipoErro=verificaParConfig(parConfig);
        dados.setCodigoErro(tipoErro);
        
        //usuário digita a mensagem a ser cifrada
        System.out.println("Entre com a mensagem a ser cifrada: ");
        msg = leitor.nextLine();
        
        //Algoritmo AES cifra a mensagem digitada
        if (tipoErro==Erro.OK){ 
            AES algoritmoAES = new AES();
            algoritmoAES.setIv(parConfig.getIv());
            byte[] enc; 
            if (origem==0 && destino==65535) //se a origem e o destino forem os valores padrão, então é utilizado a chave padrão para cifrar
                enc=algoritmoAES.encode(AES.nullPadString(msg).getBytes(), key);
            else{ //caso contrário, gera uma nova chave de acordo com a origem e destino
                byte[] chave = Util.hash( Integer.toString(origem) + Integer.toString(destino)); 
                enc=algoritmoAES.encode(AES.nullPadString(msg).getBytes(), chave);
            }
            
            String msgParaEnviar = Util.fromHex(enc);
            System.out.println("Mensagem cifrada: "+msgParaEnviar);
            if (enc.length<=1440){
                dados.setTamanho(enc.length);
                dados.setDados(enc);
            }else{
                System.out.println("Mensagem muito grande, a conexão será fechada");
                System.exit(1);
            }
        }else {
            System.out.println("Erro ao receber Par_conf, a conexão será fechada");
            System.exit(1);
        }
        
        b = new byte[dados.getTamanho()];
        b = dados.toByte();
        
        //envia a mensagem Dados para o servidor
        try{
            out.write(b);
            out.flush();
        } catch(SocketException se){
            System.out.println("A conexão foi fechada");
        }
        
        //Cria a mensagem Conf
        Conf conf = new Conf();
        b = new byte[1];
        try {
            //recebe a mensagem Conf do servidor
            in.read(b);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        conf.toDados(b);
        if (verificaConf(conf)!=Erro.OK){
            System.out.println("Erro ao receber Conf, a conexão será fechada");
            System.exit(1);
        }else{
            System.out.println("Mensagem recebida com sucesso pelo servidor.");
        }
        
        
        //Fecha a Conexao
        out.close();
        in.close();
        echoSocket.close();
        
        
      
    }

    //funcao que verifica os dados recebidos no ParConfig estao corretos
    private int verificaParConfig(Par_conf parConfig) {
        if (parConfig.getTipo()!=TiposMensagens.PAR_CONF)
           return Erro.TIPO;
        if (parConfig.getIv()==null)
            return Erro.IVNULO;
        return Erro.OK;
    }
    
    //funcao que verifica os dados recebidos no Conf estao corretos
    private int verificaConf(Conf conf) {
        if (conf.getTipo()!=TiposMensagens.CONF)
            return  Erro.TIPO;
        if (conf.getCodigoErro()!=Erro.OK)
            return Erro.INTERNO;
        return Erro.OK;
    }

}
