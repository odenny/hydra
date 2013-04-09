package com.jd.bdp.hydra.agent.support.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * User: xiangkui
 * Date: 13-3-27
 * Time: 下午2:23
 */
public class BinaryReadWrite {
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private String s_FilePath = System.getProperty("user.home")+"/.hydra"+"/TraceID.dat";

    public BinaryReadWrite() {
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {
        try {
            File fp=new File(s_FilePath);
            if(!fp.exists()){
                fp.mkdirs();
            }
            if (fp.exists()) {
                fp.renameTo(new File(s_FilePath+"."+System.currentTimeMillis()));
                fp.delete();
            }
            if (!fp.exists()) {
                fp.createNewFile();
            }
            dis = new DataInputStream(new FileInputStream(new File(s_FilePath)));
            dos = new DataOutputStream(new FileOutputStream(new File(s_FilePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeLong(Long l) {
        try {
            if (dos != null) {
                dos.writeLong(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void flushWrite(){
        try {
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    public long readLong() {
        try {
            if (dis != null) {
                return dis.readLong();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public String getS_FilePath() {
        return s_FilePath;
    }

    public void setS_FilePath(String s_FilePath) {
        this.s_FilePath = s_FilePath;
    }

    public static void main(String[] args) throws IOException {
        BinaryReadWrite bin = new BinaryReadWrite();
    }
}