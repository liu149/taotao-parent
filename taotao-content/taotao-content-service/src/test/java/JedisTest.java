import com.taotao.content.jedis.JedisClient;
import com.taotao.content.jedis.JedisClientCluster;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * author : liuqi
 * createTime : 2018-09-13
 * description : TODO
 * version : 1.0
 */
public class JedisTest {

    @Test
    public void testJedis() throws IOException {
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.0.11",7000));
        nodes.add(new HostAndPort("192.168.0.11",7001));
        nodes.add(new HostAndPort("192.168.0.11",7002));
        nodes.add(new HostAndPort("192.168.0.11",7003));
        nodes.add(new HostAndPort("192.168.0.11",7004));
        nodes.add(new HostAndPort("192.168.0.11",7005));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        jedisCluster.set("myName1","liuqi1");

        System.out.println(jedisCluster.get("myName1"));
        jedisCluster.close();
    }


    @Test
    public void testdanji(){
        //1.初始化spring容器
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        //2.获取实现类实例
        JedisClient bean = context.getBean(JedisClient.class);
        //3.调用方法操作
        bean.set("jedisclientkey11", "jedisclientkey");
        System.out.println(bean.get("jedisclientkey11"));
    }
}
