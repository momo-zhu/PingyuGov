/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kymjs.chat.bean;

import org.kymjs.kjframe.database.annotate.Transient;

/**
 * 聊天消息javabean
 *
 * @author kymjs (http://www.kymjs.com/)
 */
public class MessageChat {
    @Transient
    public final static int MSG_STATE_SENDING = 3;
    @Transient
    public final static int MSG_STATE_SUCCESS = 1;
    @Transient
    public final static int MSG_STATE_FAIL = 2;

    private int id;
    private int type; // 0-text | 1-photo | 2-face | more type ...
    private int state; // 0-sending | 1-success | 2-fail

    private long mediaLength;
    private String fromUserName;
//    private String fromUserAvatar;
    private String toUserName;
//    private String toUserAvatar;
    private String content;
    private String roomKey;
    private String mediaName;
    private String mediaId;


    private boolean isSend;
    private boolean sendSucces;
    private long time;
    private boolean hasRead;
    private int messageId;
    private String queueId;

    public MessageChat(int type, int state, String fromUserName,
                        String toUserName, String content, Boolean isSend, Boolean sendSucces, long time, String mediaName, String mediaId, long mediaLength
            , String roomKey) {
        super();
        this.type = type;
        this.state = state;
        this.fromUserName = fromUserName;
//        this.fromUserAvatar = fromUserAvatar;
        this.toUserName = toUserName;
//        this.toUserAvatar = toUserAvatar;
        this.content = content;
        this.isSend = isSend;
        this.sendSucces = sendSucces;
        this.time = time;
        this.roomKey = roomKey;
        this.mediaName = mediaName;
        this.mediaId = mediaId;
        this.mediaLength = mediaLength;
    }

    public MessageChat(int type, int state, String fromUserName, String fromUserAvatar, String toUserName, String toUserAvatar,
                       String content, Boolean isSend, Boolean sendSucces, long time, String roomKey) {
        super();
        this.type = type;
        this.state = state;
        this.fromUserName = fromUserName;
//        this.fromUserAvatar = fromUserAvatar;
        this.toUserName = toUserName;
//        this.toUserAvatar = toUserAvatar;
        this.content = content;
        this.isSend = isSend;
        this.sendSucces = sendSucces;
        this.time = time;
        this.roomKey = roomKey;
    }

    public MessageChat() {

    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public String getQueueId() {
        return queueId;
    }

    public String getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public long getMediaLength() {
        return mediaLength;
    }

    public void setMediaLength(long mediaLength) {
        this.mediaLength = mediaLength;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

//    public String getFromUserAvatar() {
//        return fromUserAvatar;
//    }
//
//    public void setFromUserAvatar(String fromUserAvatar) {
//        this.fromUserAvatar = fromUserAvatar;
//    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

//    public String getToUserAvatar() {
//        return toUserAvatar;
//    }
//
//    public void setToUserAvatar(String toUserAvatar) {
//        this.toUserAvatar = toUserAvatar;
//    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setIsSend(boolean isSend) {
        this.isSend = isSend;
    }

    public boolean isSendSucces() {
        return sendSucces;
    }

    public void setSendSucces(boolean sendSucces) {
        this.sendSucces = sendSucces;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
