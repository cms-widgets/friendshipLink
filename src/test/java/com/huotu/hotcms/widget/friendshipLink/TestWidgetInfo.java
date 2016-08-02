package com.huotu.hotcms.widget.friendshipLink;

import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.widget.test.WidgetTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class TestWidgetInfo extends WidgetTest {
    @Override
    protected boolean printPageSource() {
        return true;
    }

    @Override
    protected void editorWork(Widget widget, WebElement editor, Supplier<Map<String, Object>> currentWidgetProperties) {
        try {
            currentWidgetProperties.get();
        } catch (IllegalStateException ignored) {
            assertThat(0).as("save没有属性值返回异常").isEqualTo(0);
        }

        //添加一个链接
        WebElement addLink = editor.findElement(By.className("addLink"));
        addLink.click();
        List<WebElement> rows = editor.findElements(By.className("row"));
        assertThat(rows.size()).as("节点添加成功").isEqualTo(2);
        Map ps = currentWidgetProperties.get();
        List<Map<String, Object>> linkList = (List<Map<String, Object>>) ps.get("linkList");
        assertThat(linkList.get(0).get("title").toString()).as("添加的节点").isEqualTo(".");


        //删除节点
        List<WebElement> removerLinkItems = editor.findElements(By.className("removerLinkItem"));
        assertThat(removerLinkItems.size()).isEqualTo(2);
        removerLinkItems.get(0).click();
        try {
            ps = currentWidgetProperties.get();
        } catch (IllegalStateException e) {
            assertThat(0).as("linkList 没有数据，返回properties is null").isEqualTo(0);
        }

        rows = editor.findElements(By.className("row"));
        assertThat(rows.size()).isEqualTo(1);
    }

    @Override
    protected void browseWork(Widget widget, WidgetStyle style, Function<ComponentProperties, WebElement> uiChanger) {

        ComponentProperties properties = new ComponentProperties();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> linkItem = new HashMap<>();
        linkItem.put("title", "hello world");
        linkItem.put("target", "_blank");
        linkItem.put("url", "http://www.baidu.com");
        list.add(linkItem);
        list.add(linkItem);
        properties.put(WidgetInfo.VALID_LINK_LIST, list);

        WebElement webElement = uiChanger.apply(properties);

        List<WebElement> a = webElement.findElements(By.tagName("a"));
        assertThat(a.size()).isEqualTo(2);
        assertThat(a.get(0).getAttribute("href")).isEqualToIgnoringCase("http://www.baidu.com");
        assertThat(a.get(0).getText()).isEqualToIgnoringCase("hello world");
    }
}
