package com.github.hcsp.integration;

import com.github.hcsp.Application;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
public class ApplicationIntegrationTest {
    @Resource
    private Environment environment;

    @Test
    public void userTest() throws Exception {
        String url = "http://localhost:" + environment.getProperty("local.server.port");
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet(url + "/auth");
            httpclient.execute(httpget, (ResponseHandler<String>) httpResponse -> {
                Assertions.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
                Assertions.assertTrue(EntityUtils.toString(httpResponse.getEntity()).contains("用户没有登录"));
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws Exception {
        String url = "http://localhost:" + environment.getProperty("local.server.port");

        // 第一次访问
        Document html = Jsoup.parse(new URL(url), 60 * 1000);
        Assertions.assertEquals(
                Arrays.asList("1", "桃子", "14700", "2", "葡萄", "8000", "3", "香蕉", "6000", "4", "西瓜", "3500", "5", "苹果", "3100"),
                html.select("td").stream().map(Element::text).map(s -> s.replace(",", "").trim()).collect(Collectors.toList())
        );

        runSql("insert into goods (name)values('肥皂')");

        // 第二次访问，应该从缓存中获取，得到旧数据
        html = Jsoup.parse(new URL(url), 60 * 1000);
        Assertions.assertEquals(
                Arrays.asList("1", "桃子", "14700", "2", "葡萄", "8000", "3", "香蕉", "6000", "4", "西瓜", "3500", "5", "苹果", "3100"),
                html.select("td").stream().map(Element::text).map(s -> s.replace(",", "").trim()).collect(Collectors.toList())
        );

        Thread.sleep(2000);

        // 第三次访问，应该得到更新后的数据
        html = Jsoup.parse(new URL(url), 60 * 1000);
        Assertions.assertEquals(
                Arrays.asList("1", "桃子", "14700", "2", "葡萄", "8000", "3", "香蕉", "6000", "4", "西瓜", "3500", "5", "苹果", "3100", "6", "肥皂", "0"),
                html.select("td").stream().map(Element::text).map(s -> s.replace(",", "").trim()).collect(Collectors.toList())
        );
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        runSql("delete from goods where name = '肥皂'");
    }

    private void runSql(String sql) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?characterEncoding=utf-8&serverTimezone=UTC", "root", "fnst-3d3k");
        try (Statement statement = conn.createStatement()) {
            statement.execute(sql);
        }
    }
}
