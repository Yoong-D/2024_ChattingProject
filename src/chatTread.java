package chattingProject;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class chatTread extends Thread {
    private Socket client; // 사용자 소켓
    private String name; // 사용자 닉네임
    private int roomNumber; // 사용자가  속한 방 번호
    private Map<String, Integer> userRoom; // 클라이언트 리스트를 키로, 해당 방 번호를 값으로 설정하는 이중 맵
    private Map<String, PrintWriter> userList; // 클라이언트 리스트 <내부 맵>
    private BufferedReader in; // 사용자 출력값 읽어오는 통로
    private PrintWriter out; // 값을 써서 클라이언트에게 보낼 통로
    private InetAddress ip; // 사용자 ip
    private List<String> member; // 같은 방에 속한 멤버 이름 리스트
    private List<String> chatHistory = new ArrayList<>();// 채팅 내역 저장 리스트


    // 생성자 - 초기화 담당
    public chatTread(Socket client, Map<String, PrintWriter> userList, Map<String, Integer> userRoom, List<String> member,List<String> chatHistory, int roomNumber) {
        this.client = client;
        this.userList = userList;
        this.roomNumber = roomNumber;
        this.userRoom = userRoom;
        this.member = member;
        this.chatHistory = chatHistory;
        // 클라이언트 생성시 클라이언트로부터 id 얻기
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // 생성자


    // 멀티 스레드 동작
    @Override
    public void run() {
        while(nickName()){
            out.println("해당 닉네임은 이미 사용중입니다. 닉네임을 다시 입력해주세요.");
        }
        System.out.println("[ " + name + " ] 닉네임의 사용자가 연결했습니다.");
        // 사용자 소켓을 이용하여 ip 받아오기
        ip = client.getInetAddress();
        System.out.println( name +" 사용자 IP 주소 : " + ip.getHostAddress());

        // 클라이언트가 접속시 "명령어 모음" 보내기
        out.println("\u001B[33m" +
                "+--------------------------------+\n" +
                "| 방 목록 보기 : /list              |\n" +
                "| 방 생성 : /create                |\n" +
                "| 방 입장 : /join [방번호]          |\n" +
                "| 방 나가기 : /exit                |\n" +
                "| 접속 종료 : /bye                 |\n" +
                "| 로그인 접속자 보기 : /users       |\n" +
                "| 현재 방 접속자 보기 : /room users |\n" +
                "| 귓속말 : /wisper [닉네임] [메시지]|\n" +
                "| 채팅 내역 다운로드 : /download    |\n" +
                "+--------------------------------+\u001B[0m"); // 텍스트 색상 노란색으로 변경
        // 처음 접속시 방 번호 1번에 배정된다.
        out.println("현재 접속중인 방 번호는 1번입니다.");

        // map에 해당 사용자 정보 + 방 번호  넣기   -> 동기화 작업
        synchronized (userList) {
            userList.put(name, out); // 사용자 명, 출력 통로
        }
        synchronized (userRoom) {
            userRoom.put(name, roomNumber); // 방 번호
        }
        String msg = null;
        try {
            // 클라이언트에서 온 데이터 읽기
            ChatFunctional.ActivityRoom(userRoom);
            while ((msg = in.readLine()) != null) {
                // /list  /create /join [방번호] /exit /bye
                switch (msg) {
                    case "/bye":
                        sameRoomUser();
                        broadcast("\u001B[31m[ " + name + " ] 닉네임의 사용자가 연결을 끊었습니다.\u001B[0m");
                        break;
                    case "/list":
                        ChatFunctional.list(userRoom, out);
                        break;
                    case "/create":
                        roomNumber++; // 방 번호 증가
                        ChatFunctional.create(userRoom, name, roomNumber, out); // 이름-방번호, 사용자 이름, 생성될 방번호, 출력통로
                        break;
                    // '/join'가 포함된 경우 처리하는 코드(자바 12이후 지원 패턴 매칭)
                    case String s when s.contains("/join"):
                        // 방 번호 찾기
                        String temp = msg.substring(6, msg.length());
                        roomNumber = Integer.parseInt(temp);
                        ChatFunctional.join(userRoom, name, roomNumber);
                        broadcast(name + "님이 방에 입장했습니다.");
                        break;
                    case "/exit":
                        boolean empty = ChatFunctional.exit(roomNumber);
                        if (!empty) {
                            out.println(roomNumber + "번 방이 삭제되었습니다");
                        }else{
                            broadcast("[ " + name + " ] 님이 방을 나갔습니다.");
                        }
                        roomNumber = 0; // 로비(구현 x)
                        sleep(1000);
                        out.println("\u001B[33m" +
                                "+--------------------------------+\n" +
                                "| 방 목록 보기 : /list             |\n" +
                                "| 방 생성 : /create               |\n" +
                                "| 방 입장 : /join [방번호]         |\n" +
                                "| 방 나가기 : /exit                |\n" +
                                "| 접속 종료 : /bye                 |\n" +
                                "| 로그인 접속자 보기 : /users       |\n" +
                                "| 현재 방 접속자 보기 : /room users |\n" +
                                "| 귓속말 : /wisper [닉네임] [메시지]|\n" +
                                "| 채팅 내역 다운로드 : /download    |\n" +
                                "+--------------------------------+\u001B[0m"); // 텍스트 색상 노란색으로 변경
                        break;
                    case "/users":
                        ChatFunctional.users(userList, out);
                        break;
                    case "/room users":
                        sameRoomUser();
                        ChatFunctional.roomUsers(member, out);
                        break;
                    case String s when s.contains("/wisper"):
                        String t = msg.substring(7, msg.length()).trim();
                        int cut = t.indexOf(" ");
                        String wisperName = t.substring(0,cut); // 닉네임
                        String priavteMessage = t.substring(cut+1,t.length()); // 메시지
                        out.println("\u001B[90m [ " + wisperName + " ] 님에게 귓속말 보내기 : " + priavteMessage + "\u001B[0m"); // 텍스트 색상 회색으로 변경
                        SecretTalk(wisperName,priavteMessage);
                        break;
                    case "/download" :
                        out.println("현재 채팅방의 대화 내역을 다운로드합니다.");
                        chatDownload();
                        break;
                    default:
                        broadcast(name + " : " + msg);
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("[ " + name + " ] 닉네임의 사용자가 연결을 끊었습니다..");
        } finally {
            synchronized (userList) {
                userList.remove(name);
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    // 닉네임 출력 + 중복 확인
    public boolean nickName(){
        boolean same = false;
        try{
            name = in.readLine();
            if(userList.containsKey(name)){
                same =  true;
            }else{
                out.println("닉네임 설정이 완료되었습니다.");

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return same;
    }

    // 같은 방에 있는 멤버 추려내기
    public void sameRoomUser() {
        synchronized (userRoom) {
            member.clear();
            Iterator<String> it = userRoom.keySet().iterator();
            while (it.hasNext()) {
                String temp = it.next();
                if ((userRoom.get(temp) == roomNumber) && !member.contains(temp)) {
                    member.add(temp);
                }
            }
        }
    }

    // 같은 방 번호에 접속한 사람들에게만 모든 메시지를 전송
    public void broadcast(String str) {
        sameRoomUser();
        synchronized (userList) {
            for (String temp : member) {
                PrintWriter broad = userList.get(temp);
                try {
                    broad.println(str);
                    chatHistory.add(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
    // 귓속말 보내기 기능 구현
    public void SecretTalk(String name, String str) {
        synchronized (userList) {
           PrintWriter broad = userList.get(name);
            try {
                broad.println("\u001B[32m [ " + name + " ] 님에게 온 귓속말 : " + str + "\u001B[0m"); //텍스트 색상 초록색으로 변경
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // 채팅 내역 다운로드 기능 구현
    public void chatDownload() {
        Calendar calendar = Calendar.getInstance(); // 현재 로컬 캘린더 객체
        // 월은 0부터 시작하므로 1을 더해줌
        int monthValue = calendar.get(Calendar.MONTH) + 1; // 월 값에 1을 더한 후 변수에 저장
        String month = "_" + monthValue; // 문자열로 변환
        if (monthValue < 10) { // 한 자리 수인 경우 앞에 0을 추가
            month = "_0" + monthValue;
        }
        // 현재 날짜 중 일 가져오기
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 사용자의 다운로드 경로 가져오기
        String downloadPath = System.getProperty("user.home") + File.separator + "Downloads";

        // 다운로드할 파일의 이름
        String fileName = "chatHistory" + month + day + ".txt";
        // 지정된 경로에 파일 추가
        File downloadFile = new File(downloadPath, fileName);

        // 파일이 이미 해당 경로에 존재시 처리
        int count = 1;
        while (downloadFile.exists()) {
            fileName = "chatHistory" + month + day + "(" + count + ").txt";
            downloadFile = new File(downloadPath, fileName);
            count++;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(downloadFile))) {
            for(String temp : chatHistory){
                writer.write(temp);
                writer.newLine();
            }
            out.println("다운로드 경로에 " +fileName + " 파일 명으로 저장 되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}