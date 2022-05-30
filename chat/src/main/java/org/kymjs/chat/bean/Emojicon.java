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

/**
 * emoji表情的javabean
 *
 * @author kymjs (http://www.kymjs.com/) on 6/8/15.
 */
public class Emojicon {
    private String name; //在网络传递中的值

    public String getWeixinName() {
        return weixinName;
    }

    public void setWeixinName(String weixinName) {
        this.weixinName = weixinName;
    }

    private String weixinName; //在网络传递中的值
    //    private byte[] code; //在系统中所代表的值
    private String fileName; //code转换为String的值

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private int resource;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Emojicon(String name, String fileName, int resource) {
        this.name = name;
        this.fileName = fileName;
        this.resource = resource;
    }

    public Emojicon() {
    }
    //    public byte[] getCode() {
//        return code;
//    }
//
//    public void setCode(byte[] code) {
//        this.code = code;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
//    }
//
//    /**
//     * @return code转换为String的值
//     */
//    public String getValue() {
//        if (code == null) {
//            return null;
//        } else {
//            return new String(code);
//        }
//    }
}
