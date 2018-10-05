package com.philoertel.juryduty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class ParserTest {

    @Test
    public void parseGroups() throws Exception {
        Instructions instructions = Parser.parseInstructions(fileContents);
        assertThat(instructions.getDateString()).isEqualTo("20180828");
        assertThat(instructions.getReportingGroups()).containsExactly(
                "605", "619", "601", "613", "617", "616", "620", "625", "614", "627",
                "629");
    }

    private static final String fileContents = "<!DOCTYPE html>\n" +
            "<html lang=\"en\" dir=\"ltr\">\n" +
            "<head>\n" +
            "  <link rel=\"profile\" href=\"http://www.w3.org/1999/xhtml/vocab\" />\n" +
            "  <meta charset=\"utf-8\">\n" +
            "  <!--<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">-->\n" +
            "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
            "<meta name=\"X-UA-Compatible\" content=\"IE=Edge\" />\n" +
            "<link rel=\"shortlink\" href=\"/node/239\" />\n" +
            "<link rel=\"canonical\" href=\"/divisions/jury-services/jury-reporting\" />\n" +
            "<meta name=\"Generator\" content=\"Drupal 7 (http://drupal.org)\" />\n" +
            "  <title>\n" +
            "\tJury Reporting Instructions | Superior Court of California - County of San Francisco  </title>\n" +
            "  <style>\n" +
            "@import url(\"https://www.sfsuperiorcourt.org/modules/system/system.base.css?padx97\");\n" +
            "</style>\n" +
            "<style>\n" +
            "@import url(\"https://www.sfsuperiorcourt.org/sites/all/modules/jquery_update/replace/ui/themes/base/jquery.ui.core.css?padx97\");\n" +
            "@import url(\"https://www.sfsuperiorcourt.org/sites/all/modules/jquery_update/replace/ui/themes/base/jquery.ui.theme.css?padx97\");\n" +
            "@import url(\"https://www.sfsuperiorcourt.org/sites/all/modules/jquery_update/replace/ui/themes/base/jquery.ui.tabs.css?padx97\");\n" +
            "@import url(\"https://www.sfsuperiorcourt.org/sites/all/modules/jquery_update/replace/ui/themes/base/jquery.ui.accordion.css?padx97\");\n" +
            "</style>\n" +
            "<style>\n" +
            "@import url(\"https://www.sfsuperiorcourt.org/modules/field/theme/field.css?padx97\");\n" +
            "@import url(\"https://www.sfsuperiorcourt.org/modules/node/node.css?padx97\");\n" +
            "@import url(\"https://www.sfsuperiorcourt.org/sites/all/modules/views/css/views.css?padx97\");\n" +
            "@import url(\"https://www.sfsuperiorcourt.org/sites/all/modules/ckeditor/css/ckeditor.css?padx97\");\n" +
            "</style>\n" +
            "<style>\n" +
            "@import url(\"https://www.sfsuperiorcourt.org/sites/all/modules/ctools/css/ctools.css?padx97\");\n" +
            "</style>\n" +
            "<link type=\"text/css\" rel=\"stylesheet\" href=\"//cdn.jsdelivr.net/bootstrap/3.3.5/css/bootstrap.css\" media=\"all\" />\n" +
            "<style>\n" +
            "@import url(\"https://www.sfsuperiorcourt.org/sites/all/themes/bootstrap/css/3.3.5/overrides.min.css?padx97\");\n" +
            "@import url(\"https://www.sfsuperiorcourt.org/sites/all/themes/calcourts/css/styles.css?padx97\");\n" +
            "</style>\n" +
            "  <!-- HTML5 element support for IE6-8 -->\n" +
            "  <!--[if lt IE 9]>\n" +
            "    <script src=\"//html5shiv.googlecode.com/svn/trunk/html5.js\"></script>\n" +
            "  <![endif]-->\n" +
            "  <script src=\"//code.jquery.com/jquery-1.10.2.js\"></script>\n" +
            "<script>window.jQuery || document.write(\"<script src='/sites/all/modules/jquery_update/replace/jquery/1.10/jquery.js'>\\x3C/script>\")</script>\n" +
            "<script src=\"https://www.sfsuperiorcourt.org/misc/jquery.once.js?v=1.2\"></script>\n" +
            "<script src=\"https://www.sfsuperiorcourt.org/misc/drupal.js?padx97\"></script>\n" +
            "<script src=\"//code.jquery.com/ui/1.10.2/jquery-ui.js\"></script>\n" +
            "<script>window.jQuery.ui || document.write(\"<script src='/sites/all/modules/jquery_update/replace/ui/ui/jquery-ui.js'>\\x3C/script>\")</script>\n" +
            "<script src=\"//cdn.jsdelivr.net/bootstrap/3.3.5/js/bootstrap.js\"></script>\n" +
            "<script src=\"https://www.sfsuperiorcourt.org/sites/all/themes/calcourts/js/jquery.tablesorter.js?padx97\"></script>\n" +
            "<script src=\"https://www.sfsuperiorcourt.org/sites/all/themes/calcourts/js/jasny-bootstrap.js?padx97\"></script>\n" +
            "<script src=\"https://www.sfsuperiorcourt.org/sites/all/themes/calcourts/js/custom.js?padx97\"></script>\n" +
            "<script>jQuery.extend(Drupal.settings, {\"basePath\":\"\\/\",\"pathPrefix\":\"\",\"ajaxPageState\":{\"theme\":\"calcourts\",\"theme_token\":\"SOK9_xwZ2drER65UaKF0LDqQvQfiof4P5RA7WF3jMf4\",\"js\":{\"sites\\/all\\/themes\\/bootstrap\\/js\\/bootstrap.js\":1,\"\\/\\/code.jquery.com\\/jquery-1.10.2.js\":1,\"0\":1,\"misc\\/jquery.once.js\":1,\"misc\\/drupal.js\":1,\"\\/\\/code.jquery.com\\/ui\\/1.10.2\\/jquery-ui.js\":1,\"1\":1,\"\\/\\/cdn.jsdelivr.net\\/bootstrap\\/3.3.5\\/js\\/bootstrap.js\":1,\"sites\\/all\\/themes\\/calcourts\\/js\\/jquery.tablesorter.js\":1,\"sites\\/all\\/themes\\/calcourts\\/js\\/jasny-bootstrap.js\":1,\"sites\\/all\\/themes\\/calcourts\\/js\\/custom.js\":1},\"css\":{\"modules\\/system\\/system.base.css\":1,\"misc\\/ui\\/jquery.ui.core.css\":1,\"misc\\/ui\\/jquery.ui.theme.css\":1,\"misc\\/ui\\/jquery.ui.tabs.css\":1,\"misc\\/ui\\/jquery.ui.accordion.css\":1,\"modules\\/field\\/theme\\/field.css\":1,\"modules\\/node\\/node.css\":1,\"sites\\/all\\/modules\\/views\\/css\\/views.css\":1,\"sites\\/all\\/modules\\/ckeditor\\/css\\/ckeditor.css\":1,\"sites\\/all\\/modules\\/ctools\\/css\\/ctools.css\":1,\"\\/\\/cdn.jsdelivr.net\\/bootstrap\\/3.3.5\\/css\\/bootstrap.css\":1,\"sites\\/all\\/themes\\/bootstrap\\/css\\/3.3.5\\/overrides.min.css\":1,\"sites\\/all\\/themes\\/calcourts\\/css\\/styles.css\":1}},\"urlIsAjaxTrusted\":{\"\\/search\\/node\":true},\"bootstrap\":{\"anchorsFix\":\"0\",\"anchorsSmoothScrolling\":\"0\",\"formHasError\":1,\"popoverEnabled\":0,\"popoverOptions\":{\"animation\":1,\"html\":0,\"placement\":\"right\",\"selector\":\"\",\"trigger\":\"click\",\"triggerAutoclose\":1,\"title\":\"\",\"content\":\"\",\"delay\":0,\"container\":\"body\"},\"tooltipEnabled\":0,\"tooltipOptions\":{\"animation\":1,\"html\":0,\"placement\":\"auto left\",\"selector\":\"\",\"trigger\":\"hover focus\",\"delay\":0,\"container\":\"body\"}}});</script>\n" +
            "</head>\n" +
            "<body role=\"document\" class=\"html not-front not-logged-in no-sidebars page-node page-node- page-node-239 node-type-page\">\n" +
            "  <div id=\"skip-link\">\n" +
            "    <a href=\"#main-content\" class=\"element-invisible element-focusable\">Skip to main content</a>\n" +
            "  </div>\n" +
            "    \n" +
            "<div id=\"page\" class=\"container\">\n" +
            "     <div id=\"main\" class=\"row\">\n" +
            "     \t<div id=\"headerBar\" class=\"row\">\n" +
            "          \t\n" +
            "\n" +
            "<div class=\"toolBar col-xs-12\">\n" +
            "\t<div class=\"row\">\n" +
            "\t\t<div class=\"col-xs-4\">&nbsp;</div>\n" +
            "\t\t<div class=\"col-xs-1 resizers\">\n" +
            "\t\t\t<div class=\"resizeNormal\"><a id=\"resizeNormal\" href=\"#\">A</a></div>\n" +
            "\t\t\t<div class=\"resizeMedium\"><a id=\"resizeMedium\" href=\"#\">A</a></div>\n" +
            "\t\t\t<div class=\"resizeLarge\"><a id=\"resizeLarge\" href=\"#\">A</a></div>\n" +
            "\t\t</div>\n" +
            "\t\t<div class=\"col-xs-6 languages\">\n" +
            "\t\t\t<a href=\"https://translate.google.com/translate?hl=en&sl=en&tl=zh-CN&u=https://sfsuperiorcourt.org/translation-disclaimer\" class=\"txttoolbar\"><span class=\"zh-si\"><span class=\"alt-text\">Chinese (Simplified)</span></span></a>\n" +
            "\t\t\t<a href=\"https://translate.google.com/translate?hl=en&sl=en&tl=zh-TW&u=https://sfsuperiorcourt.org/translation-disclaimer\" class=\"txttoolbar\"><span class=\"zh-tr\"><span class=\"alt-text\">Chinese (Traditional)</span></span></a>\n" +
            "\t\t\t<a href=\"https://translate.google.com/translate?hl=en&sl=en&tl=tl&u=https://sfsuperiorcourt.org/translation-disclaimer\" class=\"txttoolbar\">Filipino</a>\n" +
            "\t\t\t<a href=\"https://translate.google.com/translate?hl=en&sl=en&tl=ru&u=https://sfsuperiorcourt.org/translation-disclaimer\" class=\"txttoolbar\">русский</a>\n" +
            "\t\t\t<a href=\"https://translate.google.com/translate?hl=en&sl=en&tl=es&u=https://sfsuperiorcourt.org/translation-disclaimer\" class=\"txttoolbar\">Español</a>\n" +
            "\t\t\t<a class=\"txttoolbar last\" href=\"https://translate.google.com/translate?hl=en&sl=en&tl=vi&u=https://sfsuperiorcourt.org/translation-disclaimer\">Tiếng Việt</a>\n" +
            "\t\t</div>\n" +
            "\t\t<div class=\"contact col-xs-1\">\n" +
            "\t\t\t<a class=\"btn btn-primary\" href=\"/contact\" name=\"Contact_Us\">Contact Us</a>\n" +
            "\t\t</div>\n" +
            "\t</div>\n" +
            "</div>\n" +
            "<div class=\"logoBar col-xs-12\">\n" +
            "\t<div class=\"row\">\n" +
            "\t\t<div class=\"logo col-xs-8\">\n" +
            "\t\t\t<a href=\"/\"><img src=\"/sites/default/files/images/logo.gif\" alt=\"Superior Court of California - County of San Francisco\" title=\"Superior Court of California - County of San Francisco\" /></a>\n" +
            "\t\t</div>\n" +
            "\t\t<div class=\"datesearch col-xs-4\">\n" +
            "\t\t\t<div class=\"date row\">\n" +
            "\t\t\t\t<div class=\"col-xs-12\">\n" +
            "\t\t\t\t<!--Updated Daily-->Aug. 27, 2018\t\t\t\t</div>\n" +
            "\t\t\t</div>\n" +
            "\t\t\t<div class=\"searchWrap row\">\n" +
            "\t\t\t\t<div class=\"col-xs-12\">\n" +
            "\t\t\t\t<form class=\"search-form clearfix\" action=\"/search/node\" method=\"post\" id=\"search-form\" accept-charset=\"UTF-8\"><div><div class=\"form-wrapper form-group\" id=\"edit-basic\"><div class=\"input-group\"><input placeholder=\"Search\" class=\"form-control form-text\" type=\"text\" id=\"edit-keys\" name=\"keys\" value=\"\" size=\"40\" maxlength=\"255\" /><span class=\"input-group-btn\"><button type=\"submit\" class=\"btn btn-primary\"><span class=\"icon glyphicon glyphicon-search\" aria-hidden=\"true\"></span></button></span></div><button class=\"element-invisible btn btn-primary form-submit\" type=\"submit\" id=\"edit-submit\" name=\"op\" value=\"Search\">Search</button>\n" +
            "</div><input type=\"hidden\" name=\"form_build_id\" value=\"form-8GbYoe4Qk3BA8ZaDG2B78M0GNVjkYR7GzZnzlSYKWdw\" />\n" +
            "<input type=\"hidden\" name=\"form_id\" value=\"search_form\" />\n" +
            "</div></form>\t\t\t\t</div>\n" +
            "\t\t\t</div>\n" +
            "\t\t</div>\n" +
            "\t</div>\n" +
            "</div>\n" +
            "          </div>\n" +
            "          <!-- BEGIN MENU_TOP -->\n" +
            "<nav id=\"topNavBar\" class=\"navbar navbar-default\" role=\"navigation\">\n" +
            "\t<div class=\"row\">\n" +
            "          <div class=\"top-nav\">          \n" +
            "               <div class=\"navbar\" id=\"main-navbar-collapse\">\n" +
            "                    <ul id=\"mainMenu\" class=\"menu row\">\n" +
            "                      <li class=\" col-xs-2\"><a id=\"home\" href=\"/\">Home</a></li>\n" +
            "                    <li class=\"col-xs-2 dropdown\"><a href=\"/online-services\">Online Services <span>Pay&nbsp;Fines,&nbsp;Case Lookup...</span></a>\n" +
            "<ul class=\"dropdown-menu\">\n" +
            "<li><a href=\"http://webapps.sftc.org/cc/CaseCalendar.dll\">Case Calendar</a></li>\n" +
            "<li><a href=\"http://webapps.sftc.org/ci/CaseInfo.dll\">Case Query</a></li>\n" +
            "<li><a href=\"/online-services/efiling\">EFiling</a></li>\n" +
            "<li><a href=\"/online-services/tentative-rulings\">Tentative Rulings</a></li>\n" +
            "<li><a href=\"/divisions/traffic/traffic-citations\">Traffic – Pay Fines</a></li>\n" +
            "</ul></li>\n" +
            "<li class=\"col-xs-2 dropdown\"><a href=\"/forms-filing\">Forms &amp; Fees <span>Forms,&nbsp;Fee&nbsp;Schedule...</span></a>\n" +
            "<ul class=\"dropdown-menu\">\n" +
            "<li><a href=\"/forms-filing/civil-fee-schedules-for-prior-years\">Civil Fee Schedules for Prior Years</a></li>\n" +
            "<li><a href=\"/forms-filing/fees\">Local Fees</a></li>\n" +
            "<li><a href=\"/forms-filing/forms\">Local Forms</a></li>\n" +
            "</ul></li>\n" +
            "<li class=\"col-xs-2 dropdown\"><a href=\"/self-help\">Self-Help <span>Get Help,&nbsp;Info,&nbsp;FAQs...</span></a>\n" +
            "<ul class=\"dropdown-menu\">\n" +
            "<li><a href=\"/self-help/annulment\">Annulment</a></li>\n" +
            "<li><a href=\"/self-help/custody\">Child Custody/Visitation</a></li>\n" +
            "<li><a href=\"/self-help/child-support\">Child Support</a></li>\n" +
            "<li><a href=\"/divisions/probate/conservatorships-of-adults\">Conservatorships of Adults</a></li>\n" +
            "<li><a href=\"/self-help/dissolution-of-marriage-domestic-partnership\">Dissolution of Marriage</a></li>\n" +
            "<li><a href=\"/self-help/establish-paternity\">Establish Paternity</a></li>\n" +
            "<li><a href=\"/self-help/evictions\">Evictions</a></li>\n" +
            "<li><a href=\"/self-help/gender-changes\">Gender Changes</a></li>\n" +
            "<li><a href=\"/divisions/probate/guardianship-children\">Guardianship of Children</a></li>\n" +
            "<li><a href=\"/self-help/legal-separation\">Legal Separation</a></li>\n" +
            "<li><a href=\"/self-help/name-change\">Name Change</a></li>\n" +
            "<li><a href=\"/self-help/restrainingorders\">Restraining Orders</a></li>\n" +
            "<li><a href=\"/self-help/small-claims\">Small Claims Mediation</a></li>\n" +
            "<li><a href=\"/self-help/spousal-support\">Spousal Support</a></li>\n" +
            "<li><a href=\"/self-help/adoption\">Step-Parent Adoptions</a></li>\n" +
            "</ul></li>\n" +
            "<li class=\"col-xs-2 dropdown active\"><a href=\"/divisions\">Divisions <span>Civil,&nbsp;Criminal,&nbsp;Family...</span></a>\n" +
            "<ul class=\"dropdown-menu\">\n" +
            "<li><a href=\"/divisions/appeals\">Appellate Division</a></li>\n" +
            "<li><a href=\"/divisions/civil\">Civil</a></li>\n" +
            "<li><a href=\"/divisions/collaborative\">Collaborative Courts</a></li>\n" +
            "<li><a href=\"/divisions/reporters\">Court Reporters</a></li>\n" +
            "<li><a href=\"/divisions/criminal\">Criminal</a></li>\n" +
            "<li><a href=\"/divisions/interpreters\">Interpreter Division</a></li>\n" +
            "<li><a href=\"/divisions/jury-services\">Jury Services</a></li>\n" +
            "<li><a href=\"/divisions/probate\">Probate</a></li>\n" +
            "<li><a href=\"/divisions/small-claims\">Small Claims</a></li>\n" +
            "<li><a href=\"/divisions/traffic\">Traffic</a></li>\n" +
            "<li><a href=\"/divisions/ufc\">Unified Family Court</a></li>\n" +
            "</ul></li>\n" +
            "<li class=\"col-xs-2 dropdown last\"><a href=\"/general-info\">General Info <span>Local&nbsp;Rules,&nbsp;ADA,&nbsp;Maps...</span></a>\n" +
            "<ul class=\"dropdown-menu\">\n" +
            "<li><a href=\"/general-info/ada\">ADA</a></li>\n" +
            "<li><a href=\"/general-info/administration-information\">Administration Information</a></li>\n" +
            "<li><a href=\"/general-info/waiting-rooms\">Children's Waiting Rooms</a></li>\n" +
            "<li><a href=\"/general-info/Community Outreach\">Community Outreach</a></li>\n" +
            "<li><a href=\"/general-info/contact\">Contact Us</a></li>\n" +
            "<li><a href=\"/general-info/court-technology\">Court Technology</a></li>\n" +
            "<li><a href=\"/general-info/directions\">Courthouse Directions</a></li>\n" +
            "<li><a href=\"/general-info/guidelines-professional-conduct\">Guidelines-Professional Conduct</a></li>\n" +
            "<li><a href=\"/general-info/holiday-schedule\">Holiday Schedule</a></li>\n" +
            "<li><a href=\"/general-info/hr\">Human Resources/Employment</a></li>\n" +
            "<li><a href=\"/general-info/judicial-assignments\">Judicial Assignments</a></li>\n" +
            "<li><a href=\"/general-info/local-rules\">Local Rules</a></li>\n" +
            "<li><a href=\"/general-info/mint-cafe\">Mint Cafe</a></li>\n" +
            "<li><a href=\"/general-info/news-media\">News & Media</a></li>\n" +
            "<li><a href=\"/general-info/master-calendar\">Presiding Judge Master Calendar</a></li>\n" +
            "<li><a href=\"/general-info/privacy-policy\">Privacy Policy</a></li>\n" +
            "<li><a href=\"/general-info/records\">Records</a></li>\n" +
            "<li><a href=\"/general-info/temporary-judges\">Temporary Judge Program</a></li>\n" +
            "</ul></li>\n" +
            "                    </ul>                    \n" +
            "               </div>  \n" +
            "          </div>             \n" +
            "     </div>\n" +
            "</nav>\n" +
            "\n" +
            "\n" +
            "<!-- END MENU_TOP -->\n" +
            "          <div class=\"divisions\">\n" +
            "                              <div id=\"content\" role=\"main\" class=\"col-xs-12 shadow\">\n" +
            "               \t<div id=\"contentWrap\" class=\"row\">\n" +
            "                    \t<ol class=\"breadcrumb\"><li><a href=\"/\">Home</a></li>\n" +
            "<li><a href=\"/divisions\">Divisions</a></li>\n" +
            "<li><a href=\"/divisions/jury-services\">Jury Services</a></li>\n" +
            "<li>Jury Reporting Instructions</li>\n" +
            "</ol>  \n" +
            "                         <div id=\"hrDarkBorder\" class=\"row\"><!-- fill line --></div>\n" +
            "                         <div class=\"clear20\"></div>\n" +
            "                         <div id=\"mainContentWrap\" class=\"col-xs-12\">                                       \n" +
            "                                <div class=\"region region-content\">\n" +
            "    <div id=\"block-system-main\" class=\"block block-system\">\n" +
            "\n" +
            "      \n" +
            "  <article class=\"node-239 node node-page clearfix\">\n" +
            "\n" +
            "     <div class=\"contentLeftColumn col-xs-3\"> \n" +
            "     \t\t\n" +
            "\n" +
            "<div class=\"sidebar-nav\">\n" +
            "\t<div class=\"navbar navbar-default\" role=\"navigation\">\n" +
            "\t\t<div class=\"navbar\" id=\"sidebar-navbar-collapse\">\n" +
            "     <ul class=\"nav nav-pills nav-stacked level1\">\n" +
            "          <li><a href=\"divisions\">Divisions</a>\n" +
            "\t\t\t<ul class=\"nav nav-pills nav-stacked level2\">     \n" +
            "        <li><a href=\"/divisions/appeals\">Appellate Division</a></li>\n" +
            "        <li><a href=\"/divisions/civil\">Civil</a></li>\n" +
            "        <li><a href=\"/divisions/collaborative\">Collaborative Courts</a></li>\n" +
            "        <li><a href=\"/divisions/reporters\">Court Reporters</a></li>\n" +
            "        <li><a href=\"/divisions/criminal\">Criminal</a></li>\n" +
            "        <li><a href=\"/divisions/interpreters\">Interpreter Division</a></li>\n" +
            "        <li><a class=\"active \" href=\"/divisions/jury-services\">Jury Services</a>\n" +
            "          <ul class=\"nav nav-pills nav-stacked level3\">\n" +
            "            <li><a class=\"active\" href=\"/divisions/jury-services/jury-reporting\">Jury Reporting Instructions</a></li>\n" +
            "          </ul></li>\n" +
            "        <li><a href=\"/divisions/probate\">Probate</a></li>\n" +
            "        <li><a href=\"/divisions/small-claims\">Small Claims</a></li>\n" +
            "        <li><a href=\"/divisions/traffic\">Traffic</a></li>\n" +
            "        <li><a class=\"last\" href=\"/divisions/ufc\">Unified Family Court</a></li>\n" +
            "     \n" +
            "     \t\t</ul>\n" +
            "     \t</li>\n" +
            "     </ul>  \n" +
            "          \n" +
            "          \n" +
            "          </div>\n" +
            "\n" +
            "\t</div>\n" +
            "</div>\n" +
            "          \n" +
            "               </div>\n" +
            "     <div class=\"contentMain col-xs-9 leftBorder\">\n" +
            "          <div class=\"row\">\n" +
            "               <div class=\"hero col-xs-12\">\n" +
            "                    <h1>jury reporting instructions:</h1>\n" +
            "\n" +
            "<h1>Week of&nbsp;AUGUST 27, 2018</h1>\n" +
            "                           \n" +
            "               </div>\n" +
            "               <div id=\"hrBorderSub\" class=\"row\"><!-- fill line --></div>\n" +
            "               <div class=\"row\">\n" +
            "     <div class=\"addThis col-xs-12\">\n" +
            "\t\t<div class=\"function\">\n" +
            "\t\t\t<a href=\"javascript:window.print();\" class=\"print\">Print</a>\n" +
            "\t\t\t | \n" +
            "\t\t\t<a href=\"/contact\" class=\"email\">E-mail</a>\n" +
            "\t\t</div>    \n" +
            "     </div> \n" +
            "</div>               <div id=\"mainContent\" class=\"col-xs-12\">\n" +
            "               \t\t\t               <div class=\"contentCenter col-xs-12\">\n" +
            "                              <a id=\"main-content\"></a> \n" +
            "                                                                     \n" +
            "                              <div class=\"row\"> \n" +
            "                                   <div class=\"col-xs-12\">     \n" +
            "                                       \t                     \n" +
            "                                        <div class=\"field field-name-body field-type-text-with-summary field-label-hidden\"><div class=\"field-items\"><div class=\"field-item even\"><h3>civic center courthouse: 400 MCALLISTER street, room 007</h3>\n" +
            "\n" +
            "<p><strong><u>GROUPS REPORTING: </u></strong></p>\n" +
            "\n" +
            "<p>There are no groups scheduled to report on Tuesday, August 28, 2018.&nbsp;</p>\n" +
            "\n" +
            "<p><strong><u>GROUPS ON STANDBY:</u></strong></p>\n" +
            "\n" +
            "<p>Groups 101, 102, 104, 105, 106, 107, 108, 110, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 124, 125 and 126 revisit this website on Tuesday, August 28, 2018 after 4:30 p.m.</p>\n" +
            "\n" +
            "<p><strong><u>GROUPS ALREADY REPORTED:</u> Please call the Juror Hotline at 415-551-3608 if you did not report.&nbsp;</strong></p>\n" +
            "\n" +
            "<p>Groups 103, 109, 111 and 123.</p>\n" +
            "\n" +
            "<h3><span style=\"color: rgb(68, 68, 68); text-transform: uppercase; font-family: &quot;times new roman&quot;, times, serif; font-size: 14px; font-weight: bold;\">hall of justice: 850 bryant street, room 307</span></h3>\n" +
            "\n" +
            "<p><strong><u>GROUPS REPORTING:</u></strong></p>\n" +
            "\n" +
            "<p>Groups&nbsp;605 and 619&nbsp;report to 850 Bryant Street, Room 307 on Tuesday, August 28, 2018 at 8:45 a.m.</p>\n" +
            "\n" +
            "<p>Groups 601, 613 and 617 report to 850 Bryant Street, Room 307 on Tuesday, August 28, 2018 at at 9:30 a.m.</p>\n" +
            "\n" +
            "<p>Groups&nbsp;616, 620 and 625&nbsp;report to 850 Bryant Street, Room 307 on Tuesday, August 28, 2018 at at 12:45 p.m.</p>\n" +
            "\n" +
            "<p>Groups&nbsp;614, 627 and 629&nbsp;report to 850 Bryant Street, Room 307 on Tuesday, August 28, 2018 at 1:30 p.m.</p>\n" +
            "\n" +
            "<p><strong><u>GROUPS ON STANDBY:</u></strong></p>\n" +
            "\n" +
            "<p>Groups&nbsp;603, 608, 610, 615 and 622&nbsp;revisit this website on Tuesday, August 28, 2018 after 4:30 p.m.</p>\n" +
            "\n" +
            "<p><strong><u>GROUPS ALREADY REPORTED:</u>&nbsp;Please call the Juror Hotline at 415-551-3608 if you did not report.&nbsp;</strong></p>\n" +
            "\n" +
            "<p>Groups 602, 604, 606, 607, 609, 611, 612, 618, 621, 623, 624, 626, 628 and 630.&nbsp;</p>\n" +
            "\n" +
            "<h3>Parking AND PUBLIC TRANSPORTATION Information</h3>\n" +
            "\n" +
            "<p><strong><u>Parking Information</u></strong></p>\n" +
            "\n" +
            "<p><em>Civic Center Courthouse:</em> The Court has negotiated a discounted rate of $7 per day at the Performing Arts Garage, subject to availability. The garage is located at 360 Grove Street, between Gough and Franklin Streets; enter the garage from Grove Street. Bring parking ticket to the Jury Assembly Room and ask for a voucher. There are no in-and-out privileges with the discounted rate. Please note that the garage is sometimes full in the afternoon, therefore leave extra time to find alternative parking.</p>\n" +
            "\n" +
            "<p><em>Hall of Justice: </em>Parking is extremely limited around the Hall of Justice. Most parking lots are full by mid-morning. We strongly recommend taking public transportation.</p>\n" +
            "\n" +
            "<p><strong><u>Public Transit Information</u></strong></p>\n" +
            "\n" +
            "<p><em>Civic Center Courthouse: </em>47 Van Ness, 49 Van Ness/Mission, 19 Polk, 5 Fulton, 5R Fulton Rapid, and 31 Balboa, in addition to Metro service to Civic Center Station.</p>\n" +
            "\n" +
            "<p><em>Hall of Justice:&nbsp;</em>47 Van Ness, 19 Polk, 12 Folsom, 8 Bayshore, and 27 Bryant. Please visit <a href=\"https://www.sfmta.com/\">www.sfmta.com</a> for more information.</p>\n" +
            "\n" +
            "<p><u><strong>Biking Information</strong></u></p>\n" +
            "\n" +
            "<p><em>Civic Center Courthouse:</em>&nbsp;The Civic Center Courthouse is accessible via several bicycle routes along Polk Street and Market Street. There are bike racks available on Polk Street next to the Courthouse, in front of City Hall, and in the Civic Center Garage. There is a Bay Area Bike Share station in front of City Hall. For more information on City Bike Share stations, visit <a href=\"http://www.bayareabikeshare.com/\">www.bayareabikeshare.com</a>.</p>\n" +
            "\n" +
            "<p><em>Hall of Justice:&nbsp;</em>The Hall of Justice is accessible by bike via several bicycle routes along 7th Street, Townsend Street, and 5th Street. There are bike racks available in front of the Hall of Justice on Bryant Street.</p>\n" +
            "\n" +
            "<h3>Other information</h3>\n" +
            "\n" +
            "<p><u><strong>Security Information</strong></u></p>\n" +
            "\n" +
            "<p>You are required to pass through airport-style security when entering the courthouse. Weapons and items construed to be weapons are not allowed and will be confiscated. You must clear security to enter the Jury Assembly Room. As there may be lines, please allow sufficient time for the security process. Peak times are generally 8:30-10 a.m. and 12-2:30 p.m.</p>\n" +
            "\n" +
            "<p>Items that are not allowed in the courthouse include, but are not limited to: firearms; knives; scissors; pepper spray; sharp tools or objects; tasers; graffiti pens or markers; flammable gas or materials; aerosol cans; spray paint; skateboards; scooters; portable speakers; large musical instruments; and other large/bulky items. You may not enter the courthouse with these or any items that security staff deem unacceptable. There is no storage available for any such items.</p>\n" +
            "\n" +
            "<p><strong><u>WiFi and Cell Phones</u></strong></p>\n" +
            "\n" +
            "<p>Jurors are welcome to use laptops and cell phones while in the Jury Assembly Room. Free WiFi is available.</p>\n" +
            "\n" +
            "<p><strong><u>Juror Hotline</u></strong></p>\n" +
            "\n" +
            "<p>If you have questions, call us at 415-551-3608, Monday- Friday, 8:00 a.m.- 4:30 p.m.</p>\n" +
            "</div></div></div>    \n" +
            "                                   </div>  \n" +
            "                              </div>                             \n" +
            "                         </div>                        \n" +
            "               \t\n" +
            "               </div>\n" +
            "          </div>\n" +
            "     </div>\n" +
            "\n" +
            "\n" +
            "\n" +
            "</article>\n" +
            "</div>\n" +
            "  </div>\n" +
            "                             \n" +
            "                         </div>\n" +
            "                         \t<div id=\"footer\" class=\"row\">\n" +
            "          <div class=\"footerLeft col-xs-8\">\n" +
            "               <!-- begin body_footer -->\n" +
            "               <div class=\"row\">\n" +
            "                    <div class=\"col-xs-12\">                    \t\n" +
            "\t\t\t\t\t<a href=\"/general-info/contact\">Contact Us</a> | <a href=\"/general-info/ada\">ADA</a> | <a href=\"/general-info/hr\">Employment</a> | <a href=\"/general-info/holiday-schedule\">Holiday Schedule</a> | <a href=\"/sitemap\">Site Map</a>\n" +
            "                    </div>\n" +
            "               </div>\n" +
            "               <!-- end body_footer -->\n" +
            "          </div>    \n" +
            "          <div class=\"footerRight col-xs-4\">\n" +
            "               <div class=\"row\">\n" +
            "                    <div class=\"col-xs-12\">\n" +
            "                    \t&copy; 2018  Superior Court of California - County of San Francisco \n" +
            "                    </div>  \n" +
            "\t\t\t</div>                          \t           \n" +
            "          </div>           \n" +
            "     </div>\n" +
            "\n" +
            "\n" +
            "                    </div>    \n" +
            "\t\t\t</div>           \n" +
            "               \n" +
            "          </div>\n" +
            "     </div>\n" +
            "</div>\n" +
            "\n" +
            "  <script src=\"https://www.sfsuperiorcourt.org/sites/all/themes/bootstrap/js/bootstrap.js?padx97\"></script>\n" +
            "\n" +
            "</body>\n" +
            "</html>\n";
}
