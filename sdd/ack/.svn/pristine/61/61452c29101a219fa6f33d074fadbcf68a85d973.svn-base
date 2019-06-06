package com.utstar.integral.controller;


import com.alibaba.fastjson.JSONObject;
import com.utstar.integral.bean.ExcelUtil;
import com.utstar.integral.bean.Media;
import com.utstar.integral.bean.Result;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author UTSC0928
 * @date 2018/6/4
 */
@Controller
@RequestMapping(value = "/index")
public class IndexController {
    @Resource
    private   RedisCommonDAO redisCommonDAO;

    private  String id;

    @RequestMapping("index.do")
    public String toIndex(){
        return "index";
    }

    @RequestMapping("ListviewLog.do")
    public void listPlatformBeanPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer limit=Integer.valueOf(request.getParameter("limit"));
        Integer offset=Integer.valueOf( request.getParameter("offset"));
        if(limit==0||limit==null){
            limit = 50;
        }
        if(offset==0||offset==null){
            offset=0;
        }
        String key ="VOTE_MEDIA_CODE";

        String madianame = "";
        if(!StringUtils.isEmpty(String.valueOf(request.getParameter("madianame")))){
            madianame=String.valueOf(request.getParameter("madianame"));
        }
        String madiacode = "";
        if(!StringUtils.isEmpty(String.valueOf(request.getParameter("madiacode")))){
            madiacode = String.valueOf(request.getParameter("madiacode"));
        }
        Map<String, String> map = new TreeMap<String, String>(new Comparator<String>() {
            public int compare(String obj1, String obj2) {
                // 降序排序
                return obj2.compareTo(obj1);
            }
        });
        List<Media> wsSearchConfList = new ArrayList<>();
        Map<String, String> sourceMap = redisCommonDAO.hgetALL(key);
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            Media ws = new Media();
            if (!StringUtils.isEmpty(madianame) && !"null".equals(madianame) &&!(entry.getValue().contains(madianame))) {
                continue;
            }
            if (!StringUtils.isEmpty(madiacode) && !"null".equals(madiacode) &&!(entry.getKey().contains(madiacode))) {
                continue;
            }
            map.put(entry.getKey(), entry.getValue());
        }

        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            Media ws = new Media();
            String listKey = iter.next();
            ws.setMedianame(map.get(listKey));
            ws.setMediacode(listKey);
            ws.setId(listKey);
            wsSearchConfList.add(ws);
        }
        List<Media> countList = new ArrayList<>();
        int length = wsSearchConfList.size();
        if(length>=limit+offset){
            length = limit+offset;
        }
        for(int i=offset;i<length;i++){
            countList.add(wsSearchConfList.get(i));
        }
        JSONObject json = new JSONObject();
        json.put("rows", countList);
        json.put("total", wsSearchConfList.size());
        writeJsonForStr(json, response);

    }

    @RequestMapping("/excelInput.do")
    @ResponseBody
    public Result importExcel(@RequestParam(value="txt_file",required=false)MultipartFile file, HttpServletRequest request) throws IOException, InterruptedException {
        MultipartRequest multipartRequest = (MultipartRequest) request;
        MultipartFile excelFile = multipartRequest.getFile("txt_file");
//		Map<String,Object> map = new HashMap<String, Object>();
        if (excelFile != null) {
            String fileName = excelFile.getOriginalFilename();
            String type = fileName.substring(fileName.lastIndexOf(".") + 1);
            //根据excel类型取数据
            if ("xls".equals(type) || "xlsx".equals(type)) {
                List<List<String>> datas = ("xlsx".equals(type) ? ExcelUtil.readXlsx(excelFile.getInputStream()) : ExcelUtil.readXls(excelFile.getInputStream()));
                //读取的内容后c处理入redis中
                if (datas != null && datas.size() > 0) {
                    for(List<String> list:datas){
                        if(list.size()>=2) {
                                redisCommonDAO.hsetnx("VOTE_MEDIA_CODE", list.get(1), list.get(0));
                            }
                    }
                    return new Result(true);
                }
            } else {
                return new Result(false, "请使用excel导入！");
            }
        } else {
            return new Result(false);
        }
        return new Result(false);

    }

    @RequestMapping("/delete.do")
    @ResponseBody
    public Result deleteSource(@RequestBody String ids) {
        if(!StringUtils.isEmpty(ids)) {
            ids = ids.substring(1,ids.length()-1);
            String[] code = ids.split("@");
            for(int i=0;i<code.length;i++) {
                if(!StringUtils.isEmpty(code[i])) {
                    redisCommonDAO.hDelete("VOTE_MEDIA_CODE", code[i].trim());
                }
            }
            return new Result(true);
        }
        return new Result(false, "未选中任何值！");

    }
        private void writeJsonForStr(JSONObject json, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        try {
            response.getWriter().write(json.toString());
            response.getWriter().flush();
        } catch (IOException e) {
        }
    }

    @RequestMapping("/edit.do")
    @ResponseBody
    public Result editSource(@RequestBody String ids) {
        if(!StringUtils.isEmpty(ids)) {
            ids = ids.substring(1,ids.length()-1);
            String[] code = ids.split("@");
            if(!StringUtils.isEmpty(code[0])){
                redisCommonDAO.hDelete("VOTE_MEDIA_CODE", code[0].trim());
                redisCommonDAO.hsetnx("VOTE_MEDIA_CODE", code[1], code[2]);
                return new Result(true);
            }else{
                return new Result(false, "修改出错，请重试！");
            }
        }
        return new Result(false, "未选中任何值！");

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static void main(String[] args){
        Map<String,String> ame = new HashMap<>();
        ame.put("a", "2");
        ame.put("a", "3");
        ame.put("a", "4");
        ame.put("a", "5");
        System.out.println(ame);
    }
}
