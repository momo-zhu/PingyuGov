package com.meetfine.pingyugov.utils;

import android.os.Environment;

import com.meetfine.pingyugov.R;
import com.meetfine.pingyugov.model.BranchModel;
import com.meetfine.pingyugov.model.GridItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Andriod05 on 2016/12/14.
 */
public class Config {

    public static final String HOST = "http://api.pingyu.gov.cn/api/";//平舆县政府公网地址
//    public static final String HOST = "http://pingyu.157.qia.im/api/";//戚佳鑫本地

    /*平舆县政府APP下载地址*/
    public static final String VERSION_CHECK_URL = "http://api.pingyu.gov.cn/Content/download/PingYuGov-update.json";
    public static final String DOWNLOAD_URL = "http://api.pingyu.gov.cn/Content/download/PingYuGov.apk";

    /*日期格式*/
    public static final SimpleDateFormat YEAR = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DETAIL = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat DATE = new SimpleDateFormat("MM-dd");
    public static final String YEAR_STR = "yyyy-MM-dd";
    public static final String DATE_STR = "MM-dd";
    public static final String DETAIL_STR = "yyyy-MM-dd HH:mm";
    public static final SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    /*权限申请*/
    public static final int REQUEST_PERMISSION = 127;
    public static final int REQUEST_PERMISSION_STORAGE = 128;
    public static final int REQUEST_PERMISSION_CAMERA = 129;
    public static final int REQUEST_PERMISSION_AUDIO = 130;
    /*文件下载地址*/
    public static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/PYAttachments";
    //0 平舆要闻，1 政务动态，2 部门动态，3 乡镇动态，/* 4国务院信息，5 省政府信息*/，6 图片新闻，7 通知公告，8 热点导读
    public static final String[] GovInfo = new String[]{
            "Contents?channelId=541a95e49a05c2b642f0dfa1",
            "Contents?channelId=5438f89f0701eef421000000",
            "Contents?channelId=541a95f99a05c2a9456288ef",
            "Contents?channelId=541a95d39a05c26b433bd5bf",
            "",
            "Contents?channelId=541a95d39a05c26b433bd5bf",
            "Contents?channelId=541a96079a05c21f45052f79",
            "Contents?channelId=541aa0299a05c2144606345a",
            ""
    };
    //0 县长邮箱,1 部门邮箱,2 平舆论坛, 3 在线访谈,4 民意征集,5 留言反馈
    public static final String[] GovInfo_Interaction = new String[]{
            "",
            "",
            "Content?channelId=",
            "InteractionLives?productId=545099e40701eee82e000024",
            "InteractionColls?type=541aafd49a05c2bc4e554798",
            "SiteFeedbacks?typeId=541a74d19a05c20d3b70cd10"
    };
    //0 招商动态, 1 重点项目, 2 投资环境, 3 优惠政策, 4 项目推介 , 5 企业风采
    public static final String[] GovInfo_Attract = new String[]{
            "Contents?channelId=541ab9369a05c2984e994b99",
            "Contents?channelId=541ab9559a05c2b14ede2bbe",
            "Contents?channelId=541aba2a9a05c2084787137c",
            "Contents?channelId=541aba539a05c20a47b16eb8",
            "Contents?channelId=541aba609a05c20a4772b954",
            "Contents?channelId=541ab97e9a05c28b4e120b9d"
    };
    //0 城市风景, 1 人文记录, 2 人文故事, 3 新闻摄影
    public static final String[] GovInfo_Pic = new String[]{
            "Contents?channelId=541acaba9a05c218548fc153",
            "Contents?channelId=541acaca9a05c2be54ba70f7",
            "Contents?channelId=541acae19a05c28a449ffc10",
            "Contents?channelId=541acaf19a05c2834efbee63",
    };
    ///0 重点领域 ，1民生领域 ，2特殊人群，3 市民办事 ，4 企业办事
    public static final String[] ServiceInfo = new String[]{
            "5398029eaf88bc447abcfdd9",
            "5970495aceab063b4e13e594",
            "539eb2e5af88bc90641b296a",
            "538fdcdaa0139884c80e4b36",
            "538fdcdaa0139884c80e4b3b",
    };
    /*“我的收藏”DataBase名称*/
    public final static String favorDBName = "PYGovNew_MyFavor_DATABASE";
    /*“我的收藏”详情类型*/
    public final static int[] favorTypes = {0,1,2,3};//3 民意征集

    /*请求新闻详情时的字段定义控制器顺序：Content*/
    public static final int ContentController = 0;
    public static final int opennesscontentController = 1;
    public static final int OpennessAnnualReportController = 2;
    public static final int OpennessRulesController = 3;
    public static final int OpennessRequestController = 4;
    public static final int      OpennessGuideController = 5;
    public static final int ServiceContentController = 6;
    public static final String[] ContentFlag = {"content", "opennessContent", "content", "rule", "request", "Content", "Content"};
    /*信息公开-县政府*/
    public static BranchModel govBranch = new BranchModel("", "县政府");

    /*信件处理状态及颜色定义*/
    public final static String[] ThreadStatus = {"未知", "新信件", "处理中", "已处理", "再追问", "已解决"};
    public final static String[] StatusColor = {"#FFCCCCCC", "#FFCCCCCC", "#FFFF0000", "#FF6666FF", "#FFFF0000", "#FF6666FF"};

    /*走进平舆*/
    public static ArrayList<GridItem> getEnjoyHSGridList1(){
        ArrayList<GridItem> gridItems = new ArrayList<>();
        gridItems.add(new GridItem("县情概要", "541a54a89a05c29434a51875", R.drawable.icon_introduce));
        gridItems.add(new GridItem("旅游资源", "541a553d9a05c2c22c8071fc", R.drawable.icon_tourism));
        gridItems.add(new GridItem("乡镇介绍", "541a554b9a05c2323433996c", R.drawable.icon_village));
        gridItems.add(new GridItem("城市建设", "596ff689ceab065d6013e593", R.drawable.icon_city_build));
        gridItems.add(new GridItem("平舆名人", "541a54d69a05c2c133e44ef5", R.drawable.icon_man_great));
        gridItems.add(new GridItem("历史文化", "541a55309a05c2483324d68d", R.drawable.icon_history));
        gridItems.add(new GridItem("平舆品牌", "541a8a4d9a05c2413ebb09e2", R.drawable.icon_brand));
        gridItems.add(new GridItem("平舆历史", "541a8a959a05c2c83c82cdd8", R.drawable.icon_situation));
        gridItems.add(new GridItem("名优特产", "541a8ab89a05c25d3ebf9623", R.drawable.icon_specialty));
        return gridItems;
    }


}
