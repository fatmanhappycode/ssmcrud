// 首页获取员工列表
// emps?pn=1
var totalRecord,currentPage;
function to_Page(pn) {
    $.ajax({
        url:APP_PATH+"/emps",
        data:"pn=" + pn,
        type:"GET",
        success:function (result) {
            // 解析并显示员工数据
            build_emps_table(result);
            // 解析并显示分页信息
            build_page_info(result);
            // 解析并显示分页条
            build_page_nav(result);
        }
    })
}


// 加载时去首页
$(function () {
    to_Page(1);
});


// 解析并显示员工数据
function build_emps_table(result) {
    // 清空
    $("#emps_table tbody").empty();
    var emps = result.extend.pageInfo.list;
    $.each(emps,function (index, item) {
        var checkBoxTd = $("<td><input type='checkbox' class='check_item'></td>");
        var empIdTd = $("<td></td>").append(item.empId);
        var empNameTd = $("<td></td>").append(item.empName);
        var genderTd = $("<td></td>").append(item.gender == 'M'?"男":"女");
        var emailTd = $("<td></td>").append(item.email);
        var deptNameTd = $("<td></td>").append(item.department.deptName);
        var editBtn = $("<button></button>").addClass("btn btn-primary btn-sm edit_btn")
            .append($("<span></span>").addClass("glyphicon glyphicon-pencil"))
            .append("编辑");
        editBtn.attr("edit-id",item.empId);
        var delBtn = $("<button></button>").addClass("btn btn-danger btn-sm delete_btn")
            .append($("<span></span>").addClass("glyphicon glyphicon-trash"))
            .append("删除");
        delBtn.attr("del-id",item.empId);
        var btnTd = $("<td></td>").append(editBtn).append(" ").append(delBtn);
        $("<tr></tr>")
            .append(checkBoxTd)
            .append(empIdTd)
            .append(empNameTd)
            .append(genderTd)
            .append(emailTd)
            .append(deptNameTd)
            .append(btnTd)
            .appendTo("#emps_table tbody")
    });
}


// 解析并显示分页信息
function build_page_info(result) {
    // 清空
    $("#page_info_area").empty();
    $("#page_info_area").append("当前" + result.extend.pageInfo.pageNum + "页，" +
        "总" + result.extend.pageInfo.pages + "页，" +
        "总" + result.extend.pageInfo.total + "条记录");
    totalRecord = result.extend.pageInfo.total;
    currentPage = result.extend.pageInfo.pageNum;
}


// 解析并显示分页条
function build_page_nav(result) {
    // 清空
    $("#page_nav_area").empty();
    var navEle = $("<nav></nav>");
    var ul = $("<ul></ul>").addClass("pagination");
    var firstPageLi = $("<li></li>").append($("<a></a>").append("首页").attr("href","#"));
    var prePageLi = $("<li></li>").append($("<a></a>").append("&laquo;"));
    var nextPageLi = $("<li></li>").append($("<a></a>").append("&raquo;"));
    var lastPageLi = $("<li></li>").append($("<a></a>").append("末页").attr("href","#"));

    if (result.extend.pageInfo.hasPreviousPage == false) {
        firstPageLi.addClass("disabled");
        prePageLi.addClass("disabled");
    } else {
        firstPageLi.click(function () {
            to_Page(1);
        });
        prePageLi.click(function () {
            to_Page(result.extend.pageInfo.pageNum - 1);
        })
    }
    ul.append(firstPageLi).append(prePageLi);

    //遍历页码号
    $.each(result.extend.pageInfo.navigatepageNums,function (index, item) {
        var numLi = $("<li></li>").append($("<a></a>").append(item));
        if (result.extend.pageInfo.pageNum == item) {
            numLi.addClass("active");
        }
        numLi.click(function () {
            to_Page(item);
        });
        ul.append(numLi);
    });
    if (result.extend.pageInfo.hasNextPage == false) {
        nextPageLi.addClass("disabled");
        lastPageLi.addClass("disabled");
    } else {
        nextPageLi.click(function () {
            to_Page(result.extend.pageInfo.pageNum + 1)
        });
        lastPageLi.click(function () {
            to_Page(result.extend.pageInfo.pages)
        });
    }
    ul.append(nextPageLi).append(lastPageLi);
    navEle.append(ul).appendTo("#page_nav_area");
}

// 绑定新增按钮的模态框
$(document).on("click","#emp_add_modal_btn",function () {
    reset_form("#empAddModal form");
    getDepts("#empAddModal select");
    $("#empAddModal").modal({
        backdrop:"static"
    });
});

// 重置表单
function reset_form(ele) {
    $(ele)[0].reset();
    $(ele).find("*").removeClass("has-error has-success");
    $(ele).find(".help-block").text("");

}

//显示所有部门信息并显示在下拉列表
function getDepts(ele) {
    $(ele).empty();
    $.ajax({
        url:APP_PATH+"/depts",
        type:"GET",
        success:function (result) {
            $.each(result.extend.depts,function () {
                var optionEle = $("<option></option>").append(this.deptName).attr("value",this.deptId);
                optionEle.appendTo(ele);
            });
        }
    });
}

// 添加员工表单验证
function validate_add_form() {
    // var empName = $("#empName_add_input").val();
    // var regName = /(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})/;
    // if(!regName.test(empName)) {
    //     show_validate_msg("#empName_add_input","error","用户名必须是2-5位中文或者6-16位英文和数字的组合");
    //     return false;
    // } else {
    //     show_validate_msg("#empName_add_input","success","");
    // }
    var email = $("#email_add_input").val();
    var regEmail = /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
    if(!regEmail.test(email)) {
        show_validate_msg("#email_add_input","error","邮箱格式不正确");
        return false;
    } else {
        show_validate_msg("#email_add_input","success","");
    }
    return true;
}

// 显示校验成功或失败的信息
function show_validate_msg(ele,status,msg) {
    $(ele).parent().removeClass("has-success has-error");
    $(ele).next("span").text("");
    if ("success" == status) {
        $(ele).parent().addClass("has-success");
        $(ele).next("span").text(msg);
    } else if ("error" == status) {
        $(ele).parent().addClass("has-error");
        $(ele).next("span").text(msg);
    }
}

// 保存按钮事件
$(document).on("click","#emp_save_btn",function () {
    if (!validate_add_form()) {
        return false;
    }
    if ($(this).attr("ajax-va") == "error") {
        return false;
    }
    $.ajax({
        url:APP_PATH+"/emp",
        type:"POST",
        data:$("#empAddModal form").serialize(),
        success:function (result) {
            if (result.code == 200) {
                $("#empAddModal").modal('hide');
                to_Page(totalRecord);
            } else {
                if (undefined != result.extend.errorFields.email) {
                    show_validate_msg("#email_add_input", "error", result.extend.errorFields.email);
                }
                if (undefined != result.extend.errorFields.empName) {
                    show_validate_msg("#email_add_input", "error", result.extend.errorFields.empName);
                }
            }
        }
    })
});

// ajax校验用户名
$(document).on("change","#empName_add_input",function () {
    var empName = this.value;
    $.ajax({
        url:APP_PATH+"/checkuser",
        data:"empName="+empName,
        type:"POST",
        success:function (result) {
            if (result.code == 200) {
                show_validate_msg("#empName_add_input","success","用户名可用");
                $("#emp_save_btn").attr("ajax-va","success");
            }  else {
                show_validate_msg("#empName_add_input","error",result.extend.va_msg);
                $("#emp_save_btn").attr("ajax-va","error");
            }
        }
    })
});

//点击编辑按钮打开模态框
$(document).on("click",".edit_btn",function () {
    //查出部门信息显示部门信息
    getDepts("#empUpdateModal select");
    // 查出员工信息显示员工信息
    getEmp($(this).attr("edit-id"));
    // 把员工id传递到更新按钮
    $("#emp_update_btn").attr("edit-id",$(this).attr("edit-id"));
    //打开模态框
    $("#empUpdateModal").modal({
        backdrop:"static"
    });
});

// 查出员工信息显示员工信息
function getEmp(id) {
    $.ajax({
        url:APP_PATH+"/emp/"+id,
        type:"GET",
        success:function (result) {
            var empData = result.extend.emp;
            $("#empName_update_static").text(empData.empName);
            $("#email_update_input").val(empData.email);
            $("#empUpdateModal input[name=gender]").val([empData.gender]);
            $("#empUpdateModal select").val([empData.dId]);
        }
    });
}

//点击更新，更新员工信息
$(document).on("click","#emp_update_btn",function () {
    //验证邮箱是否合法
    var email = $("#email_update_input").val();
    var regEmail = /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
    if(!regEmail.test(email)) {
        show_validate_msg("#email_update_input","error","邮箱格式不正确");
        return false;
    } else {
        show_validate_msg("#email_update_input","success","");
    }
    $.ajax({
        url:APP_PATH+"/emp/"+$(this).attr("edit-id"),
        type:"PUT",
        data:$("#empUpdateModal form").serialize(),
        success:function (result) {
            $("#empUpdateModal").modal("hide");
            to_Page(currentPage);
        }
    })
});

// 单个删除
$(document).on("click",".delete_btn",function () {
    var empName = $(this).parents("tr").find("td:eq(2)").text();
    var empId = $(this).attr("del-id");
    if (confirm("确认删除【"+empName+"】吗？")) {
        $.ajax({
            url:APP_PATH+"/emp/"+empId,
            type:"DELETE",
            success:function (result) {
                alert(result.msg);
                to_Page(currentPage);
            }
        });
    }
});

// 全选按钮
$(document).on("click","#check_all",function () {
   $(".check_item").prop("checked",$(this).prop("checked"));
});

// 全选时全选
$(document).on("click",".check_item",function () {
    var flag = $(".check_item:checked").length == $(".check_item").length;
    $("#check_all").prop("checked",flag);
});

//点击批量删除
$(document).on("click","#emp_delete_all_btn",function () {
    var empNames = "";
    var del_idstr = "";
    $.each($(".check_item:checked"),function () {
        empNames += $(this).parents("tr").find("td:eq(2)").text() + ",";
        del_idstr += $(this).parents("tr").find("td:eq(1)").text() + "-";
    });
    empNames = empNames.substring(0,empNames.length-1);
    del_idstr = del_idstr.substring(0,del_idstr.length-1);
    if (confirm("确认删除【" + empNames + "】吗？")) {
        $.ajax({
            url:APP_PATH+"/emp/" + del_idstr,
            type:"DELETE",
            success:function (result) {
                alert(result.msg);
                to_Page(currentPage);
            }
        })
    }
});

