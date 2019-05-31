<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>活动页面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap/bootstrap-table.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/plug-in/bootstrap-fileinput-master/css/fileinput.min.css"/>
      <!--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/chosen/chosen.min.css"/>-->
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap/bootstrap-select.min.css">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vue/vue-treeselect.min.css">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/activity/list.css">
  </head>
  <body>
   <table id="ArbetTable">
       <form id="searchForm">
            <div id="search-div">
                <p class="text-center" style="font-size:25px;margin-top:10px;padding-top: 10px;">活动管理</p>
               <div class="form-group" >
                   <label for="activityId-search">活动ID</label>
                   <input type="text" class="form-control search-container" id="activityId-search" name="activityId">
               </div>
               <div class="form-group" >
                   <label for="activityName-search">活动名称</label>
                   <input type="text" class="form-control search-container" id="activityName-search" name="activityName">
               </div>
               <div class="form-group" >
                   <label for="validTime-search">开始时间</label>
                   <input type="text" class="form-control search-container search-date" id="validTime-search" name="validTime">
               </div>
                <div class="form-group" >
                    <label for="expireTime-search">结束时间</label>
                    <input type="text" class="form-control search-container search-date" id="expireTime-search" name="expireTime">
                </div>
                <div class="form-group">
                     <label for="statusType-search">状态</label>
                     <select name="statusType" id="statusType-search" class="form-control search-container">
                       <option value="" selected="selected">全部</option>
                       <option value="VALID">有效</option>
                       <option value="EXPIRED" >无效</option>
                     </select>
               </div>
            </div>
       </form>

   </table>
    <div id="toolbar1" class="btn-group">
        <button id="btn_add" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增
        </button>
        <button id="btn_edit" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>修改
        </button>
        <button id="btn_delete" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除
        </button>
        <button id="btn_search" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-search" aria-hidden="true"></span>搜索
        </button>
        <button id="btn_ftp" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-hand-up" aria-hidden="true"></span>ftp地址
        </button>
    </div>

            <!--  add modal start-->
            <div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" id="addModal">
                      <div class="modal-dialog modal-lg" role="document">
                          <div class="modal-content">
                            <!-- content start -->
                            <div class="modal-header">
                                <!--<button type="button" class="close" onclick="activityObj.closeAddModal()" aria-label="Close"><span aria-hidden="true">&times;</span></button>-->
                                <h4 id="add-modal-id" class="modal-title">新增活动</h4>
                              </div>
                              <div class="modal-body">
                                <form id="addForm">
                                  <div class="form-group">
                                    <label for="activityId">活动ID</label>
                                    <input type="text" class="form-control" id="activityId" name="activityId" placeholder="请填写活动id" readonly>
                                  </div>
                                  <div class="form-group">
                                    <label for="activityName">活动名称</label>
                                    <input type="text" class="form-control" id="activityName" name="activityName" placeholder="请填写活动名称">
                                  </div>
                                  <div class="form-group">
                                    <label for="validTime">开始时间</label>
                                    <input type="text" class="form-control activity-date" id="validTime"
                                            name="validTime" placeholder="请选择开始时间">
                                  </div>
                                  <div class="form-group">
                                    <label for="expireTime">结束时间</label>
                                    <input type="text" class="form-control activity-date" id="expireTime"
                                            name="expireTime" placeholder="请选择结束时间">
                                  </div>

                                    <!--<div class="form-group">
                                        <label for="activityType">活动类型</label>
                                        <select name="activityType" id="activityType" class="form-control">
                                            <option value="INCREASE" selected="selected">增加</option>
                                            <option value="DECREASE" >消减</option>
                                        </select>
                                      </div>-->
                                        <div class="form-group">
                                          <label for="sysId">运营商</label>
                                          <select name="sysId" id="sysId" class="form-control selectpicker" multiple>
                                              <option value="t" selected="selected">电信</option>
                                              <option value="m" >移动</option>
                                              <option value="u" >联通</option>
                                          </select>
                                        </div>
                                        <div class="form-group">
                                          <label for="joinType">参与类型</label>
                                          <select name="zeroDel" id="joinType" class="form-control">
                                              <option value="NOT_DEL" selected="selected">持续累积</option>
                                              <option value="DEL_DAY">当日清零</option>
                                          </select>
                                        </div>

                                    <div class="form-group">
                                        <label for="duration">最少观看时长</label>
                                        <input type="text" class="form-control" id="duration" name="duration" placeholder="请填写观看时长(s)">
                                    </div>
                                    <div class="form-group">
                                        <label for="limit">参与上限</label>
                                        <input type="text" class="form-control" id="limit" name="limit" placeholder="请填写参与上限">
                                    </div>
                                    <div class="form-group">
                                        <label for="point">积分</label>
                                        <input type="text" class="form-control" id="point" name="point" placeholder="请填写积分">
                                    </div>
                                    <div class="form-group">
                                          <label for="transmissionMode">数据获取方式</label>
                                          <select name="transmissionMode" id="transmissionMode" class="form-control">
                                            <option value="FTP" selected="selected">ftp</option>
                                            <option value="INTERFACE" >接口查询</option>
                                          </select>
                                    </div>
                                    <div class="form-group">
                                      <label for="statusType">状态</label>
                                      <select name="statusType" id="statusType" class="form-control">
                                          <option value="VALID" selected="selected">有效</option>
                                          <option value="EXPIRED" >无效</option>
                                      </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="dataType">媒资内容</label>
                                        <select name="dataType" class="form-control" id="dataType">
                                            <option value="" >请选择媒资内容</option>
                                            <option value="CATEGORY" >栏目</option>
                                            <option value="BASETAG" >基础标签</option>
                                            <option value="OPTAG" >运营标签</option>
                                            <option value="COLUMN" >一级分类</option>
                                            <option value="TYPE" >类型</option>
                                            <option value="TEXT" >文本导入</option>
                                        </select>
                                    </div>
                                    <div class="form-group choose-container">
                                        <label for="dataTypeTip">先选择媒资</label>
                                        <select id="dataTypeTip" name="dataTypeTip" class="form-control">
                                            <option value="">请先选择媒资</option>
                                        </select>
                                    </div>
                                    <div class="form-group choose-container" id="chooseCate">
                                        <label for="choose-CATEGORY">栏目</label>
                                        <!--<select id="choose-CATEGORY" name="choose-CATEGORY" data-live-search="true" data-live-search-placeholder="搜索栏目"
                                                class="selectpicker form-control" multiple title="请选择栏目">
                                            <c:forEach var="category" items="${categorys}">
                                                <option value="${category.CODE}">${category.NAME}</option>
                                            </c:forEach>
                                        </select>  :load-options="loadOptions"  :searchable="false"-->
                                        <treeselect v-model="value" :multiple="true"
                                                    :options="options" :flat="true"
                                                    :placeholder="tipMsg.tip"
                                                     id="choose-CATEGORY"
                                                    :noChildrenText="tipMsg.noOption"/>
                                    </div>
                                    <div class="form-group choose-container" id="ddd">
                                        <label for="choose-BASETAG">基础标签</label>
                                        <select id="choose-BASETAG" name="choose-BASETAG" class="selectpicker form-control" data-live-search="true" data-live-search-placeholder="搜索基础标签"
                                                 multiple title="选择基础标签">
                                            <c:forEach var="item" items="${baseTag}">
                                                <option value="${item.CODE}">${item.NAME}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group choose-container">
                                        <label for="choose-OPTAG">运营标签</label>
                                        <select id="choose-OPTAG" name="choose-OPTAG" class="selectpicker form-control" data-live-search="true" data-live-search-placeholder="搜索运营标签"
                                                 multiple title="请选择运营标签">
                                            <c:forEach var="item" items="${opTag}">
                                                <option value="${item.CODE}">${item.NAME}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group choose-container">
                                        <label for="choose-TYPE">类型</label>
                                        <select id="choose-TYPE" name="choose-TYPE" class="selectpicker form-control" data-live-search="true" data-live-search-placeholder="搜索类型"
                                                 multiple title="请选择类型">
                                        <option value="2" selected="selected">直播</option>
                                        <option value="1">点播免费</option>
                                        <option value="0">点播收费</option>
                                        </select>
                                    </div>
                                    <div class="form-group choose-container">
                                        <label for="choose-COLUMN">一级分类</label>
                                        <select id="choose-COLUMN" name="choose-COLUMN" class="selectpicker form-control" data-live-search="true" data-live-search-placeholder="搜索一级分类"
                                                 multiple title="请选择一级分类">

                                        </select>
                                    </div>
                                    <input name="uuid" id="uuid" type="hidden">
                                    <input name="mediaCode" id="mediaCode" type="hidden">
                                    <input name="id" id="id" type="hidden">
                                </form>
                              </div>
                              <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                                <button type="button" id="saveBtn" class="btn btn-primary">提交</button>
                              </div>
                            <!-- content end -->

                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->
        </div>
        <!--  add modal end-->
        <!--  category modal start-->
       <div class="modal fade bs-example-modal-md" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel4" id="waitModal">
           <div class="modal-dialog modal-md" role="document">
               <div class="modal-content" style="margin-top:40%;height:100px;width:400px;">
                   <h3 class="text-center" style="margin-top: 36px;">媒资匹配中,请等待....</h3>
               </div>
           </div>
       </div>
        <!--  category modal end-->
        <!--  text modal end-->
            <div class="modal fade bs-example-modal-md" id="TEXT-ChooseModal" tabindex="-1" role="dialog" aria-labelledby="textModalLabel">
            <div class="modal-dialog modal-md" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">请选择Excel文件</h4>
                    </div>
                    <div class="modal-body">
                        <a href="${pageContext.request.contextPath}/excelTemplate/example.xls" download=""   class="form-control" style="border:none;">下载导入模板</a>
                        <input type="file" name="txt_file" id="txt_file" multiple class="file-loading" />
                        <select id="choose-TEXT" style="width:80%;display:none;">
                           <option value="" selected="selected"></option>
                           <option value="TEXT" ></option>
                       </select>
                    </div>
                </div>
            </div>
        </div>
        <!--  text modal end-->
        <!--  tag modal start-->

                <!--  tag modal end-->
                <!--  column modal start-->

                <!--  column modal end-->
                <!--  type modal start-->

                <!--  type modal end-->

    <script src="${pageContext.request.contextPath}/js/common/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/common/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/common/bootstrap-table.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/common/bootstrap-table-zh-CN.js"></script>
    <script src="${pageContext.request.contextPath}/js/laydate/laydate.js" charset="UTF-8"></script>
    <script src="${pageContext.request.contextPath}/js/common/bootbox.min.js" charset="UTF-8"></script>
    <script src="${pageContext.request.contextPath}/js/common/globa.js"></script>
    <script src="${pageContext.request.contextPath}/query/bootstrap-table-export.js"></script>
    <script src="${pageContext.request.contextPath}/plug-in/bootstrap-fileinput-master/js/fileinput.js"></script>
    <script src="${pageContext.request.contextPath}/plug-in/bootstrap-fileinput-master/js/locales/zh.js"></script>
    <!--<script src="${pageContext.request.contextPath}/js/common/chosen.jquery.min.js"></script>-->
    <script src="${pageContext.request.contextPath}/js/common/bootstrap-select.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/common/defaults-zh_CN.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vue/vue.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/vue/vue-treeselect.min.js"></script>
   <script src="${pageContext.request.contextPath}/js/vue/axios.min.js"></script>
   <script src="${pageContext.request.contextPath}/js/app/activity/table.js"></script>
   <script type = "text/javascript">
        $(function ()
        {

             Vue.component('treeselect', VueTreeselect.Treeselect);
                activityObj.cateTree = new Vue({
                  el: '#chooseCate',
                  data: {
                    // define default value
                    value: null,
                    // define options
                    options: [],
                    tipMsg :{noOption : "没有更多数据",tip:"请选择栏目"}
                  },
                  mounted:function () {
                    this.findZNodes();
                },
                methods:
                {
                    //加载修改树结构json
                    findZNodes: function ()
                    {
                        $.ajax(
                        {
                            url : "/acp/activity/cate",
                            type :"post",
                            dataType : "JSON",
                            success : function(data)
                            {
                                activityObj.cateTree.options = data;
                            }
                        });
                    },
                    loadOptions({ action, parentNode, callback })
                    {
                          if (action === 'LOAD_CHILDREN_OPTIONS')
                          {
                              $.ajax(
                                {
                                    url : "/acp/activity/cate",
                                    type :"post",
                                    dataType : "JSON",
                                    data :{pid : parentNode.id},
                                    success : function(data)
                                    {
                                        parentNode.children = data;
                                        callback();
                                    }
                                });
                          }
                    }
                }
            })


             //1.初始化Table
             var oTable = new TableInit();
             oTable.Init();

            //0.初始化fileinput
            var oFileInput = new FileInput();

            activityInit.createOption('${pageContext.request.contextPath}/data/column.json', $("#choose-COLUMN"));
            activityInit.initSelectpicker();
            oFileInput.Init("txt_file", "${pageContext.request.contextPath}/activity/excelInput");

                lay('.search-date').each(function(){
                    laydate.render({
                    elem: this,
                    trigger: 'click',
                    type: 'datetime'
                    });
                });

             $("#btn_add").off('click').on('click', activityObj.addShow);
             $("#btn_edit").off('click').on('click', activityObj.editShow);
             $("#btn_delete").off('click').on('click', activityObj.batchDel);
             $("#btn_search").off('click').on('click', activityObj.search);
             $("#btn_ftp").off('click').on('click', activityInit.showFtpUrl);
             $("#dataType").on('change', function()
             {
                var $me = $(this);
                if($me.val() == "TEXT")
                {
                    $("#TEXT-ChooseModal").modal("show");
                    $(".choose-container").hide();
                }else
                {
                    if($me.val() == "CATEGORY")
                    {
                        $("#chooseCate").show()
                     .siblings(".choose-container").hide();
                    }else
                    {
                        $("#choose-" + $me.val()).parents(".choose-container").show()
                     .siblings(".choose-container").hide().end().end().selectpicker('refresh');
                    }

                }

             });

             $('#addModal').on('shown.bs.modal', function (e)
             {
                   if(!$.isEmptyObject(activityObj.selectRow)){
                        $("#expireTime").val(activityObj.selectRow.expireTime);
                        $("#validTime").val(activityObj.selectRow.validTime);
                   }
                   laydate.render(
                   {
                     elem: '#expireTime',
                     type: 'datetime',
                     format: 'yyyy-MM-dd HH:mm:ss',
                     //value: $.isEmptyObject(activityObj.selectRow) ?  '' : activityObj.selectRow.expireTime
                     value: ''
                   });
                   laydate.render(
                   {
                     elem: '#validTime',
                     type: 'datetime',
                     format: 'yyyy-MM-dd HH:mm:ss',
                     value: ''
                   });

                 $("#saveBtn").off('click').on('click',activityObj.add);
             })
         });



/*
    function operateFormatter(value, row, index) {//赋予的参数
        return [
            '<a class="btn active disabled" href="#">编辑</a>',
            '<a class="btn active" href="#">档案</a>',
            '<a class="btn btn-default" href="#">记录</a>',
            '<a class="btn active" href="#">准入</a>'
        ].join('');
    }
*/
    function msgAlert(msg)
    {
        bootbox.alert({
                        size: "small",
                        title: "操作提示",
                        message: msg,
                        callback: function(){ /* your callback code */ }
                    });
    }



    var activityInit =
    {
        initSelectpicker : function()
        {
            $(".choose-container>.selectpicker").each(function(index,element)
            {
                if(index > 0){
                    var $me = $(element);
                    $me.parent().hide().end().selectpicker('refresh');
                }
            });
        },

        //dialogWait: bootbox.dialog({
                //message: '<p class="text-center mb-0"><i class="fa fa-spin fa-cog"></i> 媒资匹配中，请等待</p>',
                //closeButton: false
            //}),

//dialog.modal('hide');

        showFtpUrl: function()
        {
            $.ajax(
                {
                    url : "${pageContext.request.contextPath}/activity/ftp",
                    type :"get",
                    dataType : "JSON",
                    success : function(data)
                    {
                        if(data.success)
                        {
                            var dialog = bootbox.dialog({
                                title: 'ftp地址',
                                message: "<textarea id='ftpUrl' style='border:none;outline:none;'>"+data.message+"</textarea>",
                                size: 'middle',
                                buttons: {
                                    ok: {
                                        label: "复制",
                                        className: 'btn-success',
                                        callback: function(){
                                            var temp = document.getElementById("ftpUrl");
                                            temp.select();
                                            document.execCommand("Copy");

                                            msgAlert("复制成功");
                                        }
                                    },
                                     noclose: {
                                        label: "前往",
                                        className: 'btn-info',
                                        callback: function(){
                                           window.open(data.message);
                                        }
                                    }
                                }
                            });
                        }
                    }
                });

        },

        createOption : function(url, container)
        {
            $.getJSON(url, function(result)
            {
                $.each(result, function(i, value)
                {
                    var $op = $("<option></option>");
                    $op.val(value.id);
                    $op.text(value.text);
                    container.append($op);
                });
              });
        }
    };

    var activityObj =
    {
        dataTypeObj:
        {
            "CATEGORY": "栏目",
            "BASETAG": "基础标签",
            "OPTAG": "运营标签",
            "TEXT": "文本导入",
            "COLUMN": "一级分类",
            "TYPE": "类型"
        },
        closeAddModal : function()
        {
            $("#addModal").removeData("bs.modal");
        },
        initAddForm : function()
        {
            $("input").val("");


            activityObj.clearSelects();
            //activityInit.initSelectpicker();
            $('select').not(".selectpicker").prop('selectedIndex', 0);
            chooseObj.uuid = chooseObj.generateUUID();
            activityObj.selectRow = {};
            $("#chooseCate").hide();
            $(".choose-container").hide();
            $("#dataTypeTip").parent().show();
            $("#add-modal-id").text("新增活动");
        },
        addShow : function()
        {
            activityObj.initAddForm();
            activityObj.loadActiId();
             // 打开模态框
            $("#addModal").modal();

        },
        loadActiId: function()
        {
            $.ajax
                ({
                    url: '${pageContext.request.contextPath}/activity/get_acti_id',
                    type:'get',
                    dataType:"json",
                    success: function(data)
                    {
                        if(data.success)
                        {
                            $("#activityId").val(data.message);
                        }
                    }
                });
        },
        batchDel : function()
        {
            var rows = $('#ArbetTable').bootstrapTable('getSelections');
            if(rows.length == 0)
            {
                msgAlert("请选中要删除的记录!");
                return;
            }
            var actiNames = "";
            var actiIds = [];
            for(var i in rows)
            {
                actiNames += rows[i].activityId + ",";
                actiIds.push(rows[i].activityId);
            }
            bootbox.confirm({
                title: "操作提示",
                message: "确定要删除活动["+actiNames.substr(0, actiNames.length -1)+"]?",
                buttons: {
                    confirm: {
                        label: '删除',
                        className: 'btn-danger'
                    },
                    cancel: {
                        label: '取消',
                        className: 'btn-success'
                    }
                },
                callback: function (result) {
                    if(result)
                    {
                         $.ajax
                        ({
                            url: "${pageContext.request.contextPath}/activity/del",
                            type:'post',
                            data:{ids: actiIds},
                            traditional: true,
                            dataType:"json",
                            success: function(data)
                            {
                                if(data.success)
                                {
                                    $('#addModal').modal('hide');
                                    $('#ArbetTable').bootstrapTable('refresh');
                                }
                                msgAlert(data.message);
                            }
                        });
                    }
                }
            });
        },
        cateTree: {},
        clearSelects: function()
        {
            $('.selectpicker').selectpicker('val','');
            $('.selectpicker').selectpicker('refresh');
            activityObj.cateTree.value=[];
        },
        add : function()
        {
            var dataType = $("#dataType").val();
            console.info(dataType);
            if(!dataType)
            {
                msgAlert("请选择媒资内容");
                return;
            }
            var isSelected = $("#choose-" + dataType).val();
            if(dataType != 'TEXT'){
                if(dataType == 'CATEGORY')
                {
                    if(!activityObj.cateTree.value)
                    {
                        $("#mida")
                        msgAlert("请选择具体媒资");
                        return;
                    }
                    $("#mediaCode").val(activityObj.cateTree.value.join(","));
                }else
                {
                    if(!isSelected)
                    {
                        msgAlert("请选择具体媒资");
                        return;
                    }
                    $("#mediaCode").val(isSelected);
                }


            }

            //$("#uuid").val(chooseObj.uuid);
            var url = $("#id").val() ? "${pageContext.request.contextPath}/activity/edit" : "${pageContext.request.contextPath}/activity/add"
            $.ajax
            ({
                url: url,
                type:'post',
                data:$("#addForm").serialize(),
                dataType:"json",
                beforeSend : function(xhr){
                    $("#waitModal").modal({
                        backdrop: "static",
                        keyboard: false
                    });
                },
                success: function(data)
                {
                    $("#waitModal").modal("hide");
                    if(data.success)
                    {
                        $('#addModal').modal('hide');
                        $('#ArbetTable').bootstrapTable('refresh');
                    }
                    msgAlert(data.message);
                },
                error: function(){
                    $("#waitModal").modal("hide");
                }
            });
        },
        editShow : function()
        {
            var rows = $('#ArbetTable').bootstrapTable('getSelections');
            if(rows.length != 1)
            {
                msgAlert("请选中一行记录操作!");
                return;
            }
            var row = rows[0];
            activityObj.selectRow = row;
            console.info($.isEmptyObject(activityObj.selectRow) ?  '' : activityObj.selectRow.validTime);
            activityObj.initEditForm(row);
        },
        initEditForm : function(row)
        {
            activityObj.clearSelects();
            $("#id").val(row.id);
            $("#activityId").val(row.activityId);
            $("#activityName").val(row.activityName);
            $("#statusType").find("option[value="+row.statusType+"]").attr("selected",true).siblings().removeAttr("selected");
            $("#joinType").find("option[value="+row.zeroDel+"]").attr("selected",true).siblings().removeAttr("selected");
            $("#dataType").find("option[value="+row.dataType+"]").prop("selected",true).siblings().removeAttr("selected");
            console.info(row.dataType);
            $("#duration").val(row.duration);
            $("#limit").val(row.limit);
            $("#point").val(row.point);
            $("#transmissionMode").find("option[value="+row.transmissionMode+"]").attr("selected",true).siblings().removeAttr("selected");
            $("#mediaCode").val(row.mediaCode);
            console.info(row.sysId);
            $("#dataTypeTip").parent().hide();
            $("#sysId").selectpicker('val', row.sysId.split(","));
            if(row.dataType == "TEXT")
            {
                //$("#choose-" + row.dataType).find("option[value="+row.dataType+"]").attr("selected",true).siblings().removeAttr("selected");
                $("#dataTypeTip").parent().show().siblings(".choose-container").hide();
                $(".choose-container").hide();
            }else
            {
                var $me = $("#choose-" + row.dataType);
                $me.parents(".choose-container").show()
                     .siblings(".choose-container").hide();
                if(row.dataType == "CATEGORY")
                {
                    $("#chooseCate").show().siblings(".choose-container").hide();
                    activityObj.cateTree.value = row.mediaCode.split(",");
                }else
                {
                console.info($me);
                console.info(row.mediaCode.split(","));
                    $me.selectpicker('val', row.mediaCode.split(","));
                }
            }
            chooseObj.uuid = chooseObj.generateUUID();
            $("#add-modal-id").text("修改活动");
            $("#addModal").modal();
        },
        search : function()
        {
            $('#ArbetTable').bootstrapTable('refresh');
        },
        selectRow : {}
    }

    var chooseObj =
    {
        chooseStatus : false,
        showChooseModal : function(chooseType)
        {
                switch(chooseType)
                {
                    case "CATEGORY"://栏目
                        $("#CATEGORY-ChooseModal").modal({"backdrop": false});
                        $('#CATEGORY-ChooseModal').on('shown.bs.modal', function (e)
                          {
                            if(!chooseObj.chooseStatus)
                            {
                                $('.chosen-select').chosen(
                                {
                                    no_results_text: "没有找到结果！",//搜索无结果时显示的提示
                              search_contains:true,   //关键字模糊搜索。设置为true，只要选项包含搜索词就会显示；设置为false，则要求从选项开头开始匹配
                             allow_single_deselect:true, //单选下拉框是否允许取消选择。如果允许，选中选项会有一个x号可以删除选项
                            disable_search: false, //禁用搜索。设置为true，则无法搜索选项。
                            disable_search_threshold: 0, //当选项少等于于指定个数时禁用搜索。
                            inherit_select_classes: true, //是否继承原下拉框的样式类，此处设为继承
                            //placeholder_text_single: "请选择栏目",
                            case_sensitive_search: false, //搜索大小写敏感。此处设为不敏感
                            group_search: false, //选项组是否可搜。此处搜索不可搜
                            include_group_label_in_selected: true //选中选项是否显示选项分组。false不显示，true显示。默认false。
                                });
                                    chooseObj.chooseStatus = true;
                            }
                                $(".chosen-select").trigger("chosen:updated");
                          });
                        break;
                    case "TAG"://标签
                        $("#TAG-ChooseModal").modal({"backdrop": false});
                        break;
                    case "COLUMN"://一级分类
                        chooseObj.loadColumnCode();
                        $("#COLUMN-ChooseModal").modal({"backdrop": false});
                        break;
                    case "TYPE"://类型
                        $("#TYPE-ChooseModal").modal({"backdrop": false});
                        break;
                    case "TEXT"://文本导入
                        $("#TEXT-ChooseModal").modal({"backdrop": false});
                        break;
                }
        },
        columnCode : [
                        {code : "001", name : "电影"},
                        {code : "002", name : "电视剧"},
                        {code : "003", name : "新闻"},
                        {code : "004", name : "少儿"},
                        {code : "005", name : "综艺"},
                        {code : "006", name : "体育"},
                        {code : "007", name : "纪实"},
                        {code : "008", name : "生活"},
                        {code : "009", name : "财经"}
                      ],
        loadColumnCode : function()
            {
                var columnContainer = $("#choose-COLUMN");
                if(columnContainer.children("option").length > 1)
                {
                    return;
                }
                var columnCode = this.columnCode;
                for(var i in columnCode)
                {
                    var $op = $("<option></option>");
                    $op.val(columnCode[i].code);
                    $op.text(columnCode[i].name);
                    columnContainer.append($op);
                }
            },
         generateUUID : function()
         {
            var romdomStr = Math.random().toString();
            return new Date().getTime() +  romdomStr.substr(romdomStr.length-2, 2);
         },
         uuid : ''
    }

    </script>

  </body>
</html>