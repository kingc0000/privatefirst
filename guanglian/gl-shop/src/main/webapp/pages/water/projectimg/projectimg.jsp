<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<script src='<c:url value="/resources/js/projectimg/jquery.mousewheel.min.js" />'></script>
<script src='<c:url value="/resources/js/projectimg/hammer.min.js" />'></script>
<script src='<c:url value="/resources/js/projectimg/zoom-marker.js" />'></script>
<link href='<c:url value="/resources/js/projectimg/zoom-marker.css" />' rel="stylesheet">
<link href='<c:url value="/resources/css/style.css" />' rel="stylesheet">
<script type="text/javascript">
    //设置title
    var pageTitle = "图纸标记";
    $('#pictitle').html(pageTitle + '管理');
    $('#panel-heading').html(pageTitle);
</script>
<style>
    * {
        margin: 0;
        padding: 0;
    }

    #markernames {
        width: 80px;
        position: absolute;
        display: none;
        z-index: 1;
    }

    #menu {
        width: 100px;
        position: absolute;
        display: none;
        z-index: 1;
    }

    #menu ul {
        width: 100%;
        list-style: none;
        border: 2px solid #000000;
        border-bottom: 0px;
    }

    #menu ul li {
        height: 30px;
        font-size: 16px;
        color: #ccaf04;
        line-height: 30px;
        text-align: center;
        border-bottom: 2px solid #000000;
    }

    select {
        width: 80px;
        height: 20px;
    }
</style>
<script type="text/javascript">
    $(document).ready(function () {
        var markers = [];
        <c:if test="${not empty projectImgs[0].pMarkers}">
        <c:forEach items="${projectImgs[0].pMarkers}" var="m" >
        var icon = '';
        <c:if test="${m.id == 1}">
        icon = ' well-icon dewell grey'
        </c:if>
        <c:if test="${m.id == 2}">
        icon = ' well-icon dewell red'
        </c:if>
        var marker = {
            icon: icon, x: ${m.markerX}, y: ${m.markerY}, dialog: {
                value: ${m.well.name},
                offsetX: 20,
                style: {
                    "border-color": "#ff8355"
                }
            }
        };
        markers.push(marker)
        </c:forEach>
        </c:if>
        $('#zoom-marker-img').zoomMarker({
            src: "${projectImgs[0].url}",
            rate: 0.2,
            markers: markers
        });
    })
    window.onclick = function (e) {
        var menu = document.getElementById("menu");
        menu.style.display = "none";
    }
    var rightClickX, rightClickY;
    var rightClickPageX, rightClickPageY;
    var clientX, clientY;
    var rightClickPosition;
    var welldatas;
    $(function () {
        $.post('/water/csite/getWarning.shtml', {"pid":${pid}}, function (datas) {
            welldatas = datas;
            $.each(datas.pwell, function (index, pwell) {
                $("#wellselect").append("<option value='pwell" + pwell.id + "'>" + pwell.name + "</option>");
            })
        })
        $('#zoom-marker-img').on("contextmenu", function (ev) {
            var markernames = document.getElementById("markernames");
            markernames.style.display = "none";
            var menu = document.getElementById("menu");
            var ev = ev || event;
            var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
            menu.style.display = "block";
            menu.style.left = ev.clientX + "px";
            //当滑动滚动条时也能准确获取菜单位置
            menu.style.top = ev.clientY + scrollTop + "px";
            //阻止默认事件
            return false;
        })
        $('#zoom-marker-img').on("zoom_marker_click", function (event, marker) {
            console.log(JSON.stringify(marker));
            if (confirm("确定要删除" + marker.param.dialog.value + "标记吗？")) {
                $('#zoom-marker-img').zoomMarker_RemoveMarker(marker.id);
            }
        });
        $("#addMarker").on('click', function (e) {
            $('#zoom-marker-img').trigger("zoom_marker_mouse_click", {
                pageX: rightClickPageX,
                pageY: rightClickPageY,
                x: rightClickX,
                y: rightClickY
            });
            var menu = document.getElementById("menu");
            menu.style.display = "none";
            var markernames = document.getElementById("markernames");
            var ev = e || event;
            var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
            markernames.style.display = "block";
            markernames.style.left = clientX + "px";
            //当滑动滚动条时也能准确获取菜单位置
            markernames.style.top = clientY + scrollTop + "px";
            //阻止默认事件
            var markerList = $('#zoom-marker-img').zoomMarker_markerList();
            $.each(markerList, function (index, e) {
                console.log(index + ": " + e.param.x + "=====" + e.param.y)
            })
        })
        $('#zoom-marker-img').mousedown(function (e) {
            var options = $('#zoom-marker-img').zoomMarker_options();
            var that = $('#zoom-marker-img');
            var offset = that.offset();
            if (e.which == 3) {
                if (typeof(e.clientX) === 'undefined') {
                    rightClickX = (e.pageX - offset.left) / that.width() * options.imgNaturalSize.width,
                        rightClickY = (e.pageY - offset.top) / that.height() * options.imgNaturalSize.height
                } else {
                    rightClickPageX = e.offsetX
                    rightClickPageY = e.offsetY
                    rightClickX = (e.pageX - offset.left) / that.width() * options.imgNaturalSize.width,
                        rightClickY = (e.pageY - offset.top) / that.height() * options.imgNaturalSize.height
                }
                clientX = e.clientX;
                clientY = e.clientY;
            }
        })
        $("#zoom-marker-img").on("zoom_marker_mouse_click", function (event, position) {
            console.log("Mouse click on: " + JSON.stringify(position));
            rightClickPosition = position;
        });
        $("#wellselect").change(function () {
            var markerList = $('#zoom-marker-img').zoomMarker_markerList();
            $(markerList).each(function (index, element) {
                if (element.param.x == rightClickPosition.x && element.param.y == rightClickPosition.y) {
                    $('#zoom-marker-img').zoomMarker_RemoveMarker(element.id);
                }
            });
            var val = $("#wellselect option:selected").val();
            var welltype = val.substring(0, 5);
            var id = val.substring(5);
            var marker = {
                markerX: rightClickPosition.x,
                markerY: rightClickPosition.y,
                markerType: welltype,
                well: {id: id},
                projectImg: {id:${projectImgs[0].id}}
            }
            var dialog = '';
            var icon = 'well-icon pwell grey';
            var hasMarked = false;
            $.each(welldatas[welltype], function (index, elem) {
                if (elem.id == id) {
                    icon = 'well-icon pwell';
                    dialog = elem.name;
                    $(markerList).each(function (index, element) {
                        if (element.param.icon.indexOf(welltype) != -1 && element.param.dialog.value == elem.name) {
                            alert(dialog + "已经添加过标记！")
                            hasMarked = true;
                            return false;
                        }
                    });
                }
            })
            if (!hasMarked && confirm("确定将此处标记为 " + dialog + " 吗？")) {
                var markernames = document.getElementById("markernames");
                markernames.style.display = "none";
                $('#zoom-marker-img').zoomMarker_AddMarker({
                    icon: icon,
                    x: rightClickPosition.x,
                    y: rightClickPosition.y,
                    dialog: {
                        value: dialog,
                        offsetX: 20,
                        style: {
                            "border-color": "#ff8355"
                        }
                    },
                });
                $.ajax({
                    url : 'save.shtml',
                    type : "POST",
                    data : JSON.stringify(marker),
                    dataType: 'json',
                    contentType:'application/json;charset=UTF-8',
                    success : function(result) {
                        console.log(result);
                    }
                });
            } else {
                var markernames = document.getElementById("markernames");
                markernames.style.display = "none";
            }

        })
    })
</script>
<div class="row" id="edittable" style="display:none;">
    <div class="col-lg-12 col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <span id="edittile"></span>
                <span class="tools pull-right">
                            <a href="javascript:;" class="fa fa-chevron-down"></a>
                            <a href="javascript:;" class="fa fa-times"></a>
                          </span>
            </header>
        </section>
    </div>
</div>
<div class="row">
    <div id="zoom-marker-div" class="zoom-marker-div">
        <img class="zoom-marker-img" id="zoom-marker-img" alt="..." name="viewArea" draggable="false"/>
    </div>
</div>
<div id="menu">
    <ul>
        <li id="addMarker">添加井</li>
    </ul>
</div>
<div id="markernames">
    <select id="wellselect">
        <option>请选择</option>
    </select>
</div>