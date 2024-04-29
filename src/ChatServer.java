package chattingProject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServer {
    public static void main(String[] args) {
        Map<String,PrintWriter> userList = new HashMap<>(); // 사용자 리스트
        Map<String, Integer> userRoom = new HashMap<>(); // 사용자 방번호 매칭 리스트
        List<String> member= new ArrayList<>(); // 현재 같은 방에 있는 멤버
        ServerSocket server = null; // 서버 소켓
        try{
            // 서버 소켓 열기
            server = new ServerSocket(12345);
            // 다중 사용자 접속
            while(true){
                // 클라이언트 연결완료 -> 클라이언트 소켓 반환
                Socket client = server.accept();
                // 채팅과 관련된 개별 멀티 스레드 실행(클라이언트 소켓, 사용자들 정보)
                new chatTread(client,userList,userRoom,member,1).start();
            }
        }catch(Exception e){
            e.printStackTrace();

        }finally {
            // 서버 소켓 닫기
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
