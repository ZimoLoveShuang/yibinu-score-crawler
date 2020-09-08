<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html class="no-js" lang="en">

<head>

    <meta charset="utf-8">
    <title>宜宾学院教务系统成绩单下载</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- CSS -->
    <link rel='stylesheet' href='http://fonts.googleapis.com/css?family=PT+Sans:400,700'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/assets/css/reset.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/assets/css/supersized.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/assets/css/style.css">


    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

</head>

<body>

<div class="page-container">
    <h1>成绩单</h1>
    <form action="${pageContext.request.contextPath}/api/getScoreReport" method="post">
        <input type="text" name="xh" class="username" placeholder="学号">
        <input type="password" name="pwd" class="password" placeholder="密码">
        <button type="submit" id="btn">下载成绩单</button>
        <div class="error"><span>+</span></div>
        <div style="margin: 20px;font-size: 10px;color: #ef4300;">
            <a id="contact" target="_blank">
                联系站长
            </a>
        </div>
        <div id="donation" style="margin: 20px;font-size: 10px;color: #ef4300;">点击这里捐赠站长</div>
    </form>
    <div id="outerdiv"
         style="position:fixed;top:0;left:0;background:rgba(0,0,0,0.7);z-index:2;width:100%;height:100%;display:none;">
        <div id="innerdiv" style="position:absolute;">
            <img id="bigimg" style="border:5px solid #fff;" src=""/>
        </div>
    </div>
</div>

<!-- Javascript -->
<script src="${pageContext.request.contextPath}/static/assets/js/jquery-1.8.2.min.js"></script>
<script src="${pageContext.request.contextPath}/static/assets/js/supersized.3.2.7.min.js"></script>
<script src="${pageContext.request.contextPath}/static/assets/js/scripts.js"></script>

<script>
    $(function () {
        contactQQ();

        $("#donation").click(function () {
            imgShow("#outerdiv", "#innerdiv", "#bigimg");
        });

        $.supersized({
            // Functionality
            slide_interval: 4000,    // Length between transitions
            transition: 1,    // 0-None, 1-Fade, 2-Slide Top, 3-Slide Right, 4-Slide Bottom, 5-Slide Left, 6-Carousel Right, 7-Carousel Left
            transition_speed: 1000,    // Speed of transition
            performance: 1,    // 0-Normal, 1-Hybrid speed/quality, 2-Optimizes image quality, 3-Optimizes transition speed // (Only works for Firefox/IE, not Webkit)

            // Size & Position
            min_width: 0,    // Min width allowed (in pixels)
            min_height: 0,    // Min height allowed (in pixels)
            vertical_center: 1,    // Vertically center background
            horizontal_center: 1,    // Horizontally center background
            fit_always: 0,    // Image will never exceed browser width or height (Ignores min. dimensions)
            fit_portrait: 1,    // Portrait images will not exceed browser height
            fit_landscape: 0,    // Landscape images will not exceed browser width

            // Components
            slide_links: 'blank',    // Individual links for each slide (Options: false, 'num', 'name', 'blank')
            slides: [    // Slideshow Images
                {image: '${pageContext.request.contextPath}/static/assets/img/backgrounds/1.jpg'},
                {image: '${pageContext.request.contextPath}/static/assets/img/backgrounds/2.jpg'},
                {image: '${pageContext.request.contextPath}/static/assets/img/backgrounds/3.jpg'}
            ]

        });
    });

    function contactQQ() {
        var contact = $('#contact');
        var qq = '${qq}';
        if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
            contact.attr('href', '');
        } else if (/(Android)/i.test(navigator.userAgent)) {
            contact.attr('href', 'mqqwpa://im/chat?chat_type=wpa&uin=' + qq + '&version=1&src_type=web&web_src=oicqzone.com');
        } else {
            contact.attr('href', 'tencent://Message/?Uin=' + qq + '&websiteName=q-zone.qq.com&Menu=yes');
        }
    }

    function imgShow(outerdiv, innerdiv, bigimg) {
        var src = '${pageContext.request.contextPath}/static/assets/img/donate.png';
        $(bigimg).attr("src", src);

        $("<img/>").attr("src", src).load(function () {
            var windowW = $(window).width();
            var windowH = $(window).height();
            var realWidth = this.width;
            var realHeight = this.height;
            var imgWidth, imgHeight;
            var scale = 0.8;

            if (realHeight > windowH * scale) {
                imgHeight = windowH * scale;
                imgWidth = imgHeight / realHeight * realWidth;
                if (imgWidth > windowW * scale) {
                    imgWidth = windowW * scale;
                }
            } else if (realWidth > windowW * scale) {
                imgWidth = windowW * scale;
                imgHeight = imgWidth / realWidth * realHeight;
            } else {
                imgWidth = realWidth;
                imgHeight = realHeight;
            }

            $(bigimg).css("width", imgWidth);

            var w = (windowW - imgWidth) / 2;
            var h = (windowH - imgHeight) / 2;
            $(innerdiv).css({"top": h, "left": w});
            $(outerdiv).fadeIn("fast");
        });

        $(outerdiv).click(function () {
            $(this).fadeOut("fast");
        });
    }
</script>

</body>
</html>

