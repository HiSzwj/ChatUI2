package com.source.android.chatsocket.messages;

public class ChatActivityStatusMessage {
    private boolean status;

    public ChatActivityStatusMessage(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
