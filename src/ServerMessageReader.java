package chattingProject;

import java.io.BufferedReader;
import java.io.IOException;
// 추가한 코드
/*

 */
public class ServerMessageReader implements Runnable {
    private BufferedReader in;

    public ServerMessageReader(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String serverLine;
            while ((serverLine = in.readLine()) != null) {

                    System.out.println(serverLine); // 서버로부터 받은 메시지를 출력


            }
        } catch (IOException e) {
            System.out.println("Server connection was closed.");
        }
    }
}


