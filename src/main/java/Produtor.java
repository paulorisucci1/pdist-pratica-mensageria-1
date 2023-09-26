import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Produtor {

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("mqadmin");
        connectionFactory.setPassword("Admin123XX_");
        try (
                Connection connection = connectionFactory.newConnection();
                Channel canal = connection.createChannel();
        ) {
            String mensagem = "Paulo Elias Risucci da Silva ...";
            String NOME_FILA = "fila_de_tarefas";

            //(queue, passive, durable, exclusive, autoDelete, arguments)
            boolean duravel = true;
            canal.queueDeclare(NOME_FILA, duravel, false, false, null);

            // ​(exchange, routingKey, mandatory, immediate, props, byte[] body)
            canal.basicPublish(
                    "",
                    NOME_FILA,
                    false,
                    false,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    mensagem.getBytes());

        }
    }
}


