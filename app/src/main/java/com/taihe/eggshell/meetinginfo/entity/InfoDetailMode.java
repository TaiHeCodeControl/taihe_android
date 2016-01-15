package com.taihe.eggshell.meetinginfo.entity;

import java.util.List;

/**
 * Created by Thinkpad on 2015/7/14.
 */
public class InfoDetailMode {

    /**
     * data : [{"child":[{"uid":"15153","rphoto":"","username":"18899999999","addtime":"1452585739","ruid":"15154","uname":"今日测试","r_coment":"我良辰有一百种方法让你不服，而你却无可奈何","rname":"","rusername":"18900000000","uphoto":""},{"uid":"15152","rphoto":"","username":"15977777777","addtime":"1452585793","ruid":"15153","uname":"","r_coment":"放肆，在我龙傲天面前谁敢这么拽","rname":"今日测试","rusername":"18899999999","uphoto":""}],"uid":"15154","d_id":"3","username":"18900000000","addtime":"1452585592","uname":"","uphoto":"","aid":"250","d_coment":"我赵日天第一个不服"},{"child":[],"uid":"15159","d_id":"2","username":"15800000000","addtime":"1452576300","uname":"薛飞","uphoto":"","aid":"250","d_coment":"大人真乃神人也！"},{"child":[{"uid":"15157","rphoto":"","username":"13911234576","addtime":"1452576200","ruid":"15159","uname":"test","r_coment":"大人，我觉得此事必有阴谋","rname":"薛飞","rusername":"15800000000","uphoto":""},{"uid":"15156","rphoto":"","username":"15877777777","addtime":"1452576500","ruid":"15157","uname":"测试学员","r_coment":"元芳别闹了，快回家","rname":"test","rusername":"13911234576","uphoto":""},{"uid":"15158","rphoto":"","username":"13126587777","addtime":"1452586500","ruid":"15157","uname":"下达","r_coment":"元芳，你老婆叫你回家吃饭","rname":"test","rusername":"13911234576","uphoto":""}],"uid":"15158","d_id":"1","username":"13126587777","addtime":"1452576158","uname":"下达","uphoto":"","aid":"250","d_coment":"我觉得楼主说的对，元芳你说呢？"}]
     * code : 0
     */

    private int code;

    public void setCode(int code) {
        this.code = code;
    }


    public int getCode() {
        return code;
    }
        /**
         * child : [{"uid":"15153","rphoto":"","username":"18899999999","addtime":"1452585739","ruid":"15154","uname":"今日测试","r_coment":"我良辰有一百种方法让你不服，而你却无可奈何","rname":"","rusername":"18900000000","uphoto":""},{"uid":"15152","rphoto":"","username":"15977777777","addtime":"1452585793","ruid":"15153","uname":"","r_coment":"放肆，在我龙傲天面前谁敢这么拽","rname":"今日测试","rusername":"18899999999","uphoto":""}]
         * uid : 15154
         * d_id : 3
         * username : 18900000000
         * addtime : 1452585592
         * uname :
         * uphoto :
         * aid : 250
         * d_coment : 我赵日天第一个不服
         */

    private String uid;
    private String d_id;
    private String username;
    private String addtime;
    private String uname;
    private String uphoto;
    private String aid;
    private String d_coment;
    private List<ChildEntity> child;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setUphoto(String uphoto) {
        this.uphoto = uphoto;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public void setD_coment(String d_coment) {
        this.d_coment = d_coment;
    }

    public void setChild(List<ChildEntity> child) {
        this.child = child;
    }

    public String getUid() {
        return uid;
    }

    public String getD_id() {
        return d_id;
    }

    public String getUsername() {
        return username;
    }

    public String getAddtime() {
        return addtime;
    }

    public String getUname() {
        return uname;
    }

    public String getUphoto() {
        return uphoto;
    }

    public String getAid() {
        return aid;
    }

    public String getD_coment() {
        return d_coment;
    }

    public List<ChildEntity> getChild() {
        return child;
    }

    public static class ChildEntity {
        /**
         * uid : 15153
         * rphoto :
         * username : 18899999999
         * addtime : 1452585739
         * ruid : 15154
         * uname : 今日测试
         * r_coment : 我良辰有一百种方法让你不服，而你却无可奈何
         * rname :
         * rusername : 18900000000
         * uphoto :
         */

        private String uid;
        private String rphoto;
        private String username;
        private String addtime;
        private String ruid;
        private String uname;
        private String r_coment;
        private String rname;
        private String rusername;
        private String uphoto;

        public void setUid(String uid) {
            this.uid = uid;
        }

        public void setRphoto(String rphoto) {
            this.rphoto = rphoto;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public void setRuid(String ruid) {
            this.ruid = ruid;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public void setR_coment(String r_coment) {
            this.r_coment = r_coment;
        }

        public void setRname(String rname) {
            this.rname = rname;
        }

        public void setRusername(String rusername) {
            this.rusername = rusername;
        }

        public void setUphoto(String uphoto) {
            this.uphoto = uphoto;
        }

        public String getUid() {
            return uid;
        }

        public String getRphoto() {
            return rphoto;
        }

        public String getUsername() {
            return username;
        }

        public String getAddtime() {
            return addtime;
        }

        public String getRuid() {
            return ruid;
        }

        public String getUname() {
            return uname;
        }

        public String getR_coment() {
            return r_coment;
        }

        public String getRname() {
            return rname;
        }

        public String getRusername() {
            return rusername;
        }

        public String getUphoto() {
            return uphoto;
        }
    }
}
