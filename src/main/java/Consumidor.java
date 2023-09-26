import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;


public class Consumidor {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("mqadmin");
        connectionFactory.setPassword("Admin123XX_");

        Connection conexao = connectionFactory.newConnection();
        Channel canal = conexao.createChannel();
        int prefetchCount = 1;
        canal.basicQos(prefetchCount);

        String NOME_FILA = "fila_de_tarefas"
                + "";
        boolean duravel = true;
        canal.queueDeclare(NOME_FILA, duravel, false, false, null);

        DeliverCallback callback = (consumerTag, delivery) -> {
            String mensagem = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Eu " + consumerTag + " Recebi: " + mensagem);
            try {
                doWork(mensagem);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            } finally {
                System.out.println("[x] Feito");
                canal.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        // fila, noAck, callback, callback em caso de cancelamento (por exemplo, a fila foi deletada)
        boolean autoAck = false;
        canal.basicConsume(NOME_FILA, autoAck, callback, consumerTag -> {
            System.out.println("Cancelaram a fila: " + NOME_FILA);
        });
    }

    private static void doWork(String task) throws InterruptedException {
        for(char ch: task.toCharArray()) {
            if(ch == '.') {
                Thread.sleep(1000);
            }
        }
    }
}


