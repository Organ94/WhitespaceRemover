import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {

    protected static final String HOST = "localhost";
    protected static final int PORT = 8080;

    public static void main(String[] args) throws IOException {

        final ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(HOST, PORT));

        while (true) {
            try (SocketChannel socketChannel = server.accept()) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

                while (socketChannel.isConnected()) {
                    int byteCount = socketChannel.read(inputBuffer);

                    if (byteCount == -1) break;

                    final String msg = new String(inputBuffer.array(), 0, byteCount,
                            StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    System.out.println("Получено сообщение от клиента: " + msg);

                    socketChannel.write(ByteBuffer.wrap((whitespaceRemover(msg)).getBytes(StandardCharsets.UTF_8)));
                }
            }
        }
    }

    private static String whitespaceRemover(String msg) {
        return msg.replaceAll("\\s+", " ");
    }
}
