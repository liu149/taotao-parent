import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.HashMap;

public class SolrjTest {
//	@Test
//	public void add() throws Exception{
//		//1.创建solrserver   建立连接 需要指定地址
//		SolrServer solrServer = new HttpSolrServer("http://192.168.0.12:8080/solr/taotao");
//		//2.创建solrinputdocument
//		SolrInputDocument document = new SolrInputDocument();
//
//		//3.向文档中添加域
//		document.addField("id", "test001");
//		document.addField("item_title", "这是一个测试");
//		//4.将文档提交到索引库中
//		solrServer.add(document);
//		//5.提交
//		solrServer.commit();
//	}
//
//	@Test
//	public void delete() throws  Exception{
//		SolrServer solrServer = new HttpSolrServer("http://192.168.0.12:8080/solr/taotao");
//		SolrInputDocument document = new SolrInputDocument();
////		document.addField("id","test001");
//		solrServer.deleteById("test001");
//		solrServer.commit();
//
//	}
//
//
//	@Test
//	public void testquery() throws Exception{
//		//1.创建solrserver对象
//		SolrServer solrServer = new HttpSolrServer("http://192.168.0.12:8080/solr/taotao");
//		//2.创建solrquery对象   设置各种过滤条件，主查询条件 排序
//		SolrQuery query = new SolrQuery();
//		//3.设置查询的条件
//		query.setQuery("测试");
////		query.addFilterQuery("item_price:[0 TO 300000000]");
//		query.set("df", "item_keywords");
//		//4.执行查询
//		QueryResponse response = solrServer.query(query);
//		//5.获取结果集
//		SolrDocumentList results = response.getResults();
//		System.out.println("查询的总记录数"+results.getNumFound());
//		//6.遍历结果集  打印
//		for (SolrDocument solrDocument : results) {
//			System.out.println(solrDocument.get("id"));
//			System.out.println(solrDocument.get("item_title"));
//		}

//		String zkHost = "192.168.0.12:8080";
//		SolrClient solrClient = new HttpSolrClient.Builder(zkHost).build();
//	}

	@Test
	public void testSolrCloudAddDocument() throws Exception {
		// 第一步：把solrJ相关的jar包添加到工程中。
		// 第二步：创建一个SolrServer对象，需要使用CloudSolrServer子类。构造方法的参数是zookeeper的地址列表。
		//参数是zookeeper的地址列表，使用逗号分隔
//		CloudSolrServer solrServer = new CloudSolrServer("192.168.0.12:2181,192.168.0.12:2182,192.168.0.12:2183");
////		// 第三步：需要设置DefaultCollection属性。
////		solrServer.setDefaultCollection("mycollection1");
////		// 第四步：创建一SolrInputDocument对象。
////		SolrInputDocument document = new SolrInputDocument();
////		// 第五步：向文档对象中添加域
////		document.addField("item_title", "测试商品");
////		document.addField("item_price", "100");
////		document.addField("id", "test001");
////		// 第六步：把文档对象写入索引库。
////		solrServer.add(document);
////		// 第七步：提交。
////		solrServer.commit();

		String zkHost = "http://192.168.0.12:8080/solr/taotao";

		HttpSolrClient client = new HttpSolrClient.Builder().withBaseSolrUrl(zkHost).build();


		SolrQuery query = new SolrQuery();
		query.setQuery("手机");
		query.set("df","item_keywords");
		QueryResponse response = client.query(query);
		//5.获取结果集
		SolrDocumentList results = response.getResults();
		System.out.println("查询的总记录数"+results.getNumFound());




//		client.setDefaultCollection("mycollection1");
//
//		SolrInputDocument document = new SolrInputDocument();
//		document.addField("id","c0001");
//		document.addField("title_ik","使用solrJ添加的文档");
//		document.addField("content_ik","文档的内容");
//		document.addField("product_name","商品名称");// 3、 通过HttpSolrServer对象将SolrInputDocument添加到索引库。
//
//		client.add(document);
//		client.commit();

	}

	@Test
	public void testHttpSolrClient() throws IOException, SolrServerException {
		ClassPathXmlApplicationContext context =
				new ClassPathXmlApplicationContext("spring/applicationContext-solr.xml");
		HttpSolrClient client = (HttpSolrClient)context.getBean("httpSolrClient");
		SolrQuery query = new SolrQuery();
		query.setQuery("手机");
		query.set("df","item_keywords");
		QueryResponse response = client.query(query);
		//5.获取结果集
		SolrDocumentList results = response.getResults();
		System.out.println("查询的总记录数"+results.getNumFound());
	}

}
