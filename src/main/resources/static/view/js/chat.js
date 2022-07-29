var nickname = ''
var websocket = null;
if ('WebSocket' in window) {
    nickname = prompt("输入您的昵称开始聊天")
    websocket = new WebSocket("ws://localhost:39112/socketService/toChatWith/" + nickname);
} else {
    alert("这个浏览器不支持");
}

websocket.onopen = function (event) {

}

websocket.onclose = function (event) {
    alert("关闭了");
}

websocket.onerror = function (event) {
    alert("传输错误");
}

websocket.onmessage = function (event) {
    document.getElementById("msgs").innerHTML += event.data;

}
document.onkeydown = function (e) { // 回车提交表单
    var theEvent = window.event || e;
    var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
    if (code === 13) {
        send();
    }
}

function selectUser() {
    $.ajax({
        type: "GET",
        url: "http://localhost:39112/join/getAllUser",
        data: {"nickname": nickname},
        dataType: "json",
        success: function (response) {
            if (response.code === 200) {
                let nicknameList = response.data;
                let selectHtml = '';
                if (nicknameList.length <= 0) {
                    alert("当前还没有用户登录")
                } else {
                    nicknameList.forEach(item => {
                        selectHtml += `<option value="${item}">${item}</option>`
                    });
                    $('#nicknameSelect').append(selectHtml)
                    $('#nicknameSelectTd').show()
                }
            }
        },
        error: function (msg) {
        }
    })
}

function sendMessageToUser() {
    let message = prompt("输入您要发送的消息")
    let toNickname = $('#nicknameSelect').val()
    $.ajax({
        type: "GET",
        url: "http://localhost:39112/join/sendMessageToUser",
        data: {
            "sendNickname": nickname,
            "nickname": toNickname,
            "message": message
        },
        dataType: "json",
        success: function (response) {
            if (response.code === 200) {
                alert("发送成功")
            }
        },
        error: function (msg) {
        }
    })
}


function send() {
    
}
// 发送信息
function SendMsg() {
    let message = $('#message');
    if (message.val() === "" || message.val() == null) {
        layer.msg('请输入消息');
    } else {
        message.val(SendMsgDispose(message.val()))
        AddMsg(message.val());
        let msg = `<div class="msg robot"><div class="msg-left" worker="${nickname}"><div class="msg-host photo" style="background-image: url(../img/melaleuca.png)"></div><div class="msg-ball" title="今天 17:52:06">${message.val()}</div></div></div>`
        websocket.send(msg);
        message.val("")
    }
    let chatWindows = $('#show');
    chatWindows[0].scrollTop = chatWindows[0].scrollHeight
}
// 发送的信息处理
function SendMsgDispose(detail) {
    detail = detail.replace("\n", "<br>").replace(" ", "&nbsp;")
    return detail;
}
// 增加信息
function AddMsg(content) {
    var msgs = document.getElementById("msgs");
    msgs.innerHTML = msgs.innerHTML + `<div class="msg guest"><div class="msg-right"><div class="msg-host headDefault"></div><div class="msg-ball" title="今天 17:52:06">${content}</div></div></div>`;
}
