var user = JSON.parse(window.sessionStorage.getItem("user"))
var nickname = ''
var headImg = ''
var websocket = null;
$(function (){
    let vConsole = new VConsole();
    if (user == null) {
        window.location.href = "./login.html"
    } else {
        nickname = user.userNickname
        headImg = user.userHeadImg
        userId = user.userId
    }

    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://192.168.5.145:39112/socketService/toChatWith/" + userId);
    } else {
        alert("这个浏览器不支持");
    }

    websocket.onopen = function (event) {
        console.log("已建立连接")
    }

    websocket.onclose = function (event) {
        console.log(event)
        console.log("连接已关闭")
    }

    websocket.onerror = function (event) {
        console.log(event)
    }

    websocket.onmessage = function (event) {
        let messageData = JSON.parse(event.data);
        let msgHtml = `<div class="msg other">
                           <div class="msg-left" worker="${messageData.nickname}">
                               <div class="msg-host photo" style="background-image: url(${messageData.headImg})">                          
                               </div>
                               <div class="msg-ball" title="今天 17:52:06">${messageData.message}
                               </div>
                           </div>
                       </div>`
        $('#msgs').append(msgHtml)
    }
})
document.onkeydown = function (e) { // 回车提交表单
    var theEvent = window.event || e;
    var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
    if (code === 13) {
        SendMsg();
    }
}

function selectUser() {
    $.ajax({
        type: "GET",
        url: "http://192.168.5.145:39112/join/getAllUser",
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
        url: "http://192.168.5.145:39112/join/sendMessageToUser",
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
        let msg = message.val()
        message.val(SendMsgDispose(msg))
        AddMsg(msg);
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
    msgs.innerHTML = msgs.innerHTML + `<div class="msg me"><div class="msg-right""><div class="msg-host headDefault" style="background-image: url(${user.userHeadImg})"></div><div class="msg-ball" title="今天 17:52:06">${content}</div></div></div>`;
}
