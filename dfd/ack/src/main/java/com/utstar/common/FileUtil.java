package com.utstar.common;

import com.utstar.integral.bean.UserViewLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import javax.xml.bind.SchemaOutputResolver;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author UTSC0928
 * @date 2018/5/31
 */
public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    private static final String SUFFIX = ".cdr";

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取dir目录下所有后缀为cdr的文件，且cdr文件的时间大于cdrTime
     * 并且按照时间升序顺序来排序
     * @param dir      cdr文件所在目录
     * @param cdrTime 已经处理的文件的最大时间
     * @return
     */
    public static List<File> getListCdrFileByDir(String dir,String cdrTime){
        File fileDir = new File(dir);
        List<File> fileList = Arrays.stream(fileDir.listFiles())
                //先过滤掉不是文件和文件后缀不是cdr的内容
                .filter(file -> file.isFile() && file.getName().endsWith(SUFFIX))
                .filter(file -> {
                    boolean flag =  file.isFile() && file.getName().endsWith(SUFFIX);
                    String fileTime = getTimeFromFile(file);
                    //文件名的时间一定要大于当前时间
                    return flag && DateTimeUtil.getTime(fileTime)>DateTimeUtil.getTime(cdrTime) && DateTimeUtil.getTime(fileTime)<System.currentTimeMillis();
                })
                .sorted((File fileA, File fileB) -> {
                    String fileTimeA = getTimeFromFile(fileA);

                    String fileTimeB = getTimeFromFile(fileB);

                    return (int)(DateTimeUtil.getTime(fileTimeA)-DateTimeUtil.getTime(fileTimeB));
                })
                .collect(Collectors.toList());


        return fileList;
    }


    /**
     * 根据cdrTime在dataDir目录下寻找cdrTime相同的所有文件
     * @param dataDir
     * @param cdrTime
     * @return
     */
    public static List<File> getDuplicateFileByCdrTime(String dataDir, String cdrTime){
        File fileDir = new File(dataDir);
        List<File> duplicateFiles = Arrays.stream(fileDir.listFiles())
                //先过滤掉不是文件和文件后缀不是cdr的内容
                .filter(file -> file.isFile() && file.getName().endsWith(SUFFIX))
                .filter(file -> {
                    boolean flag =  file.isFile() && file.getName().endsWith(SUFFIX);
                    String fileTime = getTimeFromFile(file);
                    return flag && DateTimeUtil.getTime(fileTime)== DateTimeUtil.getTime(cdrTime) && DateTimeUtil.getTime(fileTime)<System.currentTimeMillis();
                })
                .collect(Collectors.toList());
        return duplicateFiles;
    }


    public static List<UserViewLog> readUserViewLogsFromFile(File file){
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_FORMAT);
        List<UserViewLog> userViewLogs = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf8"));
            String line = null;
            String type = null;
            String userId = null;
            String mediacode = null;
            String seriesFlag = null;
            String parentobject = null;
            Date viewStart = null;
            Date viewEnd = null;
            Long second = null;
            String sysid = null;
            while ((line=bufferedReader.readLine()) != null){
                String[] arr = line.split(",");
                try {
                    type = arr[1];
                    userId = arr[3];
                    mediacode = arr[4];
                    viewStart = dateTimeFormat.parse(arr[5]);
                    viewEnd = dateTimeFormat.parse(arr[6]);
                    second = Long.valueOf(arr[7]);
                    seriesFlag = arr[14];
                    parentobject = arr[15];
                    sysid = arr[19];
                } catch (Exception e) {
                    LOGGER.error("read type userId mediacode second from line by file "+file.getName()+" failed. ",e);
                }
                UserViewLog userViewLog = new UserViewLog(userId, type, mediacode, second,seriesFlag,parentobject, viewStart, viewEnd, sysid);
                userViewLogs.add(userViewLog);
            }
        } catch (Exception e) {
            LOGGER.error("read info from "+file.getName()+" failed. ",e);
        } finally {
            if(bufferedReader !=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    LOGGER.error("close buffered failed. ",e);
                }
            }
        }

        return userViewLogs;
    }

    /*public static Map<String,Long> readUserIdSecondFromFileByVspcode(Set<String> mediacodeSet, File file){
        Map<String,Long> result = new HashMap<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf8"));
            String line = null;
            String type = null;
            String userId = null;
            String mediaCode = null;
            Long seconds = null;
            while ((line=bufferedReader.readLine()) != null){
                String[] arr = line.split(",");
                try {
                    type = arr[1];
                    userId = arr[3];
                    mediaCode = arr[4];
                    seconds = Long.valueOf(arr[7]);
                } catch (Exception e) {
                    LOGGER.error("read type userId mediacode seconds from line by file "+file.getName()+" failed. ",e);
                }
                //观看的影片的mediacode要匹配vspcode且type要是v
                if("v".equals(type) && mediaCode != null && mediacodeSet.contains(mediaCode)){
                    long oldSecond = result.getOrDefault(userId, 0L);
                    result.put(userId, oldSecond+seconds);
                }
            }

        } catch (Exception e) {
            LOGGER.error("read info from "+file.getName()+" failed. ",e);
        }finally {
            if(bufferedReader !=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    LOGGER.error("close buffered failed. ",e);
                }
            }
        }

        return result;
    }*/

    /**
     * 根据cdr文件获取cdr文件的时间
     * @param file
     * @return
     */
    public static String getTimeFromFile(File file){
        String name = file.getName().split(SUFFIX)[0];
        return name.substring(name.length() - 12);
    }

    public static void main(String[] args) {
//        String dataDir = "D:\\opt\\spark\\sparkStreaming\\data\\viewlog";
//        List<File> fileList = getListCdrFileByDir(dataDir, "201805312359");
//
//        System.out.println(fileList);
//        fileList.forEach(file -> System.out.println(file.getName()));
//
//
//        readUserIdSecondFromFileByVspcode("",fileList.get(0));

        LOGGER.info("{} love {}","patrick","music");

        Arrays.stream("".split(",")).forEach(str-> System.out.println("xx"+str));

        File file = new File("D:\\opt\\spark\\sparkStreaming\\data\\viewlog1");
        System.out.println(file.listFiles().length);
    }


    public static Set<String> readMediaCode(String url){
        File file = new File(url);
        Set<String> set = new HashSet<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf8"));
            String line;
            while ((line=bufferedReader.readLine()) != null){
                set.add(line);
            }
        } catch (Exception e) {
            LOGGER.error("read "+url+" failed. ",e);
        }finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    LOGGER.error("close bufferedReader failed. ",e);
                }
            }
        }
        return set;
    }
}
