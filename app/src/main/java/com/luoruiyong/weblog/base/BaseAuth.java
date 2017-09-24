package com.luoruiyong.weblog.base;

import com.luoruiyong.weblog.model.Customer;

/**用户验证帮助类,活动不直接访问Customer类中的静态成员变量
 * Created by Administrator on 2017/9/12.
 */

public class BaseAuth {
    public static boolean isLogin(){
        return Customer.getInstance().isLogin();
    }

    public static void setLogin(boolean login){
        Customer.getInstance().setLogin(login);
    }

    public static Customer getCustomer(){
        return Customer.getInstance();
    }

    public static void setCustomer(Customer cus){
        Customer customer = Customer.getInstance();
        customer.setId(cus.getId());
        customer.setSid(cus.getSid());
        customer.setAccount(cus.getAccount());
        customer.setNickName(cus.getNickName());
        customer.setIconUrl(cus.getIconUrl());
        customer.setIntroduction(cus.getIntroduction());
        customer.setCellNumber(cus.getCellNumber());
        customer.setEmail(cus.getEmail());
        customer.setAddress(cus.getAddress());
        customer.setBlogsCount(cus.getBlogsCount());
        customer.setFansCount(cus.getFansCount());
        customer.setConcernCount(cus.getConcernCount());
    }
}
