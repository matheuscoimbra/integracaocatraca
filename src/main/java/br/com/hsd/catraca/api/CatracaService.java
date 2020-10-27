package br.com.hsd.catraca.api;

import br.com.hsd.catraca.api.tcpcom.TcpClient;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CatracaService {

    private static Logger log = LoggerFactory.getLogger(CatracaService.class);

    StringBuilder retorno = new StringBuilder();

    private TcpClient client;


    @SneakyThrows
    public String sendMenssage(String request,String ip) {
        conecta(true,ip);
        jbtnEnviarActionPerformed( request);
        Thread.sleep(4000);
        return this.retorno.toString();
    }

    private void jbtnEnviarActionPerformed(String request) {//GEN-FIRST:event_jbtnEnviarActionPerformed
        StringBuilder jtaDadosEnv = new StringBuilder();
        boolean verNum = true;
            if (verNum){
                try {
                    char[] data;
                    data = request.toCharArray();
                    String str = "", aux = "";
                    str = textFormat(data);//formatando texto (cabeçalho, checksum e Byte final)
                    aux = stringHexFormat(str);//
                    var list = str.toCharArray();// formatando para numero Hexadecimal
                    client.sendData(list);   //enviando dados
                    jtaDadosEnv.append(str+"\n\n");

                } catch (Exception e) {
                }
            }

        }



    private void conecta(Boolean loop, String ip) {//GEN-FIRST:event_jchkConectarActionPerformed
        this.retorno = new StringBuilder();
        StringBuilder jtaDadosRec = new StringBuilder();
        StringBuilder jtaDadosBytesRec = new StringBuilder();
        client = new TcpClient(ip,
                Integer.parseInt("300"));
        client.connect();
        if (client.isConnected()){
            new Thread(() -> {
                String aux2="1";
                try {
                    while(loop) {

                        if (client.availableData() > 0) {
                            char[] temp = client.receiveData(client.
                                    availableData()); //recebendo dados
                            String str = "", aux = "";
                            for (char chr : temp) {
                                str += chr;
                            }
                            jtaDadosRec.append(str + "\n\n");

                            this.retorno.append(str);

                            aux = stringHexFormat(str);
                            jtaDadosBytesRec.append(aux + "\n\n");

                        }
                        Thread.sleep(500);  //esperando resposta

                    }
                } catch (Exception e) {

                }
                //System.out.println(aux2);
                log.info(aux2);
            }).start();
        }else{
            log.info("Conexão não Estabelecida");
            //jchkConectar.setSelected(false);
        }



    }

    public String textFormat (char[] data){
        String aux="", aux2 ="", str="";
        char BYTE_INIT, BYTE_END, BYTE_TAM[] = {0,0}, BYTE_CKSUM;
        BYTE_INIT = (char)Integer.valueOf("2", 16).intValue();//conf. bit inicial
        BYTE_END = (char)Integer.valueOf("3", 16).intValue();//conf. bit final
        BYTE_TAM[0] = (char)data.length;//conf. tamanho dos dados
        BYTE_TAM[1] = (char)Integer.valueOf("0", 16).intValue();
        aux2 += BYTE_INIT; //Inserindo byte inicial
        aux2 += BYTE_TAM[0]; //Inserindo byte do tamanho
        aux2 += BYTE_TAM[1];
        for (char chr : data) {
            str += chr;
        }
        aux = new String (aux2+str); // concatenando com a informação

        BYTE_CKSUM = aux.charAt(1);//Calculo do Checksum
        for (int a=2; a<aux.length();a++){
            BYTE_CKSUM = (char) (BYTE_CKSUM ^ aux.charAt(a));
        }
        aux += BYTE_CKSUM; //Inserindo Checksum
        aux += BYTE_END; //Inserindo byte Final
        return aux;

    }

    public String stringHexFormat(String str){
        String aux = "", temp = "";
        for (char ch : str.toCharArray()){
            temp += Integer.toHexString(ch).toUpperCase();
            //Converte Hexa em String
            if (temp.length()==1){
                aux += "0"+temp+" ";//se tiver 1 digito complementa com 0
            }
            else{
                aux+=temp+" ";
            }
            temp = new String ();
        }
        return aux;
    }// GEN-LAST:event_jbtnEnviarActionPerformed

}
