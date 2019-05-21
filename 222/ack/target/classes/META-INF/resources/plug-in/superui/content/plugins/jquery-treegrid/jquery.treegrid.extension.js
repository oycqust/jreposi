/**
 * Created by UTSC0885 on 2017/5/6.
 */


(function ($) {
    "use strict";
    var backupOptions;

    $.fn.treegridData = function (options, param) {
        //如果是调用方法
        console.log('1=' + new Date());
        if (typeof options == 'string') {
            return $.fn.treegridData.methods[options](this, param);
        }
        backupOptions = options;
        //如果是初始化组件
        options = $.extend({}, $.fn.treegridData.defaults, options || {});
        var target = $(this);
        //得到根节点
        target.getRootNodes = function (data) {
            var result = [];
            $.each(data, function (index, item) {
                if (!item[options.parentColumn]) {
                    result.push(item);
                }
            });
            return result;
        };
        var j = 10000;
        //递归获取子节点并且设置子节点
        target.getChildNodes = function (data, parentNode, parentIndex, tbody) {
            $.each(data, function (i, item) {
                if (item[options.parentColumn] == parentNode[options.id]) {
                    var tr = $('<tr></tr>');

                    var id;
                    var nowParentIndex = parentIndex + '' + i;
                    id = Number(nowParentIndex);

                    tr.addClass('treegrid-' + nowParentIndex);
                    tr.addClass('treegrid-parent-' + parentIndex);
                    $.each(options.columns, function (index, column) {
                        var td = $('<td></td>');
                        if (column.formatter) {
                            td.html(column.formatter(item[column.field], item));
                        } else {
                            td.text(item[column.field]);
                        }
                        tr.append(td);
                    });
                    tbody.append(tr);
                    target.getChildNodes(data, item, id, tbody);

                }
            });
        };
        target.addClass('table');
        if (options.striped) {
            target.addClass('table-striped');
        }
        if (options.bordered) {
            target.addClass('table-bordered');
        }
        if (options.url) {
            console.log('2=' + new Date());
            $.ajax({
                type: options.type,
                url: options.url,
                data: options.ajaxParams,
                dataType: "JSON",
                success: function (data, textStatus, jqXHR) {
                    //构造表头
                    console.log('3=' + new Date());
                    var thr = $('<tr></tr>');
                    $.each(options.columns, function (i, item) {
                        var th = $('<th style="padding:10px;"></th>');
                        th.text(item.title);
                        thr.append(th);
                    });
                    var thead = $('<thead></thead>');
                    thead.append(thr);
                    target.append(thead);

                    //构造表体
                    var tbody = $('<tbody></tbody>');
                    var rootNode = target.getRootNodes(data);
                    $.each(rootNode, function (i, item) {
                        var tr = $('<tr></tr>');
                        tr.addClass('treegrid-' + j);
                        $.each(options.columns, function (index, column) {
                            var td = $('<td></td>');
                            if (column.formatter) {
                                td.html(column.formatter(item[column.field], item));
                            } else {
                                td.text(item[column.field]);
                            }
                            tr.append(td);
                        });
                        tbody.append(tr);

                        target.getChildNodes(data, item, j, tbody);
                        j++;
                    });
                    target.append(tbody);
                    console.log('4=' + new Date());
                    target.treegrid({
                        expanderExpandedClass: options.expanderExpandedClass,
                        expanderCollapsedClass: options.expanderCollapsedClass
                    });
                    if (!options.expandAll) {
                        target.treegrid('collapseAll');
                    }
                    if (options.gridComplete) {
                        options.gridComplete();
                    }
                    console.log('5=' + new Date());
                }
            });
        }
        else {
            //也可以通过defaults里面的data属性通过传递一个数据集合进来对组件进行初始化....有兴趣可以自己实现，思路和上述类似
        }
        return target;
    };

    $.fn.treegridData.methods = {
        getAllNodes: function (target, data) {
            return target.treegrid('getAllNodes');
        },
        refresh: function (target, data) {
            if (typeof backupOptions == 'undefined')
                return;
            target.empty();
            target.treegridData(backupOptions);
        }
        //组件的其他方法也可以进行类似封装........
    };

    $.fn.treegridData.defaults = {
        id: 'Id',
        parentColumn: 'ParentId',
        data: [],    //构造table的数据集合
        type: "GET", //请求数据的ajax类型
        url: null,   //请求数据的ajax的url
        ajaxParams: {}, //请求数据的ajax的data属性
        expandColumn: null,//在哪一列上面显示展开按钮
        expandAll: true,  //是否全部展开
        striped: false,   //是否各行渐变色
        bordered: false,  //是否显示边框
        columns: [],
        gridComplete: null,
        expanderExpandedClass: 'glyphicon glyphicon-chevron-down',//展开的按钮的图标
        expanderCollapsedClass: 'glyphicon glyphicon-chevron-right'//缩起的按钮的图标
    };
})(jQuery);