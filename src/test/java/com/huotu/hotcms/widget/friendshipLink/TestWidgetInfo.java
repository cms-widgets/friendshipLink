package com.huotu.hotcms.widget.friendshipLink;

import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.widget.test.WidgetTest;
import com.huotu.widget.test.WidgetTestConfig;
import com.huotu.widget.test.bean.WidgetViewController;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by lhx on 2016/6/22.
 */

public class TestWidgetInfo extends WidgetTest {
    @Override
    protected boolean printPageSource() {
        return true;
    }

    @Autowired
    private WidgetViewController widgetViewController;

    @Override
    protected void editorWork(Widget widget, WebElement editor, Supplier<Map<String, Object>> currentWidgetProperties) {
        try{
            currentWidgetProperties.get();
            assert false;
        }catch (IllegalStateException ignored){
            assertThat(0).as("save没有属性值返回异常").isEqualTo(0);
        }

        //添加一个链接
        WebElement addLink = editor.findElement(By.className("addLink"));
        addLink.click();
        List<WebElement> rows = editor.findElements(By.className("row"));
        assertThat(rows.size()).as("节点添加成功").isEqualTo(1);
        Map ps = currentWidgetProperties.get();
        List<Map<String,Object>> linkList = (List<Map<String, Object>>) ps.get("linkList");
        assertThat(linkList.get(0).get("title").toString()).as("添加的节点").isEqualTo(".");


        //删除节点
        List<WebElement> removerLinkItems = editor.findElements(By.className("removerLinkItem"));
        assertThat(removerLinkItems.size()).isEqualTo(1);
        removerLinkItems.get(0).click();
        try {
            ps = currentWidgetProperties.get();
        }catch (IllegalStateException e){
            assertThat(0).as("linkList 没有数据，返回properties is null").isEqualTo(0);
        }

        rows = editor.findElements(By.className("row"));
        assertThat(rows.size()).isEqualTo(0);


    }

    @Override
    protected void browseWork(Widget widget, WidgetStyle style, Function<ComponentProperties, WebElement> uiChanger) {
        uiChanger = (properties) -> {
            widgetViewController.setCurrentProperties(properties);
            String uri = "/browse/" + WidgetTestConfig.WidgetIdentity(widget) + "/" + style.id();
            if (printPageSource())
                try {
                    mockMvc.perform(get(uri))
                            .andDo(print());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new IllegalStateException("no print html");
                }
            driver.get("http://localhost" + uri);
            WebElement webElement = driver.findElement(By.id("browse")).findElement(By.tagName("div"));
            return webElement;
        };
        ComponentProperties componentProperties = new ComponentProperties();
        ComponentProperties properties = new ComponentProperties();
        List<LinkItem> list = new ArrayList<>();
        LinkItem linkItem = new LinkItem();
        linkItem.setTitle("hello world");
        linkItem.setUrl("http://www.baidu.com");
        list.add(linkItem);
        properties.put("linkList", list);
        componentProperties.put("properties", properties);

        WebElement webElement = uiChanger.apply(componentProperties);

        List<WebElement> a = webElement.findElements(By.tagName("a"));
        assertThat(a.size()).isEqualTo(1);
        assertThat(a.get(0).getAttribute("href")).isEqualToIgnoringCase("http://www.baidu.com");
        assertThat(a.get(0).getText()).isEqualToIgnoringCase("hello world");
    }
}
