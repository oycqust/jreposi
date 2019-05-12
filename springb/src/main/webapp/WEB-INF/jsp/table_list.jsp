<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Hello, Bootstrap Table!</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap-table.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap-datetimepicker.min.css">

  </head>
  <body>
   <table id="ArbetTable"></table>
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
            </div>
            <table id="tb_departments"></table>
            <div class="modal fade" style="top:13%;"  tabindex="-1" role="dialog" id="showModal">
                      <div class="modal-dialog" role="document">
                          <div class="modal-content">
                            <!-- 内容会加载到这里 -->
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->
        </div>
    <script src="${pageContext.request.contextPath}/static/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/bootstrap-table.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/modal.js""></script>
    <script src="${pageContext.request.contextPath}/static/js/bootstrap-datetimepicker.min.js" ></script>
    <script src="${pageContext.request.contextPath}/static/js/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
    <script src="${pageContext.request.contextPath}/static/js/laydate.js" charset="UTF-8"></script>

    <script type = "text/javascript">
        $(function () {
             //1.初始化Table
             var oTable = new TableInit();
             oTable.Init();

             $("#btn_add").click(function(){
                     // 打开模态框
                     $("#showModal").modal({
                         backdrop: 'static',     // 点击空白不关闭
                         keyboard: false,        // 按键盘esc也不会关闭
                         remote: '/table/add'    // 从远程加载内容的地址
                     });
                 });

                 $('#showModal').on('shown.bs.modal', function (e) {
                 laydate.render({
                   elem: '#datetimepicker1',//指定元素
                   type: 'datetime'
                 });
                    return;
                   $('#datetimepicker1').datetimepicker({
                           format: 'yyyy-mm-dd hh:mm:ss',
                           language: 'zh-CN',
                           todayBtn: 1,
                           minuteStep:1
                       });
                   $('#datetimepicker1').on('changeDate', function (ev)
                   {

                   console.info(ev);
                   var ss = new Date();
                   ss.setTime(ev.timeStamp);
                   console.info(ss.getFullYear()+"-"+ss.getMonth()+"-"+ss.getDate()+" "+ss.getHours()+":"+ss.getMinutes()+":"+ss.getSeconds());
                   $("#st").val(ss.getFullYear()+"-"+ss.getMonth()+"-"+ss.getDate()+" "+ss.getHours()+":"+ss.getMinutes()+":"+ss.getSeconds());
                   });
                   $("#saveBtn").on('click',function()
                   {
                    $.ajax({
                        url:'/table/add',
                        type:'post',
                        data:$("#addForm").serialize()
                    });
                   });
                 })
         });

    var TableInit = function () {
        var oTableInit = new Object();
        //初始化Table
        oTableInit.Init = function () {
            $('#ArbetTable').bootstrapTable({
                url: '/table/data',         //请求后台的URL（*）
                method: 'get',                      //请求方式（*）
                toolbar: '#toolbar1',                //工具按钮用哪个容器
                striped: true,                      //是否显示行间隔色
                cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                pagination: true,                   //是否显示分页（*）
                sortable: false,                     //是否启用排序
                sortOrder: "asc",                   //排序方式
                queryParams: oTableInit.queryParams,//传递参数（*）
                sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
                pageNumber: 1,                       //初始化加载第一页，默认第一页
                pageSize: 10,                       //每页的记录行数（*）
                pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
                search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
                contentType: "application/x-www-form-urlencoded",
                strictSearch: true,
                showColumns: true,                  //是否显示所有的列
                showRefresh: true,                  //是否显示刷新按钮
                minimumCountColumns: 2,             //最少允许的列数
                clickToSelect: true,                //是否启用点击选中行
                height: 700,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
                uniqueId: "no",                     //每一行的唯一标识，一般为主键列
                showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
                cardView: false,                    //是否显示详细视图
                detailView: false,                   //是否显示父子表
                columns: [
                {
                    field: 'id',
                    title: 'ID'
                }, {
                    field: 'startTime',
                    title: '开始时间'
                }, {
                    field: 'status',
                    title: '状态'
                },
                {
                    field: 'operate',
                    title: '操作',
                    formatter: operateFormatter //自定义方法，添加操作按钮
                },
                ],
                rowStyle: function (row, index) {
                    var classesArr = ['success', 'info'];
                    var strclass = "";
                    if (index % 2 === 0) {//偶数行
                        strclass = classesArr[0];
                    } else {//奇数行
                        strclass = classesArr[1];
                    }
                    return { classes: strclass };
                },//隔行变色
            });

        };


        //得到查询的参数
        oTableInit.queryParams = function (params) {
            var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                limit: params.limit,   //页面大小
                offset:params.offset
            };
            return temp;
        };
        return oTableInit;
    };


    function operateFormatter(value, row, index) {//赋予的参数
        return [
            '<a class="btn active disabled" href="#">编辑</a>',
            '<a class="btn active" href="#">档案</a>',
            '<a class="btn btn-default" href="#">记录</a>',
            '<a class="btn active" href="#">准入</a>'
        ].join('');
    }
    </script>
  </body>
</html>