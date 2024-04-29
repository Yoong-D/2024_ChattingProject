# 채팅 만들기 프로젝트

## ➡️ 사용 설명서  
- 클라이언트는 12345 포트로 대기 중인 `ChatServer`에 접속합니다.
  
- 서버에 접속하면, 사용자는 닉네임을 입력받아 서버에 전송합니다.
  
- 서버는 사용자의 닉네임을 받고 "OOO 닉네임의 사용자가 연결했습니다."라고 출력합니다.
- 클라이언트가 접속하면 서버는 사용자의 IP 주소를 출력합니다.
- 
  ![image](https://github.com/Yoong-D/ChattingProject/assets/52689951/4cf6a264-812f-4d32-952c-56edd5a2828f)

- 클라이언트는 닉네임을 입력한 후부터 서버로부터 메시지를 한 줄씩 받아 화면에 출력합니다.
  
- 사용자가 메시지를 입력하면 서버에 전송합니다.
  
- 모든 메시지는 같은 방에 접속한 멤버만 공개합니다.
  
- 서버는 중복되지 않는 닉네임만 허용합니다. 이미 사용 중인 닉네임을 입력하면 경고 메시지를 보내고 다른 닉네임을 요구합니다.
  
  ![image](https://github.com/Yoong-D/ChattingProject/assets/52689951/0a3e1373-5a2c-43f6-b83a-23f064247836)  
 ![image](https://github.com/Yoong-D/ChattingProject/assets/52689951/b7727e64-2eb6-40d7-9272-5fa25a5384d8)




## 💡명령어 모음 
- 서버는 클라이언트가 접속하면 아래 명령어들을 클라이언트에게 전송합니다.  
    ![image](https://github.com/Yoong-D/ChattingProject/assets/52689951/e60e5f67-3046-4549-9a6e-10a0d8b50fb9)
    ###  list
    - "/list" 명령을 입력하면 서버는 생성된 모든 방의 목록을 출력합니다.  
      ![image](https://github.com/Yoong-D/ChattingProject/assets/52689951/60c7e5bb-f284-459d-9a1f-f7c8a236c47c)

    ### create
    - 클라이언트가 "/create"를 입력하면 서버는 새 방을 생성하고 클라이언트를 그 방으로 이동시킵니다.
    - 방은 1부터 시작하는 번호로 관리되며, 생성 시 "방 번호 [방번호]가 생성되었습니다."를 출력합니다.  
      ![image](https://github.com/Yoong-D/ChattingProject/assets/52689951/50b09c75-8b26-46f0-9fde-a1179ebd2169)

    ### join [방번호]
    - "/join [방번호]"를 통해 특정 방에 입장할 수 있습니다. 방에 입장하면, "닉네임이 방에 입장했습니다." 메시지를 전달합니다.  
      ![image](https://github.com/Yoong-D/ChattingProject/assets/52689951/accb75a5-1174-40b4-9c0e-83298003d4a0)  ![image](https://github.com/Yoong-D/ChattingProject/assets/52689951/d9ff54dd-f89a-4f47-85a2-8ea993f8d4b1)


    ### exit
    - 방에서 "/exit"를 입력하면, "닉네임이 방을 나갔습니다." 메시지와 함께 로비로 이동합니다. 방에 아무도 남지 않으면 해당 방을 삭제하고 "방 번호 [방번호]가 삭제되었습니다."를 출력합니다.  
      ![image](https://github.com/Yoong-D/ChattingProject/assets/52689951/f0d846df-5d4e-40c3-8d98-cddfd4999443)

    ### bye
    - 사용자가 "/bye"를 입력하면 연결을 종료하고 프로그램을 종료합니다. 서버도 "OOO 닉네임의 사용자가 연결을 끊었습니다."를 출력하고 연결을 종료합니다.  
      ![image](https://github.com/Yoong-D/ChattingProject/assets/52689951/ff1464c3-e879-41e1-820b-8aef7d843445)

    ### users
    - "/users" 명령으로 현재 접속 중인 모든 사용자의 목록을 볼 수 있습니다.  
      ![image](https://github.com/Yoong-D/ChattingProject/assets/52689951/a3d07487-c82c-4d43-8b5c-73effc8f3f65)

    ### room users
    - "/roomusers" 명령으로 현재 방에 있는 모든 사용자의 목록을 확인할 수 있습니다.  
      ![image](https://github.com/Yoong-D/ChattingProject/assets/52689951/b0da875b-39f8-46a7-b655-039e9041d79d)

    ### wipsepr [닉네임] [메시지]
    - "/whisper [닉네임] [메시지]" 명령을 사용하여 특정 사용자에게만 메시지를 전송할 수 있습니다. 방 내에서도 같은 명령을 사용하여 특정 닉네임에게만 메시지를 보낼 수 있습니다.  
      ![image](https://github.com/Yoong-D/ChattingProject/assets/52689951/7842fc90-df3a-4568-88c6-870f988c6886) ![image](https://github.com/Yoong-D/ChattingProject/assets/52689951/e7c818af-cc03-495f-b627-04b9e823b9c0)


    ### download
    - 유저 간 채팅에 작성한 내용들을 파일로 만들어 채팅 내역을 확인할 수 있게 만듭니다.
    - 다운로드 경로에 "chatHistory월일" 파일명으로 다운로드됩니다.
    - 해당 파일 명이 이미 존재할 경우 (숫자)를 붙여 다운로드됩니다.  
     ![image](https://github.com/Yoong-D/ChattingProject/assets/52689951/d1e5a510-f697-4ba7-b2d9-4c2bbefd7a16)
     ![image](https://github.com/Yoong-D/ChattingProject/assets/52689951/b9cf81e6-5c53-43ee-b641-2c9776d4bd82)  









