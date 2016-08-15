package com.huotu.hotcms.widget.friendshipLink;

import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.widget.test.WidgetTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.io.IOException;
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
    protected void editorWork(Widget widget, WebElement editor, Supplier<Map<String, Object>> currentWidgetProperties) throws IOException {

        //添加一个链接
        WebElement addLink = editor.findElement(By.className("addLink"));
        addLink.click();

        List<WebElement> rows = editor.findElements(By.cssSelector(".linkbox .item"));
        assertThat(rows.size()).as("节点添加成功").isEqualTo(1);
        WebElement title = editor.findElement(By.name("title"));
        Actions actions = new Actions(driver);
        actions.sendKeys(title, "abc").build().perform();
        WebElement url = editor.findElement(By.name("url"));
        actions.sendKeys(url, "http://www.baidu.com").build().perform();

        Map map = currentWidgetProperties.get();
        List<Map<String, Object>> linkList = (List<Map<String, Object>>) map.get(WidgetInfo.VALID_LINK_LIST);
        linkList.get(0).containsValue("abc");
        linkList.get(0).containsValue("http://www.baidu.com");

        //删除节点
        List<WebElement> removerLinkItems = editor.findElements(By.cssSelector(".linkbox .removerLinkItem"));
        assertThat(removerLinkItems.size()).isEqualTo(1);
        removerLinkItems.get(0).click();

        rows = editor.findElements(By.cssSelector(".linkbox .item"));
        assertThat(rows).isEmpty();

    }

    @Override
    protected void browseWork(Widget widget, WidgetStyle style, Function<ComponentProperties, WebElement> uiChanger)
            throws IOException {
        ComponentProperties properties = widget.defaultProperties(resourceService);
        WebElement webElement = uiChanger.apply(properties);

        List<WebElement> a = webElement.findElements(By.tagName("a"));
        assertThat(a.size()).isEqualTo(6);
        assertThat(a.get(0).getAttribute("href")).isEqualToIgnoringCase("http://www.huobanplus.com");
        assertThat(a.get(0).getText()).isEqualToIgnoringCase("火图科技");
    }

    @Override
    protected void editorBrowseWork(Widget widget, Function<ComponentProperties, WebElement> uiChanger) throws IOException {
        ComponentProperties properties = widget.defaultProperties(resourceService);
        List<Map> links = (List<Map>) properties.get(WidgetInfo.VALID_LINK_LIST);
        WebElement webElement = uiChanger.apply(properties);
        List<WebElement> list = webElement.findElements(By.cssSelector(".linkbox .item"));
        assertThat(list.size()).isEqualTo(links.size());
        assertThat(list.get(0).findElement(By.name("title")).getAttribute("value"))
                .isEqualTo(links.get(0).get("title").toString());
    }


}
