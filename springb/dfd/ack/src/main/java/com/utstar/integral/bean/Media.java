package com.utstar.integral.bean;

import lombok.Data;

import java.util.List;

/**
 * 媒质项
 * @author UTSC0928
 * @date 2019/2/55
 */
@Data
public class Media {

    private String id;

    private List<String> codes;

    private String sysid;

    private String medianame;

    private String mediacode;

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public String getSysid() {
        return sysid;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getMedianame() {
        return medianame;
    }

    public void setMedianame(String medianame) {
        this.medianame = medianame;
    }

    public String getMediacode() {
        return mediacode;
    }

    public void setMediacode(String mediacode) {
        this.mediacode = mediacode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Media{" +
                "codes=" + codes +
                ", sysid='" + sysid + '\'' +
                ", medianame='" + medianame + '\'' +
                ", mediacode='" + mediacode + '\'' +
                '}';
    }
}
