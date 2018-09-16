package com.taotao.content.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

/**
 * 内容处理的接口
 * @title ContentService.java
 * <p>description</p>
 * <p>company: www.itheima.com</p>
 * @author ljh 
 * @version 1.0
 */
public interface ContentService {
	/**
	 * 插入内容表
	 * @param content
	 * @return
	 */
	public TaotaoResult saveContent(TbContent content);

	/**
	 * 查询内容列表
	 * @param categoryId
	 * @return
	 */
	public EasyUIDataGridResult getContentList(Long categoryId,Integer page,Integer rows);

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public TaotaoResult deleteContentList(String ids);

	/**
	 * 根据类目id查询内容列表
	 * @param catId
	 * @return
	 */
	public List<TbContent> getContentListByCatId(Long catId);
}
