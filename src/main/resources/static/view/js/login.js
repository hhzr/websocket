$(function () {
    getVerifyCode();
})
function getVerifyCode() {
    let vConsole = new VConsole();
    $.ajax({
        type: "GET",
        url: "http://192.168.5.145:39112/user/getVerifyCode",
        data: {},
        dataType: "json",
        success: function (response) {
            if (response.code === 200) {
                $('#verifyCode').html(`<span>${response.data}</span>`)
            } else {
                layer.msg(response.message)
            }
        },
        error: function (response) {
        }
    })
}
function login() {
    alert(123)
}
layui.use(['form', 'layedit', 'laydate'], function () {
    var form = layui.form,
        layer = layui.layer;

    //自定义验证规则
    form.verify({
        password: [
            /^[\S]{6,12}$/
            , '密码必须6到12位，且不能出现空格'
        ]
    });

    //监听提交
    form.on('submit(loginBtn)', function (data) {
        $.ajax({
            type: "POST",
            url: "http://192.168.5.145:39112/user/login",
            data: JSON.stringify(data.field),
            contentType: "application/json",
            dataType: "json",
            success: function (response) {
                if (response.code === 200) {
                    window.sessionStorage.setItem("user", JSON.stringify(response.data));
                    layer.msg(response.message);
                    window.location.href = "./chat.html";
                } else {
                    layer.msg(response.message)
                    getVerifyCode()
                }
            },
            error: function (msg) {
            }
        })
        return false;
    });

    //表单初始赋值
    /* form.val('example', {
       "username": "贤心" // "name": "value"
       ,"password": "123456"
       ,"interest": 1
       ,"like[write]": true //复选框选中状态
       ,"close": true //开关状态
       ,"sex": "女"
       ,"desc": "我爱 layui"
     })*/
});