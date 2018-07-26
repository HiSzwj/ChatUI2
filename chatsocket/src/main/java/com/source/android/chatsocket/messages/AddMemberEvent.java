package com.source.android.chatsocket.messages;

/**
 * Created by zzw on 2018/4/19.
 */

public class AddMemberEvent {
    /*  {
          id:  ‘inviteJoinRoom’,
          roomId:  ‘fdlksjfldsjf;dsfdks;jfd’,
          userId:  ‘fdsljflkjdslfkjdsljfds’
      }*/
    public AddMemberEvent(String type,String roomId,String userId){
        this.id=type;
        this.roomId=roomId;
        this.userId=userId;
    }
    private String id;
    private String roomId;
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
