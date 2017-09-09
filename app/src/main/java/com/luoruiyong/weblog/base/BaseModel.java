package com.luoruiyong.weblog.base;

import org.litepal.crud.DataSupport;

/**模型基础类，model包下的所有类都继承于此类,
 * 而此类的父类是Litepal提供的DataSupport，
 * 封装了SQLite的增删查改等方法
 * Created by Administrator on 2017/9/7.
 */

public class BaseModel extends DataSupport {}
