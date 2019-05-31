package com.utstar.integral.controller;

import ch.qos.logback.classic.Logger;
import com.utstar.common.RedisKeyConstant;
import com.utstar.integral.bean.*;
import com.utstar.integral.log.LoggerBuilder;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import com.utstar.integral.repository.btoc2.ActivityRepository;
import com.utstar.integral.repository.btoc2.CommonRepository;
import com.utstar.integral.repository.credb.MediaCodeRepository;
import com.utstar.integral.service.ActivityService;
import com.utstar.integral.thread.ThreadPoolManage;
import com.utstar.integral.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * created by UTSC1244 at 2019/5/10 0010
 */
@Controller
@RequestMapping("/activity")
public class ActivityController {
    @Resource
    private ActivityService activityService;
    @Resource
    private CommonRepository commonRepository;
    @Resource
    private ActivityRepository activityRepository;
    @Resource
    private MediaCodeRepository mediaCodeRepository;
    @Resource
    private RedisCommonDAO redisCommonDAO;
    @Resource
    private ThreadPoolManage threadPoolManage;
    @Resource
    private Environment environment;

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        LoggerBuilder.getLogger("test");
        ThreadPoolExecutor pool = threadPoolManage.getThreadPoolExecutor();
        Set<String> mediacodeSetWorld = redisCommonDAO.sMembers("sss");
        Set<String> mediacodeSetVoting = redisCommonDAO.hgetAllKey("aaa");
        System.out.println(mediacodeSetWorld.getClass().getName());
        System.out.println(mediacodeSetVoting.getClass().getName());
        Map<String, Long> map = new HashMap<>();
        map.put("str", 2L);
        redisCommonDAO.multiIncr(map);
        /*String st = redisCommonDAO.get("str");
        System.out.println(st);*/
        ActivityEntity activityEntity = activityService.selectByActivityId("001");
        //List<Object[]> objects = activityRepository.selectTest();
        //List<String> code = mediaCodeRepository.getCode();
        Logger logger = LoggerBuilder.getLogger("1");
        logger.info("{}", "info");
        logger.debug("{}", "debug");
        return "activity";
    }

    @GetMapping
    public String page(Model model) {
       /* String sqlCategoty = "SELECT CATEGORYID,NAME FROM BTO_C2.CATEGORY WHERE STATUS = '0'";
        List<?> categorys = commonRepository.getListMapBySql(sqlCategoty);*/
        String sqlBaseTag = "SELECT CODE,NAME FROM BTO_C2.BASTAG";
        List<?> baseTag = commonRepository.getListMapBySql(sqlBaseTag);
        String sqlOpTag = "SELECT CODE,NAME FROM BTO_C2.OPTAG";
        List<?> opTag = commonRepository.getListMapBySql(sqlOpTag);
        //model.addAttribute("categorys", categorys);
        model.addAttribute("baseTag", baseTag);
        model.addAttribute("opTag", opTag);
        return "activity/list";
    }

    @RequestMapping("/toadd")
    public String toAdd() {
        return "activity/add";
    }

    /**
     * 查询所有活动
     *
     * @param start   起始位置
     * @param limit   记录数
     * @param session session
     * @return table实体
     */
    @ResponseBody
    @PostMapping("/list")
    public TableEntity list(@RequestParam(value = "offset", defaultValue = "0") int start,
                            @RequestParam(value = "limit", defaultValue = "20") int limit,
                            ActivityEntity param,
                            HttpSession session) {

        TableEntity tableEntity = activityService.list(param, start, limit);
        return tableEntity;
    }

    @RequestMapping("/excelInput")
    @ResponseBody
    public Result importExcel(@RequestParam(value = "txt_file", required = false) MultipartFile file,
                              HttpServletRequest request,
                              String uuid) throws IOException {
        MultipartRequest multipartRequest = (MultipartRequest) request;
        MultipartFile excelFile = multipartRequest.getFile("txt_file");
        if (excelFile != null) {
            String fileName = excelFile.getOriginalFilename();
            String type = fileName.substring(fileName.lastIndexOf(".") + 1);
            //根据excel类型取数据
            if ("xls".equals(type) || "xlsx".equals(type)) {
                List<List<String>> datas = ("xlsx".equals(type) ? ExcelUtil.readXlsx(excelFile.getInputStream()) : ExcelUtil.readXls(excelFile.getInputStream()));
                //读取的内容后c处理入redis中
                activityService.storeMediacode2Redis(datas, uuid);
                return new Result(true, uuid);
            } else {
                return new Result(false, "请使用excel导入！");
            }
        }
        return new Result(false);
    }

    /**
     * 新增活动
     *
     * @param activityEntity 活动实体
     * @param session        session
     * @return Result
     */
    @ResponseBody
    @PostMapping("/add")
    public Result add(ActivityEntity activityEntity, HttpSession session) {
        activityService.add(activityEntity, getUsernameBySession(session));
        return new Result();
    }

    /**
     * 修改活动
     *
     * @param activityEntity 活动实体
     * @param session        session
     * @return Result
     */
    @ResponseBody
    @PostMapping("/edit")
    public Result edit(ActivityEntity activityEntity, HttpSession session) {
        activityService.edit(activityEntity, getUsernameBySession(session));
        return Result.SUCCESS;
    }

    /**
     * 删除活动
     *
     * @param ids 活动实体
     * @param session        session
     * @return Result
     */
    @ResponseBody
    @PostMapping("/del")
    public Result del(String[] ids, HttpSession session) {
        Assert.isTrue(ids != null && ids.length > 0 ,"请选择要删除的记录");
        activityService.del(Arrays.asList(ids), getUsernameBySession(session));
        return Result.SUCCESS;
    }

    @GetMapping("/ftp")
    @ResponseBody
    public Result getFtpUrl(){

        return new Result(true, environment.getProperty("ftp.location"));
    }

    private String getUsernameBySession(HttpSession session) {
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        return user.getUsername();
    }

    @GetMapping("/get_all_category")
    public String getAllCategorys(Model model) {
        String sql = "SELECT CODE,NAME FROM BTO_C2.CATEGORY WHERE STATUS = '0'";
        List<?> categorys = commonRepository.getListMapBySql(sql);
        model.addAttribute("categorys", categorys);
        return "activity/chosen";
    }

    @PostMapping("/cate")
    @ResponseBody
    public List<VueTreeEntity> getCategory(Integer pid) {
        return mediaCodeRepository.getCategoryByPid(pid);
    }

    @PostMapping("/cate_all")
    @ResponseBody
    public List<VueTreeEntity> getAllCategory(Integer pid) {
        return mediaCodeRepository.getCategoryByPid(pid);
    }

    @GetMapping("/get_acti_id")
    @ResponseBody
    public Result getActiId(){
        String id = activityService.getActivityId();
        Result result = new Result();
        result.setMessage(id);
        return result;
    }


}
