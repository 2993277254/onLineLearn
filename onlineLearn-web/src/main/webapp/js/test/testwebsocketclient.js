

var websocket=null;
layui.use(['index'],function() {
    avalon.ready(function () {

        var path=getRootPath().replace('http://','');
        var pullPath="ws://"+path+"websocket.action";
        // debugger;
        // 首先判断是否 支持 WebSocket
        if('WebSocket' in window) {
            websocket = new WebSocket(pullPath);

        } else if('MozWebSocket' in window) {
            websocket = new MozWebSocket(pullPath);
        } else {
            websocket = new SockJS(getRootPath()+"sockjs/websocket.action");
        }
        console.log('websocket链接路径'+websocket);
        // 打开时
        websocket.onopen = function(evnt) {
            console.log("  websocket.onopen，连接成功  ");
            console.log(evnt);
        };


        // 处理消息时
        websocket.onmessage = function(evnt) {
            console.log('处理消息');
            console.log(evnt);
            alert(evnt.data);
        };
        websocket.onerror = function(evnt) {
            console.log("  websocket.onerror  "+JSON.stringify(evnt));
           alert('连接出错');
        };
        websocket.onclose = function(evnt) {
            console.log("  websocket.onclose  ");
        };
    });
});

function doSendUsers() {
    debugger;
    if (isNotEmpty(getUserId())) {
        if (websocket.readyState == websocket.OPEN) {
            var msg = {
                //userId:getUserId(),
                content: $("#inputMsg").val()
            };
            websocket.send(JSON.stringify(msg));//调用后台handleTextMessage方法
            alert("发送成功!");
        } else {
            alert("发送失败!");
        }
    }else {
        alert("连接失败!");
    }
}
