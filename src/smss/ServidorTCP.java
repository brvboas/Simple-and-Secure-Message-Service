/**
 * ServidorTCP.java
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

/*
 * Classe que implementa o servidor
 */
public class ServidorTCP {
    
    //Chave Padrão
    private  byte[] key = {63, 68, 61, 76, 65, 20, 31, 20, 64, 65, 20, 74, 65, 73, 74, 65}; 

    public ServidorTCP() throws Exception {
        ServerSocket serverSocket = null;
        //Cria uma conexao na porta TCP 50000 
        try {
            serverSocket = new ServerSocket(50000);
        } catch (IOException e) {
            System.err.println("Não é possível escutar na porta 50000.");
            System.exit(1);
        }

        Socket clientSocket = null;
        while (true) {
            try {
                System.out.println("Esperando por Cliente");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Falha");
                System.exit(1);
            }
            ObjectOutputStream out = new ObjectOutputStream(
                    clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(
                    clientSocket.getInputStream());
            
            
            byte[] b = new byte[7];
            //Cria mensagem Par_req
            Par_req par_req = new Par_req();
            try {
                //Recebe mensagem Par_req
                in.read(b);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            par_req.toPar_req(b);
            //System.out.println( par_req );
            
            //Cria mensagem Par_conf
            Par_conf parConfig = new Par_conf();
            int tipoErro=verificaPar_req(par_req);
            parConfig.setCodigoErro(tipoErro);
            
            //gera um iv aleatório
            byte[] iv = Util.geraIV(); 
            if (tipoErro==Erro.OK){ //verifica se os parametros de criptografia sao suportados
                parConfig.setIv(iv);
            }else{
                parConfig.setIv(null);
            }
            b = parConfig.toByte();
            
            //Envia mensagem Par_conf
            try{
                out.write(b);
                out.flush();
            } catch(SocketException se){
                System.out.println("A conexão foi fechada pelo cliente, Reiniciando o Servidor");
            }

            b = new byte[1443];
            //Cria mensagem Dados
            Dados dados = new Dados();
            try {
                //Recebe mensagem Dados
                in.read(b);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            dados.toDados(b);
            //System.out.println( dados );
            
            //Decifra a mensagem recebida
            byte[] msgEnc = dados.getDados();
            AES algoritmoAES = new AES();
            algoritmoAES.setIv(iv);
            byte[] msg;
            if (par_req.getOrigem()==0 && par_req.getDestino()==65535) //se a origem e o destino forem os valores padrão, então é utilizado a chave padrão para decifrar
                msg = algoritmoAES.decode(msgEnc,key);
            else{ //caso contrário, gera uma nova chave de acordo com a origem e destino
                byte[] chave = Util.hash( Integer.toString(par_req.getOrigem()) + Integer.toString(par_req.getDestino())); 
                msg = algoritmoAES.decode(msgEnc,chave);
            }
            String mensagemDecifrada = new String(msg).trim();
            System.out.println("Mensagem decifrada: "+mensagemDecifrada);
            
            //Cria mensagem Conf
            Conf conf = new Conf();
            b = new byte[1];
            tipoErro = verificaDados(dados);
            conf.setCodigoErro(tipoErro);
            if (mensagemDecifrada.equals(""))
                conf.setCodigoErro(Erro.DADOS);
            b = conf.toByte();
            //Envia mensagem Conf
            try {
                out.write(b);
                out.flush();
            } catch(SocketException se){
                System.out.println("A conexão foi fechada pelo cliente, Reiniciando o Servidor");
            }
  
        }
        //in.close();
        //out.close();
        //clientSocket.close();
        //serverSocket.close();
    }

    private int verificaPar_req(Par_req rcvSolicitacao) {
        if (rcvSolicitacao.getTipo()!=TiposMensagens.PAR_REQ )
            return Erro.TIPO;
        if (rcvSolicitacao.getAlgoritmo()!=Algoritmos.AES128 && 
                rcvSolicitacao.getPadding()!=Paddings.NOPADDING && 
                rcvSolicitacao.getModo()!=ModoOperacao.CFB128)
            return Erro.PARAMETROS;
        return Erro.OK;
    }

    private int verificaDados(Dados dados) {
        if (dados.getTipo()!=TiposMensagens.DADOS)
            return Erro.TIPO;
        if (dados.getTamanho()>1440)
            return Erro.PARAMETROS;
        
        return Erro.OK;
    }

 
}
