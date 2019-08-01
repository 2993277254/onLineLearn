package com.lsfly.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/10/10.
 */
public class MySessionListener extends SessionListenerAdapter {
    @Autowired
    private SessionDAO sessionDAO;

    @Override
    public void onStart(Session session) {//会话创建时触发
        System.out.println("会话创建：" + session.getId());
    }
    @Override
    public void onExpiration(Session session) {//会话过期时触发
        System.out.println("会话过期：" + session.getId());
        try {
            if(session != null) {
                sessionDAO.delete(session); //退出
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onStop(Session session) {//退出/会话过期时触发
        System.out.println("会话停止：" + session.getId());
        try {
            if(session != null) {
                sessionDAO.delete(session); //退出
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}