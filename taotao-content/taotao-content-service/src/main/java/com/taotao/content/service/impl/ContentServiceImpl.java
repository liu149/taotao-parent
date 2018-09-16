package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.content.jedis.JedisClient;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;

import javax.annotation.Resource;

@Service
public class ContentServiceImpl implements ContentService {

	@Resource(name="jedisClientCluster")
	private JedisClient jedisClient;

    @Value("${CONTENT_KEY}")
    private String CONTENT_KEY;

	@Autowired
	private TbContentMapper mapper;
	
	@Override
	public TaotaoResult saveContent(TbContent content) {
		//1.注入mapper
		
		//2.补全其他的属性
		content.setCreated(new Date());
		content.setUpdated(content.getCreated());
		//3.插入内容表中
		mapper.insertSelective(content);

        //当添加内容的时候，需要清空此内容所属的分类下的所有的缓存
        try {
            jedisClient.hdel(CONTENT_KEY, content.getCategoryId()+"");
            System.out.println("当插入时，清空缓存!!!!!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
		return TaotaoResult.ok();
	}

	@Override
	public EasyUIDataGridResult getContentList(Long categoryId,Integer page,Integer rows) {
		PageHelper.startPage(page, rows);
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> contentList = mapper.selectByExample(example);
		PageInfo<TbContent> info = new PageInfo<TbContent>(contentList);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(info.getSize());
		result.setRows(info.getList());
		return result;
	}

    @Override
    public TaotaoResult deleteContentList(String ids) {
	    String[] contentIds = ids.split(",");
	    for(String contentId : contentIds){
	        Long id = Long.valueOf(contentId);
	        mapper.deleteByPrimaryKey(id);
        }
        //删除内容的时候，清空缓存
        //先查找categoryId
        Long categoryId = mapper.selectCategoryIdById(contentIds[0]);
        try {
            jedisClient.hdel(CONTENT_KEY, categoryId+"");
            System.out.println("当插入时，清空缓存!!!!!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TaotaoResult.ok();
    }

	@Override
	public List<TbContent> getContentListByCatId(Long categoryId) {
        //判断 是否redis中有数据  如果有   直接从redis中获取数据 返回

        try {
            String jsonstr = jedisClient.hget(CONTENT_KEY, categoryId+"");//从redis数据库中获取内容分类下的所有的内容。
            //如果存在，说明有缓存
            if(StringUtils.isNotBlank(jsonstr)){
                System.out.println("这里有缓存啦！！！！！");
                return JsonUtils.jsonToList(jsonstr, TbContent.class);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> tbContents = mapper.selectByExample(example);

        //将数据写入到redis数据库中

        // 注入jedisclient
        // 调用方法写入redis   key - value
        try {
            System.out.println("没有缓存！！！！！！");
            jedisClient.hset(CONTENT_KEY, categoryId+"", JsonUtils.objectToJson(tbContents));
        } catch (Exception e) {
            e.printStackTrace();
        }

		return tbContents;
	}

}
