package com.thinkd.xshare.entity;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * Ip Port 实体
 */
public class IpPortInfo implements Serializable {

    InetAddress inetAddress;
    int port;

    public IpPortInfo(InetAddress inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
