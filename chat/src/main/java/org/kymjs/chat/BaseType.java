package org.kymjs.chat;


public class BaseType {
    public class ChatMediaType {
        public static final int Text = 0;

        public static final int Location = 1;

        public static final int Image = 2;

        public static final int Voice = 3;

        public static final int Video = 4;

        public static final int Link = 5;

        public static final int ShortVideo = 6;

        public static final int Event = 7;

        public static final int Thumb = 8;

        public static final int Other = 9;
    }


    public class ChatMessageType {
        public static final int unknow = 0;

        public static final int notice = 1;

        public static final int info = 2;

        public static final int warning = 3;

        public static final int error = 4;

        public static final int danger = 5;
    }

    public class ChatOnlineStatus {

        public static final int offline = 0;

        public static final int free = 1;

        public static final int busy = 2;

        public static final int away = 3;

    }

    public class ChatRoomType {
        public static final int system = 0;

        public static final int staff_customer = 1;

        public static final int staff_staff = 2;

        public static final int group = 9;

    }

    public class DeviceType {

        public static final int unknow = 0;

        public static final int desktop = 1;

        public static final int mobile = 2;

        public static final int tablet = 3;

        public static final int web = 4;

    }



//    //类型1 仅前台客户有权限
//    //类型2 仅后台客服有权限
//    //类型3 仅前台、后台都有权限
//
//    //------------------------------------------------ 客户端 请求 模型类型
//    //客户端 方法是 模型前去掉 Client, 例如, 客户端发送分配请求的方法： Assign（ClientAssign model)
//
//
//    //分配客服受理客户请求 （类型1）
//    public class ClientRegisterUser
//    {
//        public deviceType DeviceType { set; get; }
//        public string FromChatUserKey { set; get; }
//        public DateTime CreateDate { set; get; }
//
//        public ClientRegisterUser()
//        {
//            this.CreateDate = DateTime.UtcNow;
//        }
//    }
//
//    //分配客服受理客户请求 （类型1）
//    public class ClientAssign
//    {
//        public string ChatRoomKey { set; get; }
//        public string FromChatUserKey { set; get; }
//        public string ToNickname { set; get; }
//        public DateTime CreateDate { set; get; }
//
//        public ClientAssign()
//        {
//            this.CreateDate = DateTime.UtcNow;
//        }
//    }
//
//    //邀请加入聊天室 （类型2）
//    public class ClientInvite
//    {
//        public string ChatRoomKey { set; get; }
//        public string CustomerKey { set; get; }
//        public string CustomerName { set; get; }
//        public string FromNickname { set; get; }
//        public string ToNickname { set; get; }
//        public string Message { set; get; }
//        public DateTime CreateDate { set; get; }
//
//        public ClientInvite()
//        {
//            this.CreateDate = DateTime.UtcNow;
//        }
//    }
//
//    //响应邀请加入聊天室 （类型2）
//    public class ClientRespondInvite
//    {
//        public string ChatRoomKey { set; get; }
//        public string CustomerKey { set; get; }
//        public string CustomerName { set; get; }
//        public string FromNickname { set; get; }
//        public string ToNickname { set; get; }
//        public string Message { set; get; }
//        public bool IsAccepted { set; get; }
//        public DateTime CreateDate { set; get; }
//
//        public ClientRespondInvite()
//        {
//            this.CreateDate = DateTime.UtcNow;
//        }
//    }
//
//
//    //将客服转交给另外一个客服 （类型2）
//    public class ClientTransferCustomer
//    {
//        public string ChatRoomKey { set; get; }
//        public string CustomerKey { set; get; }
//        public string CustomerName { set; get; }
//        public string FromNickname { set; get; }
//        public string ToNickname { set; get; }
//        public string Message { set; get; }
//        public DateTime CreateDate { set; get; }
//
//        public ClientTransferCustomer()
//        {
//            this.CreateDate = DateTime.UtcNow;
//        }
//    }
//
//    //响应客服转交给 （类型2）
//    public class ClientRespondTransfer
//    {
//        public string ChatRoomKey { set; get; }
//        public string CustomerKey { set; get; }
//        public string CustomerName { set; get; }
//        public string FromNickname { set; get; }
//        public string ToNickname { set; get; }
//        public string Message { set; get; }
//        public bool IsAccepted { set; get; }
//        public DateTime CreateDate { set; get; }
//
//        public ClientRespondTransfer()
//        {
//            this.CreateDate = DateTime.UtcNow;
//        }
//    }
//
//
//    //直接加入某个聊天室 （类型3）
//    public class ClientJoinRoom
//    {
//        public string ChatRoomKey { set; get; }
//        public string FromChatUserKey { set; get; }
//        public DateTime CreateDate { set; get; }
//
//        public ClientJoinRoom()
//        {
//            this.CreateDate = DateTime.UtcNow;
//        }
//    }
//
//
//    //直接离开聊天室 （类型3）
//    public class ClientLeftRoom
//    {
//        public string ChatRoomKey { set; get; }
//        public string FromChatUserKey { set; get; }
//        public string FromNickname { set; get; }
//        public DateTime CreateDate { set; get; }
//
//        public ClientLeftRoom()
//        {
//            this.CreateDate = DateTime.UtcNow;
//        }
//    }
//
//
//    //发起客服之间聊天 （类型2）
//    public class ClientPrivateMessage
//    {
//        public string FromChatUserKey { set; get; }
//        public string ToNickname { set; get; }
//        public DateTime CreateDate { set; get; }
//
//        public ClientPrivateMessage()
//        {
//            this.CreateDate = DateTime.UtcNow;
//        }
//    }
//
//
//    //发送聊天信息 （类型3）
//    //如果是图片信息，Message内容为 缩略图的 URL
//    public class ClientSendMessage
//    {
//        public string ChatRoomKey { set; get; }
//        public string FromChatUserKey { set; get; }
//        public string QueueId { set; get; }
//        public string Message { set; get; }
//        public chatMediaType MediaType { set; get; }
//        public string MediaId { set; get; }
//        public int MediaLength { set; get; }
//        public DateTime CreateDate { set; get; }
//
//        public ClientSendMessage()
//        {
//            this.CreateDate = DateTime.UtcNow;
//        }
//    }
//
//    //信息已读 （类型2）
//    public class ClientHasRead
//    {
//        public int MessageId { set; get; }
//        public string FromChatUserKey { set; get; }
//        public DateTime CreateDate { set; get; }
//
//        public ClientHasRead()
//        {
//            this.CreateDate = DateTime.UtcNow;
//        }
//    }
//
//    //修改客服的服务状态 （类型2）
//    public class ClientChangeStatus
//    {
//        public chatOnlineStatus Status { set; get; }
//        public string FromChatUserKey { set; get; }
//        public DateTime CreateDate { set; get; }
//
//        public ClientChangeStatus()
//        {
//            this.CreateDate = DateTime.UtcNow;
//        }
//    }
//
//    //正在输入 （类型3）
//    public class ClientTyping
//    {
//        public string ChatRoomKey { set; get; }
//        public string FromChatUserKey { set; get; }
//        public DateTime CreateDate { set; get; }
//
//        public ClientTyping()
//        {
//            this.CreateDate = DateTime.UtcNow;
//        }
//    }
//
//
//    //----------------------------------------- 服务器端 回调 模型
//
//    //客户端连接成功 （类型3）
//    public class Connected
//    {
//        public bool Status { set; get; }
//        public string ChatRoomKey { set; get; }
//        public string ChatUserKey { set; get; }
//        public iShang.App.chatUserType ChatUserType { set; get; }
//        public string Nickname { set; get; }
//        public string Avatar { set; get; }
//        public string CompanyName { set; get; }
//        public string Message { set; get; }
//        public deviceType DeviceType { set; get; }
//        public DateTime CreateDate { set; get; }
//        public chatMessageType MessageType { set; get; }
//
//        public Connected()
//        {
//            this.CreateDate = DateTime.UtcNow;
//            this.MessageType = chatMessageType.notice;
//        }
//    }
//
//    //收到 聊天室的信息 （类型3）
//    public class Received
//    {
//        public string FromRoomKey { set; get; }
//        public int ChatRoomType { set; get; }
//        public string Nickname { set; get; }
//        public string Avatar { set; get; }
//        public int ChatMessageId { set; get; }
//        public string Message { set; get; }
//        public string MediaName { set; get; }
//        public chatMediaType MediaType { set; get; }
//        public string MediaId { set; get; }
//        public int MediaLength { set; get; }
//        public DateTime CreateDate { set; get; }
//        public chatMessageType MessageType { set; get; }
//
//        public Received()
//        {
//            this.FromRoomKey = "";
//            this.ChatRoomType = 1;
//            this.Avatar = "";
//            this.MediaType = chatMediaType.Text;
//            this.CreateDate = DateTime.UtcNow;
//            this.MessageType = chatMessageType.info;
//        }
//    }
//
//    public class RegisterUser
//    {
//        public string ChatRoomKey { set; get; }
//        public string FromChatUserKey { set; get; }
//        public bool Status { set; get; }
//        public DateTime CreateDate { set; get; }
//
//        public RegisterUser()
//        {
//            this.CreateDate = DateTime.UtcNow;
//        }
//    }
//
//    //分配客户给客服 （类型3）
//    public class Assign
//    {
//        public string ChatRoomKey { set; get; }
//        public int ChatRoomType { set; get; }
//        public string ChatUserKey { set; get; }
//        public iShang.App.chatUserType ChatUserType { set; get; }
//        public string Nickname { set; get; }
//        public string Avatar { set; get; }
//        public string CompanyName { set; get; }
//        public deviceType DeviceType { set; get; }
//        public DateTime CreateDate { set; get; }
//        public chatMessageType MessageType { set; get; }
//
//        public Assign()
//        {
//            this.CreateDate = DateTime.UtcNow;
//            this.MessageType = chatMessageType.notice;
//        }
//    }
//
//    //修改状态 （类型2）
//    public class ChangeStatus
//    {
//        public chatOnlineStatus Status { set; get; }
//        public string Nickname { set; get; }
//        public Dictionary<string, chatOnlineStatus> LiveChatUser { set; get; }
//        public DateTime CreateDate { set; get; }
//        public chatMessageType MessageType { set; get; }
//
//        public ChangeStatus()
//        {
//            this.CreateDate = DateTime.UtcNow;
//            this.MessageType = chatMessageType.notice;
//        }
//    }
//
//    //邀请加入聊天室 （类型2）
//    public class Invite
//    {
//        public string ChatRoomKey { set; get; }
//        public int ChatRoomType { set; get; }
//        public string CustomerKey { set; get; }
//        public string CustomerName { set; get; }
//        public string FromNickname { set; get; }
//        public string ToNickname { set; get; }
//        public string Message { set; get; }
//        public DateTime CreateDate { set; get; }
//        public chatMessageType MessageType { set; get; }
//
//        public Invite()
//        {
//            this.CreateDate = DateTime.UtcNow;
//            this.MessageType = chatMessageType.notice;
//        }
//    }
//
//    //响应邀请加入聊天室 （类型2）
//    public class RespondInvite
//    {
//        public bool IsAccepted { set; get; }
//        public string ChatRoomKey { set; get; }
//        public int ChatRoomType { set; get; }
//        public string CustomerKey { set; get; }
//        public string CustomerName { set; get; }
//        public string FromNickname { set; get; }
//        public string ToNickname { set; get; }
//        public string Message { set; get; }
//        public DateTime CreateDate { set; get; }
//        public chatMessageType MessageType { set; get; }
//
//        public RespondInvite()
//        {
//            this.CreateDate = DateTime.UtcNow;
//            this.MessageType = chatMessageType.notice;
//        }
//    }
//
//    //将客服转交给另外一个客服 （类型2）
//    public class TransferCustomer
//    {
//        public string ChatRoomKey { set; get; }
//        public int ChatRoomType { set; get; }
//        public string CustomerKey { set; get; }
//        public string CustomerName { set; get; }
//        public string FromNickname { set; get; }
//        public string ToNickname { set; get; }
//        public string Message { set; get; }
//        public DateTime CreateDate { set; get; }
//        public chatMessageType MessageType { set; get; }
//
//        public TransferCustomer()
//        {
//            this.CreateDate = DateTime.UtcNow;
//            this.MessageType = chatMessageType.notice;
//        }
//    }
//
//    //响应客服转交给 （类型2）
//    public class RespondTransfer
//    {
//        public bool IsAccepted { set; get; }
//        public string ChatRoomKey { set; get; }
//        public int ChatRoomType { set; get; }
//        public string CustomerKey { set; get; }
//        public string CustomerName { set; get; }
//        public string FromNickname { set; get; }
//        public string ToNickname { set; get; }
//        public string Message { set; get; }
//        public DateTime CreateDate { set; get; }
//        public chatMessageType MessageType { set; get; }
//
//        public RespondTransfer()
//        {
//            this.CreateDate = DateTime.UtcNow;
//            this.MessageType = chatMessageType.notice;
//        }
//    }
//
//    //加入聊天室 （类型3）
//    public class JoinRoom
//    {
//        public string ChatRoomKey { set; get; }
//        public int ChatRoomType { set; get; }
//        public string Nickname { set; get; }
//        public DateTime CreateDate { set; get; }
//        public chatMessageType MessageType { set; get; }
//
//        public JoinRoom()
//        {
//            this.CreateDate = DateTime.UtcNow;
//            this.MessageType = chatMessageType.notice;
//        }
//    }
//
//    //离开聊天室 （类型3）
//    public class LeftRoom
//    {
//        public string ChatRoomKey { set; get; }
//        public int ChatRoomType { set; get; }
//        public string Nickname { set; get; }
//        public string Name { set; get; }
//        public string ChatUserKey { set; get; }
//        public string ConnectionId { set; get; }
//        public DateTime CreateDate { set; get; }
//        public chatMessageType MessageType { set; get; }
//
//        public LeftRoom()
//        {
//            this.CreateDate = DateTime.UtcNow;
//            this.MessageType = chatMessageType.notice;
//        }
//    }
//
//    //客服在线 （类型2）
//    public class UserOnline
//    {
//        public string Nickname { set; get; }
//        public string Name { set; get; }
//        public DateTime CreateDate { set; get; }
//        public chatMessageType MessageType { set; get; }
//
//        public UserOnline()
//        {
//            this.CreateDate = DateTime.UtcNow;
//            this.MessageType = chatMessageType.notice;
//        }
//    }
//
//    //客服在线 （类型2）
//    public class UserOffline
//    {
//        public string Nickname { set; get; }
//        public string Name { set; get; }
//        public DateTime CreateDate { set; get; }
//        public chatMessageType MessageType { set; get; }
//
//        public UserOffline()
//        {
//            this.CreateDate = DateTime.UtcNow;
//            this.MessageType = chatMessageType.notice;
//        }
//    }
//
//    //正在输入中 （类型3）
//    public class Typing
//    {
//        public string ChatRoomKey { set; get; }
//        public int ChatRoomType { set; get; }
//        public string Nickname { set; get; }
//        public DateTime CreateDate { set; get; }
//        public chatMessageType MessageType { set; get; }
//
//        public Typing()
//        {
//            this.CreateDate = DateTime.UtcNow;
//            this.MessageType = chatMessageType.notice;
//        }
//    }
//
//    //信息发送 （类型3）
//    public class HasSent
//    {
//        public string ChatRoomKey { set; get; }
//        public int ChatRoomType { set; get; }
//        public string QueueId { set; get; }
//        public string FromChatUserKey { set; get; }
//        public DateTime CreateDate { set; get; }
//        public chatMessageType MessageType { set; get; }
//
//        public HasSent()
//        {
//            this.CreateDate = DateTime.UtcNow;
//            this.MessageType = chatMessageType.notice;
//        }
//    }
//
//    //同事之间发起私信 （类型2）
//    public class PrivateMessage
//    {
//        public string ChatRoomKey { set; get; }
//        public int ChatRoomType { set; get; }
//        public string Nickname { set; get; }
//        public bool OtherOnline { set; get; }
//        public DateTime CreateDate { set; get; }
//        public chatMessageType MessageType { set; get; }
//
//        public PrivateMessage()
//        {
//            this.OtherOnline = false;
//            this.CreateDate = DateTime.UtcNow;
//            this.MessageType = chatMessageType.notice;
//        }
//
//    }

}