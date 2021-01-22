package concurrency;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CloseResource {
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(8080);
        InputStream inputStream = new Socket("localhost", 8080).getInputStream();
        executorService.execute(new IOBlocked(inputStream));
        executorService.execute(new IOBlocked(System.in));

        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("Shutting down all threads");
        executorService.shutdownNow();

        TimeUnit.SECONDS.sleep(1);
        System.out.println("Closing " + serverSocket.getClass().getName());
        serverSocket.close();

        TimeUnit.SECONDS.sleep(1);
        System.out.println("Closing " + System.in.getClass().getName());
        System.in.close();
    }
}
