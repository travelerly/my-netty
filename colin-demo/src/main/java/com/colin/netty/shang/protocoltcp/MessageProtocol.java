package com.colin.netty.shang.protocoltcp;

/**
 * 协议包
 *
 * @author: colin
 * @Create: 2023/1/24 12:26
 */
public class MessageProtocol {

    private int len;
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
