/**
 * Created by huoquan on 2017/9/30.
 * 此文件为自定义avalon常用的过滤器
 */


//截取字符串
avalon.filters.funStrSub=function(str,num) {
    var n=20;
    if(num!=null)
        n=num;
    var s=str;
    if(str.length>num){
        s=str.substring(0,num)+"...";
    }
    return s;
};

// 将分钟数量转换为小时和分钟字符串
avalon.filters.toHourMinute=function(min) {
    var str="";
    if(Math.floor(min/60)>0)
        return (Math.floor(min/60) + "小时" + (min%60) + "分" );
    else
        str+=min+"分钟";
    return str;
};
