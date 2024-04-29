package chattingProject;


import java.io.PrintWriter;
import java.util.*;

public class ChatFunctional {
    static Set<Integer> roomlist = new HashSet<>(); // 방 목록(중복x)
    static List<Integer> room = new ArrayList<>(); // 현재 활성화되어 있는 방 목록(중복0)

    // 룸 초기화
    public static void ActivityRoom(Map<String,Integer> userRoom){
        roomlist.add(1);
        Iterator<Integer> it = userRoom.values().iterator();
        // 방번호 중복을 없에기 위해 Set인터페이스에 저장
        while (it.hasNext()) {
            int roomNumber = it.next();
            roomlist.add(roomNumber);
            room.add(roomNumber);
        }
    }
    // 기능 1. 방 목록 보기
    public static void list(Map<String,Integer> userRoom, PrintWriter out){
        out.println("생성된 모든 방의 목록을 출력합니다.");
        // 방 목록 출력
        for(int temp : roomlist){
            out.println(temp + "번 방");
        }
        out.println("-----------------------------");
    }

    // 기능 2. 대화방 생성
    public static void create(Map<String,Integer> userRoom, String name , int roomNumber, PrintWriter out){
        out.println("방 번호 [ " + roomNumber + " ]가 생성되었습니다.");
        out.println(roomNumber + "번 방으로 이동합니다.");
        userRoom.put(name,roomNumber); // 해당 유저의 배정된 방 번호 변경
        out.println("현재 접속중인 방 번호는 " + roomNumber+ "번입니다.");
        out.println("-----------------------------");
        roomlist.add(roomNumber);
        room.add(roomNumber);
    }
    // 기능 3. 방 입장
    public static void join(Map<String,Integer> userRoom, String name , int roomNumber){
        userRoom.put(name,roomNumber);
    }
    // 기능 4. 방 나가기
    public static boolean exit(int roomNumber){
        room.remove((Integer) roomNumber);
        // 해당 방에 사용자가 없다면 해당 방 없에기
        if(!room.contains(roomNumber)){
            roomlist.remove(roomNumber);
            return false;
        }
        //out.println("-----------------------------");
        return true;
    }
    // 기능 5. 현재 접속중인 모든 사용자 목록 출력
    public static void users(Map<String, PrintWriter> userList, PrintWriter out){
        Iterator<String> member = userList.keySet().iterator();
        while(member.hasNext()){
            out.println(member.next());
        }
    }
    // 기능 6. 현재 방에 있는 모든 사용자 목록 출력
    public static void roomUsers(List<String> member,PrintWriter out){
        for(String temp : member){
            out.println(temp);
        }
    }

}
