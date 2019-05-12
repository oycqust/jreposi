package com.example.springb.controller;

import com.example.springb.Entity.Activity;
import com.example.springb.Entity.TableEntity;
import com.example.springb.data.DataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/table")
public class TableController {

    @RequestMapping("/list")
    public String list()
    {
        return "table_list";
    }

    @RequestMapping("/data")
    @ResponseBody
    public TableEntity data(Activity activity)
    {
        TableEntity tableEntity = new TableEntity();
        List<Activity> data = DataSource.getData();
        tableEntity.setRows(data);
        tableEntity.setTotal(data.size());
        return tableEntity;
    }

    @GetMapping("/add")
    public String add()
    {
        return "add";
    }

    @PostMapping("/add")
    public String addd(Activity e)
    {
        return "";
    }
}
