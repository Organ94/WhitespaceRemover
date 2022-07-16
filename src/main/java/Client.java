import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {

        InetSocketAddress socketAddress = new InetSocketAddress(Server.HOST, Server.PORT);
        final SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(socketAddress);

        try (Scanner scanner = new Scanner(System.in)) {
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

            String msg;
            while (true) {
                System.out.println("Введите сообщение для сервера...");
                msg = scanner.nextLine();

                if (msg.equals("end")) break;

                socketChannel.write(ByteBuffer.wrap(
                        msg.getBytes(StandardCharsets.UTF_8)));
                Thread.sleep(3000);
                int byteCount = socketChannel.read(inputBuffer);
                System.out.println(new String(inputBuffer.array(), 0, byteCount,
                        StandardCharsets.UTF_8));
                inputBuffer.clear();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            socketChannel.close();
        }
    }
}
