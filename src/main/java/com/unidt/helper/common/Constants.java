package com.unidt.helper.common;

public class Constants {

    //
    // 操作成功
    public static final int FRA_SUCCESS = 0;

    // 记录重复
    public static final int FRA_RECORD_REPEAT = 1;


    //API OK 200
    public static final int API_CODE_OK = 200;
    //
    // 未授权
    public static final int API_CODE_FORBIDDEN = 400;

    // 未授权
    public static final int API_TOKEN_DEADLINE = 401;
    //
    // 未找到
    public static final int API_CODE_NOT_FOUND = 404;
    //
    // 冲突
    public static final int API_CODE_CORRUPT = 409;
    //
    // 已删除
    public static final int API_CODE_DELETED = 410;
    //
    // 服务器内部错误
    public static final int API_CODE_INNER_ERROR = 500;
    //
    //  上传文件缓冲区大小
    public static final int UPLOAD_BUFFER = 1024 * 8;

    //  下载文件缓冲区大小
    public static final int DOWNLOAD_BUFFER = 1024 * 8;
	
    // 上传服务器文件的路径
    public static final String UPLOAD_PATH = "./uploadfile";

    public static final String UPLOAD_FILE_PATH = "http://118.25.142.128:9000/uploadfile";

    //文件服务器全路径
    public static final String UPLOAD_FILESYS_PATH = "http://shujia-1255423687.cossh.myqcloud.com";

    //教师资源上传地址
    public static final String UPLOAD_RESOURCES_PATH = "/k12resources";

    //作业上传地址
    public static final String UPLOAD_TEACHER_PATH = "/k12teacher";

    //作业上传地址
    public static final String UPLOAD_WORK_PATH = "/k12work";

    //文件对象存储bucket
    public static final String UPLOAD_FILE_BUCKET ="shujia-1255423687";

    //未删除 0 已删除 1
    public static final String  DEL_FLAG_0 = "0";

    public static final String DEL_FLAG_1 = "1";

   // 备课进度状态 01未开始，02进行中，03，已完成，04已取消

    public static final String  LESSON_CHAPTER_01 ="01";

    public static final String LESSON_CHAPTER_02 ="02";

    public static final String LESSON_CHAPTER_03 ="03";

    public static final String LESSON_CHAPTER_04 ="04";


    //课程授权  0 公开，1私有（仅自己可见），2 当前班级（老师及当前班级可见）
    public final static String LESSON_RIGHT_0 = "0";

    public final static String LESSON_RIGHT_1 = "1";

    public final static String LESSON_RIGHT_2 = "1";
}
